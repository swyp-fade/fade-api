package com.fade.attachment.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.constant.AttachmentStatus;
import com.fade.attachment.constant.AttachmentType;
import com.fade.attachment.dto.response.GeneratePresignURLResponse;
import com.fade.attachment.entity.Attachment;
import com.fade.attachment.entity.AttachmentLink;
import com.fade.attachment.repository.AttachmentLinkRepository;
import com.fade.attachment.repository.AttachmentRepository;
import com.fade.global.component.FileUtils;
import com.fade.global.component.PresignUrlGenerator;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentLinkRepository attachmentLinkRepository;
    private final AttachmentCommonService attachmentCommonService;
    private final FileUtils fileUtils;
    private final PresignUrlGenerator presignUrlGenerator;

    @Transactional
    public GeneratePresignURLResponse genereatePresignUrl(
            Long uploaderMemberId,
            String checksum
    ) {
        if (checksum != null) {
            if (this.existsAttachmentByChecksum(checksum)) {
                throw new ApplicationException(ErrorCode.ALREADY_EXISTS_ATTACHMENT);
            }
        }

        final var attachment = new Attachment(
                this.fileUtils.generateFilepath(),
                this.fileUtils.getRandomFilename(),
                null,
                uploaderMemberId,
                AttachmentStatus.UPLOAD_WAIT,
                AttachmentType.IMAGE,
                checksum
        );

        this.attachmentRepository.save(attachment);

        final var presignUrl = this.presignUrlGenerator.getPreSignedUrl(
                this.fileUtils.generateFilepath() + "/" +
                this.fileUtils.getRandomFilename()
        );

        return new GeneratePresignURLResponse(
                presignUrl,
                attachment.getId()
        );
    }

    public boolean existsAttachmentByChecksum(String checksum) {
        return this.attachmentRepository.existsByChecksum(checksum);
    }

    @Transactional
    public Long linkAttachment(
            Long attachmentId,
            AttachmentLinkableType attachmentLinkableType,
            AttachmentLinkType type,
            Long linkableId
    ) {
        final var attachment = this.attachmentCommonService.findById(attachmentId);
        attachment.successUpload();

        final var attachmentLink = new AttachmentLink(
                attachment,
                type,
                attachmentLinkableType,
                linkableId
        );

        this.attachmentLinkRepository.save(attachmentLink);

        return attachmentLink.getId();
    }
}
