package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
