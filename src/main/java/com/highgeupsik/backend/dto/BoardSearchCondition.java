package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class BoardSearchCondition {

    private Region region;
    private String keyword;
    private Category category;
    private Integer likeCount;

    public void setRegion(Region region){
        this.region = region;
    }

}
