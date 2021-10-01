package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.UserCardResDTO;
import com.highgeupsik.backend.entity.QUserCard;
import com.highgeupsik.backend.entity.UserCard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.highgeupsik.backend.entity.QUserCard.*;


public class UserCardRepositoryImpl implements UserCardRepositoryCustom {


    private final JPAQueryFactory queryFactory;

    public UserCardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserCardResDTO> findUserCards(Pageable pageable) {
        List<UserCard> content = queryFactory.selectFrom(userCard)
//                .join(userCard.uploadFile, uploadFile).fetchJoin()
//                .join(userCard.user, user).fetchJoin()
                .orderBy(userCard.createdDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(userCard)
                .fetchCount();

        List<UserCardResDTO> result = content.stream().map(userCard -> new UserCardResDTO(userCard))
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, total);
    }

}
