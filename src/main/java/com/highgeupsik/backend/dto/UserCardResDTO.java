package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.UserCard;
import lombok.Getter;

@Getter
public class UserCardResDTO {

    private Long id;
    private String thumbnail;
    private UserResDTO userResDTO;
    private SchoolDTO schoolDTO;

    public UserCardResDTO(UserCard userCard) {
        id = userCard.getId();
        thumbnail = userCard.getThumbnail();
        userResDTO = new UserResDTO(userCard.getUser());
        schoolDTO = new SchoolDTO(userCard.getSchool());
    }
}
