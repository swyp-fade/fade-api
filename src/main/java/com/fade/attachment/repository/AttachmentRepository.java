package com.fade.attachment.repository;

import com.fade.attachment.constant.AttachmentStatus;
import com.fade.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    boolean existsByChecksumAndStatus(String checksum, AttachmentStatus status);
}
