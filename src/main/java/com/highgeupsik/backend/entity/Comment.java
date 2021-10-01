package com.highgeupsik.backend.entity;


import com.highgeupsik.backend.dto.CommentReqDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Comment extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private int userCount;

    private int likeCount = 0;

    private boolean deleteFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_detail_id")
    private BoardDetail boardDetail;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, int userCount, User user, BoardDetail boardDetail,
                   Comment parent) {
        this.content = content;
        this.userCount = userCount;
        this.user = user;
        this.parent = parent;
        this.boardDetail = boardDetail;
    }

    public void setBoardDetail(BoardDetail boardDetail) {
        this.boardDetail = boardDetail;
        if (!boardDetail.getCommentList().contains(this)) {
            boardDetail.getCommentList().add(this);
        }
    }

    public void updateContent(CommentReqDTO commentReqDTO) {
        content = commentReqDTO.getContent();
    }

    public void updateCommentLike(Boolean flag) {
        if (flag)
            this.likeCount++;
        else if (!flag && likeCount > 0)
            this.likeCount--;
    }

    public void delete() {
        deleteFlag = true;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
        if (!parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }
}
