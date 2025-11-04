package com.univercity.unlimited.greenUniverCity.controller;

import com.univercity.unlimited.greenUniverCity.dto.UserDTO;
import com.univercity.unlimited.greenUniverCity.entity.UserRole;
import com.univercity.unlimited.greenUniverCity.entity.UserVo;
import com.univercity.unlimited.greenUniverCity.repository.UserRepository;
import com.univercity.unlimited.greenUniverCity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
//    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping("/all")
    public List<UserVo> postmanTestUser(){
        log.info("Controller: /api/user/all 호출");
        return userService.findAllUsers();
    }
    @GetMapping("/role/{roleName}")
    public List<UserVo> postmanTestRole(@PathVariable("roleName") UserRole role){
        log.info("Controller: /api/user/role/{} 호출", role);
        return userService.findUsersByRole(role);
    }
    @PostMapping("/login")
    private UserDTO login(@RequestBody UserDTO userDTO){
        log.info("Controller:/api/user/login:{} 호출",userDTO);
        return userService.login(userDTO);
    }

}
