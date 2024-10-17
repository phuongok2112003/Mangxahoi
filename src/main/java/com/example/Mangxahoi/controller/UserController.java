package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.dto.request.UserRequestDto;
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
}
