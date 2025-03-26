package com.inc.dao;

import com.inc.POJO.User;
import com.inc.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(@Param("email") String email);
    List<UserWrapper> getAllUser();
    List<String> getAllAdmin();
    @Transactional
    @Modifying
    void updateStatus(@Param("status") String status, @Param("id") Long id);
}
