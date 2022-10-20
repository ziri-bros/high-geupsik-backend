package com.highgeupsik.backend.api.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentReqDTO {

    private String content;
    private Long parentId;
}
