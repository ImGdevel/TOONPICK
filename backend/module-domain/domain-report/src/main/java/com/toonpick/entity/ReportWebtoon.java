package com.toonpick.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("WEBTOON")
public class ReportWebtoon extends Report {

    @ManyToOne
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

}
