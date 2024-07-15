package com.fade.attachment.service;

import com.fade.attachment.entity.Attachment;
import com.fade.attachment.repository.AttachmentRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentCommonService {
    private final AttachmentRepository attachmentRepository;

    public Attachment findById(Long id) {
        return this.attachmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ATTACHMENT));
    }
}
