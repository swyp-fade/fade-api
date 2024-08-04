package com.fade.feed.entity;

import com.fade.faparchiving.entity.FapArchiving;
import com.fade.member.entity.Member;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.style.entity.Style;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "feeds")
@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE feeds SET deleted_at=NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany()
    @JoinTable(
            name = "feed_styles",
            joinColumns = @JoinColumn(name = "feed_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<Style> styles = new HashSet<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedOutfit> feedOutfitList = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "feed")
    private List<FapArchiving> fapArchivingList = new ArrayList<>();

    public Feed(
            Member member,
            Collection<Style> styles,
            Collection<FeedOutfit> feedOutfitList
    ) {
        this.member = member;
        this.styles = new HashSet<>(styles);
        this.feedOutfitList = new ArrayList<>(feedOutfitList);

        feedOutfitList.forEach(f -> f.setFeed(this));
    }

    public void remove() {
        this.deletedAt = LocalDateTime.now();
    }

    public void modifyStyles(Collection<Style> styles) {
        this.styles = new HashSet<>(styles);
    }

    public void modifyOutfits(Collection<FeedOutfit> feedOutfitList) {
        this.feedOutfitList = new ArrayList<>(feedOutfitList);
    }

    public void publishEvent(ApplicationEventPublisher eventPublisher, CreateNotificationDto createNotificationDto) {
        eventPublisher.publishEvent(createNotificationDto);
    }
}
