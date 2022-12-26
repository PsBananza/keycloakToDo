package com.example.demo.controller;

import com.example.demo.custom.CustomSuccessResponse;
import com.example.demo.custom.RegisterUser;
import com.example.demo.custom.User;
import com.example.demo.dto.BaseSuccessResponse;
import com.example.demo.dto.ChangeStatusTodoDto;
import com.example.demo.dto.CreateToDoDto;
import com.example.demo.dto.UserLoginDto;
import com.example.demo.entity.ToDoEntity;
import com.example.demo.service.AuthServiceImpl;
import com.example.demo.service.KeycloakAdminClientExample;
import com.example.demo.service.KeycloakChangeRoleService;
import com.example.demo.service.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.client.registration.Auth;
import org.keycloak.client.registration.ClientRegistration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.account.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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

//    @GetMapping("/login/1")
//    public String testController(@RequestBody User user) {
//        return new AuthServiceImpl().getAccessToken(user);
//    }

    @SneakyThrows
    @PostMapping("/create/1")
    public String create1() throws JsonProcessingException {
        new KeycloakChangeRoleService().changeRole();
        return "ok";
    }

    @SneakyThrows
    @PostMapping("/create")
    public String create(@RequestBody RegisterUser user) throws JsonProcessingException {
        new KeycloakAdminClientExample().created(user);

        return "ok";
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
