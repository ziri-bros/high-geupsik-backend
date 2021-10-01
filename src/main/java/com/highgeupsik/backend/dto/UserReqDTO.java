package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserReqDTO {

    private String email;
    private String password;
    private String username;
    private String nickname;
    private String schoolName;
    private String schoolCode;
    private Region region;
    private List<MultipartFile> profileImage = new ArrayList<>();

}
