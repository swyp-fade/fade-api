package com.fade.feed.entity;

import com.fade.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feed_outfits")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedOutfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "brand_name")
    private String brandName;

    @Column(nullable = false, name = "product_name")
    private String detail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public FeedOutfit(
            String brandName,
            String detail,
            Category category
    ) {
        this.brandName = brandName;
        this.detail = detail;
        this.category = category;
    }
}
