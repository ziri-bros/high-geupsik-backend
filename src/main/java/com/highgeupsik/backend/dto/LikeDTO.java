package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDTO {

    private boolean likeFlag;

    public LikeDTO(Like like){
        likeFlag = like.getFlag();
    }

}
