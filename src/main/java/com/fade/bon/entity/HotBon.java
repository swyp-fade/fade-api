package com.fade.bon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "hot_bons")
@Entity
@Getter
@NoArgsConstructor
public class HotBon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bon_id", nullable = false)
    private Bon bon;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Builder
    public HotBon(Bon bon, Integer rank) {
        this.bon = bon;
        this.rank = rank;
    }
}
