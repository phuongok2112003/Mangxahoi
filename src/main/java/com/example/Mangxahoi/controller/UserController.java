package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.Otp;
import com.example.Mangxahoi.dto.TokenDto;
import com.example.Mangxahoi.dto.request.PasswordResetRequest;
import com.example.Mangxahoi.dto.request.UserRequestDto;
import com.example.Mangxahoi.dto.response.EmailResponse;
import com.example.Mangxahoi.dto.response.UserResponseDto;
import com.example.Mangxahoi.services.UserService;
import com.example.Mangxahoi.utils.EOResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public  EOResponse<UserResponseDto>  save(@RequestBody UserRequestDto userDto) {
        return  EOResponse.build(userService.register(userDto));
    }

    @DeleteMapping("/{id}")
    public EOResponse<String> delete(@PathVariable long id) {
        return  EOResponse.build(userService.delete(id));
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/{id}")
    public  EOResponse<UserResponseDto>  update(@PathVariable long id,@RequestBody @NonNull UserRequestDto userDto) {

        return  EOResponse.build(userService.update(id,userDto));
    }

    @PostMapping("/get-token")
    public EOResponse<TokenDto> getToken(@RequestBody Otp otp){
        return EOResponse.build(userService.getToken(otp));
    }
    @PostMapping("/refresh-token")
    public TokenDto refreshToken(@RequestParam("token") String token) {
        return userService.refreshToken(token);
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

}
