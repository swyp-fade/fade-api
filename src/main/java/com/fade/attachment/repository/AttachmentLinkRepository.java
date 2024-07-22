package com.fade.attachment.repository;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.entity.AttachmentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentLinkRepository extends JpaRepository<AttachmentLink, Long> {
    Optional<AttachmentLink> findByLinkableIdAndTypeAndAttachmentLinkableType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
    Boolean existsByLinkableIdAndTypeAndAttachmentLinkableType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
    void deleteByLinkableIdAndTypeAndAttachmentLinkableType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
}
