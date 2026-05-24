package com.agroerp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String companyCode;

    @Column(nullable = false, length = 160)
    private String companyName;

    private String tradeLicenseNo;
    private String taxRegistrationNo;
    private String mobileNumber;
    private String email;

    @Column(length = 500)
    private String address;
}
