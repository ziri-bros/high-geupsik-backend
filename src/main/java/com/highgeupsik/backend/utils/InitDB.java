package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.entity.School;
import com.highgeupsik.backend.repository.SchoolRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @RequiredArgsConstructor
    @Transactional
    @Component
    static class InitService {

        private final SchoolRepository schoolRepository;

        public void dbInit() {
            String fileLocation = "";
            List<School> schoolList = ParsingUtils
                .getSchoolListFromJsonFile(fileLocation);
            schoolRepository.saveAll(schoolList);
        }
    }
}
