package com.fade.bon.entity;

import com.fade.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "bons")
@Entity
@Getter
@NoArgsConstructor
public class Bon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @OneToMany(mappedBy = "bon", cascade = CascadeType.ALL)
    private List<BonComment> bonComments = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Bon(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
