package com.example.demo.service;


import java.util.List;

import com.example.demo.dto.BaseSuccessResponse;
import com.example.demo.dto.ChangeStatusTodoDto;
import com.example.demo.dto.CreateToDoDto;
import com.example.demo.dto.GetNewsDto;
import com.example.demo.entity.ToDoEntity;
import com.example.demo.exception.LengthLimit;
import com.example.demo.repository.Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Repo repo;

    public ToDoEntity create(CreateToDoDto toDo) {
        ToDoEntity toDoEntity = new ToDoEntity();
        try {
            toDoEntity.setText(toDo.getText());
        }
        catch (LengthLimit e) {
            throw new RuntimeException(e);
        }
        repo.save(toDoEntity);
        return toDoEntity;
    }

    public BaseSuccessResponse deleteAllReady() {

        List<ToDoEntity> list = repo.findByStatus(true);
        for (ToDoEntity entity: list) {
            repo.delete(entity);
        }
        return new BaseSuccessResponse();
    }

    public BaseSuccessResponse deleteById(Long id) {
        repo.deleteById(id);

        return new BaseSuccessResponse();
    }

    public BaseSuccessResponse changeStatusTodoDto(ChangeStatusTodoDto change) {

        List<ToDoEntity> list = repo.findByStatus(!change.isStatus());
        for (ToDoEntity entity: list) {
            entity.setStatus(change.isStatus());
            repo.save(entity);
        }

        return new BaseSuccessResponse();
     }

     public BaseSuccessResponse changeCurrentStatus(Long id,
                                                     ChangeStatusTodoDto change) {
         ToDoEntity toDoEntity = repo.findById(id).orElseThrow();
         toDoEntity.setStatus(change.isStatus());
         repo.save(toDoEntity);

        return  new BaseSuccessResponse();
     }

    public BaseSuccessResponse changeCurrentText(Long id,
                                                 CreateToDoDto change) {
        ToDoEntity toDoEntity = repo.findById(id).orElseThrow();
        try {
            toDoEntity.setText(change.getText());
        }
        catch (LengthLimit e) {
            throw new RuntimeException(e);
        }
        repo.save(toDoEntity);

        return  new BaseSuccessResponse();
    }

    public GetNewsDto<ToDoEntity> getPaginated(int page, int perPage) {
        Pageable paging = PageRequest.of(page - 1, perPage, Sort.by("createdAt").descending());
        Page<ToDoEntity> pagedResult = repo.findAll(paging);
        GetNewsDto<ToDoEntity> content = new GetNewsDto<>();
        content.setContent(pagedResult.toList())
                .setNotReady(repo.countByStatus(false))
                .setNumberOfElements((int) repo.count())
                .setReady(repo.countByStatus(true));
        return content;
    }


    public GetNewsDto<ToDoEntity> getPaginated(int page, int perPage, Boolean status) {
        Pageable paging = PageRequest.of(page - 1, perPage, Sort.by("createdAt").descending());
        Page<ToDoEntity> pages = repo.findByStatus(paging, status);

        GetNewsDto<ToDoEntity> content = new GetNewsDto<>();
        content.setContent(pages.toList())
                .setNotReady(repo.countByStatus(false))
                .setNumberOfElements((int) repo.count())
                .setReady(repo.countByStatus(true));
        return content;
    }
}
