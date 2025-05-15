package com.toonpick.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "platform")
public class Platform {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

}
