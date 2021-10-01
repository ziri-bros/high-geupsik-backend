package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.UserCard;
import lombok.Getter;

@Getter
public class UserCardResDTO {

    private Long id;
    private String username;
    private String email;
    private UploadFileDTO cardImage;

    public UserCardResDTO(UserCard userCard){
        id = userCard.getId();
        username = userCard.getUser().getUsername();
        email = userCard.getUser().getEmail();
        cardImage = new UploadFileDTO(userCard.getUploadFile());
    }
}
