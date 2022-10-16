package com.highgeupsik.backend.timetable;

import lombok.Getter;

@Getter
public class Subject {

    String perio;
    String name;
    String lectureRoom;

    public Subject(String perio, String name, String lectureRoom) {
        this.perio = perio;
        this.name = name;
        this.lectureRoom = lectureRoom;
    }
}
