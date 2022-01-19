package com.highgeupsik.backend.entity;

import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.utils.ErrorMessage;
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadFile> uploadFileList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void checkWriter(Long userId) {
        if (!isWriter(userId)) {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public boolean isWriter(Long userId) {
        return user.getId().equals(userId);
    }

    public void updateBoard(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void updateBoardLikeCount(Boolean flag) {
        if (flag) {
            this.likeCount += 1;
        } else if (this.likeCount > 0) {
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

    public void addComment(Comment comment) {
        commentList.add(comment);
        commentCount++;
    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
        if (commentCount > 0) {
            commentCount--;
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

}
