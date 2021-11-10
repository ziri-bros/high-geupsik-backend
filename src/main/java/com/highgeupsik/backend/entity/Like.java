package com.highgeupsik.backend.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    private Boolean flag = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_detail_id")
    private BoardDetail boardDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Like(User user) {
        this.user = user;
    }

    public Like update() {
        this.flag = !this.flag;
        return this;
    }

    public void setBoardDetail(BoardDetail boardDetail) {
        this.boardDetail = boardDetail;
        if (!boardDetail.getLikeList().contains(this)) {
            boardDetail.getLikeList().add(this);
        }
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        if (!comment.getLikeList().contains(this)) {
            comment.getLikeList().add(this);
        }
    }
}
