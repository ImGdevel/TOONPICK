package com.toonpick.domain.review.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("REVIEW")
public class ReportReview extends Report {

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private WebtoonReview review;

}
