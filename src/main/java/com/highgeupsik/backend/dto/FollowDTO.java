package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Follow;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowDTO {

    private Long id;
    private Long friendId;
    private String friendName;

    public FollowDTO(Follow follow) {
        id = follow.getId();
        friendId = follow.getFromUser().getId();
        friendName = follow.getFromUserName();
    }

}
