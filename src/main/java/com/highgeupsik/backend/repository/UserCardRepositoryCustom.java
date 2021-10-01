package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.UserCardResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCardRepositoryCustom {


    Page<UserCardResDTO> findUserCards(Pageable pageable);

}
