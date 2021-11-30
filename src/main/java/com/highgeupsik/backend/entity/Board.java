package com.highgeupsik.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "BOARD")
@Entity
public class Board extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int likeCount = 0;

    private int nextAnonymousNumber = 0;

    private int commentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String thumbnail;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFileList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @Builder
    public Board(String title, String content, Category category, Region region, User user, String thumbnail) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.region = region;
        this.thumbnail = thumbnail;
    }

    public void updateBoard(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void updateBoard(String title, String content, String thumbnail, Category category) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.category = category;
    }

    public void updateBoardLikeCount(Boolean flag) {
        if (flag) {
            this.likeCount += 1;
        } else if (!flag && this.likeCount > 0) {
            this.likeCount -= 1;
        }
    }

    public int getNextAnonymousNumber() {
        increaseAnonymousNumber();
        return nextAnonymousNumber;
    }

    private void increaseAnonymousNumber() {
        this.nextAnonymousNumber += 1;
    }

    public void updateBoardCommentCount(Boolean flag) {
        if (flag) {
            this.commentCount += 1;
        } else if (!flag && this.commentCount > 0) {
            this.commentCount -= 1;
        }
    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
        commentCount -= 1;
    }

    public void setFile(UploadFile file) {
        this.uploadFileList.add(file);
        if (file.getBoard() != this) {
            file.setBoard(this);
        }
    }

    public void deleteFiles() {
        this.thumbnail = null;
        this.uploadFileList.clear();
    }

    public boolean isWriter(User user) {
        return this.user.equals(user);
    }

}
