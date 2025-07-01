package com.toonpick.domain.review.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("MEMBER")
public class ReportMember {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
