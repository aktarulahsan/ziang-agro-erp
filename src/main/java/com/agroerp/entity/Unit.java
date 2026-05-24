package com.agroerp.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "units")
@Getter
@Setter
public class Unit extends BaseEntity {
    @Column(nullable = false, unique = true, length = 30)
    private String name;
}

