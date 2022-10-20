package com.highgeupsik.backend.api.admin;

import com.highgeupsik.backend.entity.user.UserConfirm;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserConfirmDTO {

    private Long userId;
    private String username;
    private String email;
    private String studentCardImage;

    public UserConfirmDTO(UserConfirm userConfirm) {
        userId = userConfirm.getUser().getId();
        username = userConfirm.getUser().getUsername();
        email = userConfirm.getUser().getEmail();
        studentCardImage = userConfirm.getStudentCard().getStudentCardImage();
    }
}
