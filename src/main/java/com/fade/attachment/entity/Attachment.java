package com.fade.attachment.entity;

import com.fade.attachment.constant.AttachmentStatus;
import com.fade.attachment.constant.AttachmentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attachments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000, nullable = false)
    private String path;

    @Column(length = 1000, nullable = false)
    private String filename;

    @Column(length = 1000)
    private String originalFilename;

    @OneToMany(mappedBy = "attachment", orphanRemoval = true, cascade = {
            CascadeType.ALL
    })
    private Set<AttachmentLink> attachmentLinks = new HashSet<>();

    @Column(name = "uploader_member_id", nullable = false)
    private Long uploaderMemberId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttachmentStatus status;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @Column()
    private String checksum;

    public Attachment(
        String path,
        String filename,
        String originalFilename,
        Long uploaderMemberId,
        AttachmentStatus status,
        AttachmentType type,
        String checksum
    ) {
        this.path = path;
        this.filename = filename;
        this.originalFilename = originalFilename;
        this.uploaderMemberId = uploaderMemberId;
        this.status = status;
        this.type = type;
        this.checksum = checksum;
    }

    public Attachment(
        String path,
        String filename,
        String originalFilename,
        Long uploaderMemberId,
        AttachmentStatus status,
        AttachmentType type
    ) {
        this(path, filename, originalFilename, uploaderMemberId, status, type, null);
    }

    public void successUpload() {
        this.status = AttachmentStatus.SUCCESS;
    }
}