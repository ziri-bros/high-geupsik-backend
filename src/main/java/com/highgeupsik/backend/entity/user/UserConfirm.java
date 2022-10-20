package com.highgeupsik.backend.entity.user;

import com.highgeupsik.backend.entity.school.StudentCard;
import com.highgeupsik.backend.entity.TimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserConfirm extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_confirm_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_card_id")
    private StudentCard studentCard;

    @Builder
    public UserConfirm(User user, StudentCard studentCard) {
        this.user = user;
        this.studentCard = studentCard;
    }
}
