package com.fade.attachment.entity;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(
        name = "attachment_links",
        indexes = {
                @Index(
                        columnList="linkable_id,attachment_linkable_type,type"
                )
        }
)
@Getter
public class AttachmentLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;

    @Column(nullable = false, name = "attachment_linkable_type")
    @Enumerated(EnumType.STRING)
    private AttachmentLinkableType attachmentLinkableType;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private AttachmentLinkType type;

    @Column(nullable = false, name = "linkable_id")
    private Long linkableId;
}