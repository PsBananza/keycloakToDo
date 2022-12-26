package com.example.demo.controller;

import com.example.demo.custom.CustomSuccessResponse;
import com.example.demo.custom.RegisterUser;
import com.example.demo.custom.User;
import com.example.demo.dto.BaseSuccessResponse;
import com.example.demo.dto.ChangeStatusTodoDto;
import com.example.demo.dto.CreateToDoDto;
import com.example.demo.dto.UserLoginDto;
import com.example.demo.entity.ToDoEntity;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@CrossOrigin
@RequestMapping("/v1/todo")
@RestController
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private Service service;

    @GetMapping("/login")
    public UserLoginDto login(@RequestBody User user) {
        return new AuthServiceImpl().getAccessToken(user);
    }

    @GetMapping("/rest")
    public String rest(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader("Authorization");
        String token = "";
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
        }
        return new Auth().getUserToken(token);
    }


    @SneakyThrows
    @PutMapping("/change/role")
    @PreAuthorize(value = "hasRole('ROLE_admin')")
    public String create1(@RequestParam UUID userUuid) throws JsonProcessingException {
        new KeycloakChangeRoleService().changeRole(userUuid);
        return "ok";
    }

    @SneakyThrows
    @PostMapping("/create")
    public String create(@RequestBody RegisterUser user) throws JsonProcessingException {
        return new KeycloakAdminClientExample().created(user);
    }

    @GetMapping("/1")
    @PreAuthorize(value = "hasRole('user')")
    public ResponseEntity<ToDoEntity> getPaginated(@RequestParam int page, @RequestParam int perPage, @RequestParam(required = false) Boolean status) {
        if (status != null) {
            return new ResponseEntity(new CustomSuccessResponse(service.getPaginated(page, perPage, status)), HttpStatus.OK);
        } else {
            return new ResponseEntity(new CustomSuccessResponse(service.getPaginated(page, perPage)), HttpStatus.OK);
        }

    }

    @PostMapping
    @PreAuthorize(value = "hasRole('USER')")
    public ResponseEntity registration(@RequestBody CreateToDoDto toDoDto) {
        CustomSuccessResponse customSuccessResponse = new CustomSuccessResponse(service.create(toDoDto));
        return new ResponseEntity<>(customSuccessResponse, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize(value = "hasRole('defaul1-master')")
    public BaseSuccessResponse deleteAllReady() {
        service.deleteAllReady();
        return new BaseSuccessResponse();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('USER')")
    public BaseSuccessResponse deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return new BaseSuccessResponse();
    }

    @PatchMapping
    @PreAuthorize(value = "hasRole('USER')")
    public BaseSuccessResponse changeStatusTodoDto(@RequestBody ChangeStatusTodoDto change) {
        service.changeStatusTodoDto(change);
        return new BaseSuccessResponse();
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize(value = "hasRole('USER')")
    public BaseSuccessResponse changeCurrentStatus(@PathVariable Long id, @RequestBody ChangeStatusTodoDto change) {
        service.changeCurrentStatus(id, change);
        return new BaseSuccessResponse();
    }

    @PatchMapping("/text/{id}")
    @PreAuthorize(value = "hasRole('USER')")
    public BaseSuccessResponse changeCurrentStatus(@PathVariable Long id, @RequestBody CreateToDoDto text) {
        service.changeCurrentText(id, text);
        return new BaseSuccessResponse();
    }
//


}
