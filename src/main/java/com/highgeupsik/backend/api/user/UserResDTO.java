package com.highgeupsik.backend.api.user;

import com.highgeupsik.backend.api.school.SchoolResDTO;
import com.highgeupsik.backend.entity.user.Role;
import com.highgeupsik.backend.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResDTO {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private int grade;
    private int classNum;
    private String studentCardImage;
    private SchoolResDTO schoolResDTO;

    public UserResDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        username = user.getUsername();
        role = user.getRole();
        grade = user.getStudentCard().getGrade().getGradeNum();
        classNum = user.getStudentCard().getClassNum();
        studentCardImage = user.getStudentCard().getStudentCardImage();
        schoolResDTO = new SchoolResDTO(user.getStudentCard().getSchool());
    }
}
