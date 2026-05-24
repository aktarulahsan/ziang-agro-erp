package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class Brand extends BaseEntity {
    @Column(nullable = false, unique = true, length = 80)
    private String name;
}

