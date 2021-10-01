package com.highgeupsik.backend.dto;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserCardReqDTO {

    @NotNull
    private List<MultipartFile> cardImages = new ArrayList<>();

}
