package com.highgeupsik.backend.dto;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCardReqDTO {

    @NotNull
    private UploadFileDTO uploadFileDTO;

}
