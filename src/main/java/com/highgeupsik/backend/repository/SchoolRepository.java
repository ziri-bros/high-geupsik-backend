package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findByRegionAndName(Region region, String name);
}
