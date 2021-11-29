package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Role;
import com.highgeupsik.backend.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResDTO {

    private Long id;
    private String email;
    private String username;
    private String studentCardImage;
    private SchoolDTO schoolDTO;

    public UserResDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        username = user.getUsername();
        studentCardImage = user.getStudentCard().getStudentCardImage();
        schoolDTO = new SchoolDTO(user.getSchool());
    }
}
