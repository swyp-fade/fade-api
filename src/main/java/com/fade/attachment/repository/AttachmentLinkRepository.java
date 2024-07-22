package com.fade.attachment.repository;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.entity.AttachmentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentLinkRepository extends JpaRepository<AttachmentLink, Long> {
    Optional<AttachmentLink> findByLinkableIdAndAttachmentLinkableTypeAndType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
    Boolean existsByLinkableIdAndAttachmentLinkableTypeAndType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
    void deleteByLinkableIdAndAttachmentLinkableTypeAndType(Long linkableId, AttachmentLinkableType attachmentLinkableType, AttachmentLinkType type);
}
