package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.ToDoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repo extends JpaRepository<ToDoEntity, Long> {

    List<ToDoEntity> findByStatus(boolean status);
    Page<ToDoEntity> findByStatus(Pageable pageable, boolean status);
    int countByStatus(boolean status);

}
