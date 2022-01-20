package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class BoardReqDTO {

    private String title;
    private String content;
    private Category category;
    private List<UploadFileDTO> uploadFileDTOList = new ArrayList<>();
}
