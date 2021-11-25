package com.highgeupsik.backend.dto;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCardReqDTO {

    @NotNull
    private String thumbnail;
    @NotNull
    private SchoolDTO schoolDTO;

}
