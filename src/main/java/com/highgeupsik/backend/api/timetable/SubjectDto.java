package com.highgeupsik.backend.api.timetable;

import lombok.Getter;

@Getter
public class SubjectDto {

    String perio;
    String name;
    String lectureRoom;

    public SubjectDto(String perio, String name, String lectureRoom) {
        this.perio = perio;
        this.name = name;
        this.lectureRoom = lectureRoom;
    }
}
