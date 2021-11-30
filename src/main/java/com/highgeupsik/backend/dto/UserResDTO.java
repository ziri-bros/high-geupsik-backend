package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResDTO {

    private Long id;
    private String email;
    private String username;
    private int grade;
    private int classNum;
    private String studentCardImage;
    private SchoolDTO schoolDTO;

    public UserResDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        username = user.getUsername();
        grade = user.getStudentCard().getGrade().getGradeNum();
        classNum = user.getStudentCard().getClassNum();
        studentCardImage = user.getStudentCard().getStudentCardImage();
        schoolDTO = new SchoolDTO(user.getSchool());
    }
}
