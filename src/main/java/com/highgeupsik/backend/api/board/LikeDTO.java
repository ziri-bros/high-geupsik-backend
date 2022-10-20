package com.highgeupsik.backend.api.board;

import com.highgeupsik.backend.entity.board.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LikeDTO {

    private boolean likeFlag;

    public LikeDTO(Like like) {
        likeFlag = like.getFlag();
    }
}
