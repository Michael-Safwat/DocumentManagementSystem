package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    public AppUser findByEmail(String email);
}
