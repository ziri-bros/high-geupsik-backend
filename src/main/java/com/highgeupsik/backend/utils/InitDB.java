package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import com.highgeupsik.backend.repository.SchoolRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        //initService.dbInit();
    }

    @RequiredArgsConstructor
    @Transactional
    @Component
    static class InitService {

        private final SchoolRepository schoolRepository;

        public void dbInit() {
            List<School> schoolList = new ArrayList<>();
            for (Region region : Region.values()) {
                ClassPathResource resource = new ClassPathResource("/static/schools/" + region.toString() + ".json");
                ParsingUtils.addSchoolListFromJsonFile(resource, schoolList);
            }
            schoolRepository.saveAll(schoolList);
        }
    }
}
