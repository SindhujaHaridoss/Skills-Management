/*import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.skillsmanagement.Exception.CustomException;
import com.skillsmanagement.SkillsmanagementApplicationTests;
import com.skillsmanagement.repository.PersonRepository;
import com.skillsmanagement.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class SkillControllerTest extends SkillsmanagementApplicationTests {

    @Autowired
    private PersonRepository personRepository;

   
    @Autowired
    private SkillRepository skillRepository;

    @BeforeEach
    public void before() {
    	personRepository.deleteAll();
        skillRepository.deleteAll();
    }

    *//**
     * Employee repository tests
     *//*
    @Test
    public void when_FindByInvalidSkillId() {
        Assertions.assertThrows(CustomException.class,
                () -> skillRepository.findBySkillId(1L));
    }
}*/