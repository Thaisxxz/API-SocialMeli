package com.social.meli.repository;

import com.social.meli.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired EntityManager em;
    @Autowired UserRepository userRepository;

    @Test
    void findByEmail_shouldReturnUserWhenExists() {
        var u = new User();
        u.setEmail("a@a.com");
        u.setName("Ana");
        u.setNickname("nick001");
        u.setPassword("123456");
        u.setPhone("11 99999-9999");
        em.persist(u);
        em.flush();
        em.clear();

        var found = userRepository.findByEmail("a@a.com");
        assertTrue(found.isPresent());
        assertEquals("nick001", found.get().getNickname());
    }
}