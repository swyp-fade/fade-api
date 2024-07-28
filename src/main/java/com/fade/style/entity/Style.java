package com.fade.style.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "styles")
@Entity
@Getter
public class Style {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;
}
