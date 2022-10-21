package com.highgeupsik.backend.entity.user;

import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.board.Comment;
import com.highgeupsik.backend.entity.school.StudentCard;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"email", "provider"}
        )
    }
)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_card_id")
    private StudentCard studentCard;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public User(String email, String username, AuthProvider provider,
        Role role) {
        this.email = email;
        this.username = username;
        this.provider = provider;
        this.role = role;
    }

    public static User ofUser() { //테스트를 위한 스태틱 메소드
        return new User();
    }

    public void setStudentCard(StudentCard studentCard) {
        this.studentCard = studentCard;
    }

    public void updateRoleUser() {
        role = Role.ROLE_USER;
    }

    public void updateRoleGuest() {
        role = Role.ROLE_GUEST;
    }

    public User updateName(String username) {
        this.username = username;
        return this;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSameUser(User other){
        return id.equals(other.getId());
    }
}
