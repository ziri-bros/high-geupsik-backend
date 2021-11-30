package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.UserConfirm;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserConfirmDTO {

    private Long userId;
    private String username;
    private String email;
    private String studentCardImage;

    public UserConfirmDTO(UserConfirm userConfirm){
        userId = userConfirm.getUser().getId();
        username = userConfirm.getUser().getUsername();
        email = userConfirm.getUser().getEmail();
        studentCardImage = userConfirm.getStudentCard().getStudentCardImage();
    }
}
