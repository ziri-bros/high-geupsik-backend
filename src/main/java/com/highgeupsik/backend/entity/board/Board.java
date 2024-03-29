package com.highgeupsik.backend.entity.board;

import static com.highgeupsik.backend.exception.ErrorMessages.WRITER_NOT_MATCH;

import com.highgeupsik.backend.entity.TimeEntity;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.UserException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean deleteFlag = false;

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

    public static Board ofBoard() { //테스트를 위한 스태틱 메소드
        return new Board();
    }

    public void validateWriter(User other) {
        if (!isWriter(other)) {
            throw new UserException(WRITER_NOT_MATCH);
        }
    }

    public boolean isWriter(User other) {
        return user.isSameUser(other);
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
    }

    public void deleteCommentCount() {
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

    public void delete() {
        deleteFlag = true;
    }
}