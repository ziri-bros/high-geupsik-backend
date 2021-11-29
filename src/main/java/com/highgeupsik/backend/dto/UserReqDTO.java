package com.highgeupsik.backend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReqDTO {

    private String studentCardImage;
    private SchoolDTO schoolDTO;

}
