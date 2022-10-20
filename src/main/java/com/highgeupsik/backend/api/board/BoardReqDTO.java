package com.highgeupsik.backend.api.board;

import com.highgeupsik.backend.api.image.UploadFileDTO;
import com.highgeupsik.backend.entity.board.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardReqDTO {

    private String title;
    private String content;
    private Category category;
    private List<UploadFileDTO> uploadFileDTOList = new ArrayList<>();
}
