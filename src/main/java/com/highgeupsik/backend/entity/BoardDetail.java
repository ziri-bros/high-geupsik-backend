package com.highgeupsik.backend.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDetail extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_detail_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int likeCount = 0;

    private int userCount = 0;

    private int commentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile thumbnail;

    @OneToMany(mappedBy = "boardDetail", cascade = CascadeType.ALL)
    private List<UploadFile> uploadFileList = new ArrayList<>();

    @OneToMany(mappedBy = "boardDetail", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "boardDetail", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @Builder
    public BoardDetail(String title, String content, Category category,
                       Region region, User user, UploadFile thumbnail) {
        this.title = title;
        this.content = content;
        this.category  = category;
        this.user = user;
        this.region = region;
        this.thumbnail = thumbnail;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateBoardLikeCount(Boolean flag) {
        if (flag)
            this.likeCount += 1;
        else if (!flag && this.likeCount > 0)
            this.likeCount -= 1;
    }

    public void updateBoardUserCount() {
        this.userCount += 1;
    }

    public void updateBoardCommentCount(Boolean flag) {
        if (flag)
            this.commentCount += 1;
        else if (!flag && this.commentCount > 0)
            this.commentCount -= 1;
    }

    public void setFile(UploadFile file) {
        this.uploadFileList.add(file);
        if (file.getBoardDetail() != this) {
            file.setBoardDetail(this);
        }
    }

    public void deleteFiles() {
        this.uploadFileList.clear();
    }

}
