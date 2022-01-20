package com.highgeupsik.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserReqDTO {

    private StudentCardDTO studentCardDTO;
    private SchoolDTO schoolDTO;
}
