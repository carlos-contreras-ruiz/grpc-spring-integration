package com.githuh.carloscontrerasruiz.userservice.repository;

import com.githuh.carloscontrerasruiz.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
