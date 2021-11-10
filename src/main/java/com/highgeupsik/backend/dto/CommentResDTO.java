package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResDTO {

    private Long id;
    private Long writerId;
    private String content;
    private int userCount;
    private int likeCount;
    private List<CommentResDTO> commentResDTOList = new ArrayList<>();

    public CommentResDTO(Comment comment) {
        id = comment.getId();
        writerId = comment.getUser().getId();
        content = comment.getContent();
        userCount = comment.getUserCount();
        likeCount = comment.getLikeCount();
        commentResDTOList = comment.getChildren().stream().map((children) -> new CommentResDTO(children))
            .collect(Collectors.toList());
    }

    public CommentResDTO(Long id, Long writerId, String content, int userCount, int likeCount) {
        this.id = id;
        this.writerId = writerId;
        this.content = content;
        this.userCount = userCount;
        this.likeCount = likeCount;
    }

}
