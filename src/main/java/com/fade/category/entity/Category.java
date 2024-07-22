package com.fade.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "categories")
@Getter
public class Category {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;
}
