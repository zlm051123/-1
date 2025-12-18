package com.energy.function.repository;

import com.energy.function.entity.SignIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignInRepository extends JpaRepository<SignIn, Long> {
    List<SignIn> findByGroupId(String groupId);
}
