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
import com.fade.global.component.FileUploader;
import com.fade.global.component.FileUtils;
import com.fade.global.component.PresignUrlGenerator;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentLinkRepository attachmentLinkRepository;
    private final AttachmentCommonService attachmentCommonService;
    private final FileUtils fileUtils;
    private final PresignUrlGenerator presignUrlGenerator;
    private final FileUploader fileUploader;

    @Value("${aws.region}")
    private String region;
    @Value("${aws.s3.bucketName}")
    private String bucket;

    @Transactional
    public Long uploadFile(Long uploaderMemberId, MultipartFile file) {
        final var filename = this.fileUtils.getRandomFilename();
        final var path = this.fileUtils.generateFilepath();

        fileUploader.upload(
                file, path + "/" + filename
        );

        final var attachment = new Attachment(
                path,
                filename,
                null,
                uploaderMemberId,
                AttachmentStatus.SUCCESS,
                AttachmentType.IMAGE
        );

        this.attachmentRepository.save(attachment);

        return attachment.getId();
    }

    @Transactional
    public GeneratePresignURLResponse generatePresignUrl(
            Long uploaderMemberId,
            String checksum
    ) {
        if (checksum != null) {
            if (this.existsAttachmentByChecksum(checksum)) {
                throw new ApplicationException(ErrorCode.ALREADY_EXISTS_ATTACHMENT);
            }
        }

        final var filename = this.fileUtils.getRandomFilename();
        final var path = this.fileUtils.generateFilepath();

        final var attachment = new Attachment(
                path,
                filename,
                null,
                uploaderMemberId,
                AttachmentStatus.UPLOAD_WAIT,
                AttachmentType.IMAGE,
                checksum
        );

        this.attachmentRepository.save(attachment);

        final var presignUrl = this.presignUrlGenerator.getPreSignedUrl(
                path + "/" + filename
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

    public String getUrl(
            Long linkableId,
            AttachmentLinkableType attachmentLinkableType,
            AttachmentLinkType attachmentLinkType
    ) {
        final var attachmentLink =  this.attachmentLinkRepository.findByLinkableIdAndAttachmentLinkableTypeAndType(
                linkableId,
                attachmentLinkableType,
                attachmentLinkType
        ).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ATTACHMENT));

        return this.getRootUrl() + "/" + attachmentLink.getAttachment().getPath() + '/' + attachmentLink.getAttachment().getFilename();
    }

    public boolean existsLinkable(
            Long linkableId,
            AttachmentLinkableType attachmentLinkableType,
            AttachmentLinkType attachmentLinkType
    ) {
        return this.attachmentLinkRepository.existsByLinkableIdAndAttachmentLinkableTypeAndType(
                linkableId,
                attachmentLinkableType,
                attachmentLinkType
        );
    }

    public void unLink(
            Long linkableId,
            AttachmentLinkableType attachmentLinkableType,
            AttachmentLinkType attachmentLinkType
    ) {
        this.attachmentLinkRepository.deleteByLinkableIdAndAttachmentLinkableTypeAndType(
                linkableId,
                attachmentLinkableType,
                attachmentLinkType
        );
    }

    private String getRootUrl() {
        return "https://" + this.bucket + ".s3." + this.region + ".amazonaws.com/attachments";
    }
}
