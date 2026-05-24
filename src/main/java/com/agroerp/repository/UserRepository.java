package com.agroerp.repository;

import com.agroerp.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndDeletedFalse(String username);

    @EntityGraph(attributePaths = "company")
    Optional<User> findWithCompanyByUsernameAndDeletedFalse(String username);

    @Query("select u from User u join fetch u.company where u.username = :username and u.deleted = false")
    Optional<User> findActiveWithCompanyByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
