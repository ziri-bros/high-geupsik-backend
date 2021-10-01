package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDetailReqDTO {

    private String title;
    private String content;
    private Category category;
    private List<MultipartFile> boardImageList = new ArrayList<>();

}
