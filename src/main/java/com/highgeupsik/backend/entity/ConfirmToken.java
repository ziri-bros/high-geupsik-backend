package com.highgeupsik.backend.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmToken extends TimeEntity {

    @Id
    @Column(name = "confirm_id", length = 36)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private LocalDateTime expirationDate;

    private boolean expired = false;

    private Long userId;

    @Builder
    public ConfirmToken(Long userId, LocalDateTime expirationDate) {
        this.userId = userId;
        this.expirationDate = expirationDate;
    }

    public void useToken() {
        expired = true;
    }

}
