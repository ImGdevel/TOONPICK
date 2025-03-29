package toonpick.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class MemberStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "activity_score")
    private int activityScore = 0;

    @Column(name = "watching_count")
    private int watchingCount = 0;

    @Column(name = "bookmark_count")
    private int bookmarkCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @Column(name = "comment_like_count")
    private int commentLikeCount = 0;

    @Column(name = "total_like_received")
    private int totalLikeReceived = 0;

    @Column(name = "total_like_given")
    private int totalLikeGiven = 0;

    @Column(name = "report_count")
    private int reportCount = 0;

    @Column(name = "reported_count")
    private int reportedCount = 0;

    @Column(name = "reported_comment_count")
    private int reportedCommentCount = 0;

}
