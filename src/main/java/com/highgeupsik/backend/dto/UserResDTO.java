package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Role;
import com.highgeupsik.backend.entity.SchoolInfo;
import com.highgeupsik.backend.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResDTO {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private SchoolInfo schoolInfo;
    private UploadFileDTO profileImage;

    public UserResDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        username = user.getUsername();
        role = user.getRole();
        schoolInfo = user.getSchoolInfo();
    }
}
