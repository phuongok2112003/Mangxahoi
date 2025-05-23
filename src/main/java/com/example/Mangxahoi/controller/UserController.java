package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.LoginRequest;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequest;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.services.AuthService;
import com.example.Mangxahoi.services.UserService;
import com.example.Mangxahoi.utils.EOResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    @PostMapping("/login")
    public EOResponse<Otp> login(@Valid @RequestBody LoginRequest loginRequest)  {
        return EOResponse.build(authService.login(loginRequest));
    }
    @PostMapping("/register")
    public  EOResponse<String>  register(@Valid @RequestBody UserRequest userDto) {
        return  EOResponse.build(userService.register(userDto));
    }

    @DeleteMapping("/{id}")
    public EOResponse<String> delete(@PathVariable long id) {
        return  EOResponse.build(userService.delete(id));
    }


    @PutMapping("/{id}")
    public  EOResponse<UserResponseDto>  update(@PathVariable long id,@RequestBody @NonNull UserRequest userDto) {

        return  EOResponse.build(userService.update(id,userDto));
    }
    @PutMapping("/active/{id}")
    public  EOResponse<String>  turnOnOffActive(@PathVariable long id) {

        return  EOResponse.build(userService.turnOnOffSatus(id));
    }

    @PostMapping("/get-token")
    public EOResponse<TokenDto> getToken(@RequestBody Otp otp){
        return EOResponse.build(userService.getToken(otp));
    }
    @GetMapping("")
    public EOResponse<UserResponseDto> getUser() {
        return  EOResponse.build(userService.getUser());
    }

    @PostMapping("/forgot")
    public EOResponse<EmailResponse> forgotPassword(@RequestParam String email) {
        return EOResponse.build(userService.sendPasswordResetCode(email));
    }
    @PostMapping("/reset-password")
    public EOResponse<String> forgotPassword(@RequestParam("token") String token,@RequestBody PasswordResetRequest request) {
        return   EOResponse.build(userService.verifyPasswordResetCode(token,request));
    }
    @GetMapping("/reset-password")
    public EOResponse<String> getForgotPassword(@RequestParam("token") String token) {
        return   EOResponse.build(token);
    }

    @GetMapping("/{id}")
    public  EOResponse<UserResponseDto>  update(@PathVariable long id) {

        return  EOResponse.build(userService.getUser(id));
    }


}
