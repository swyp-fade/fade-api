package com.fade.attachment.repository;

import com.fade.attachment.entity.AttachmentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentLinkRepository extends JpaRepository<AttachmentLink, Long> {
}
