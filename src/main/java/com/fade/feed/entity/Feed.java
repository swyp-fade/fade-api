package com.fade.feed.entity;

import com.fade.bookmark.entity.Bookmark;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "feeds")
@Entity
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "styles", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> styles;

    @OneToMany(mappedBy = "feed")
    private Set<FeedDressedUp> feedDressedUpList = new HashSet<>();

    @OneToMany(mappedBy = "feed")
    private Set<Bookmark> bookmarks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
