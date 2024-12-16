package com.xpto.distancelearning.course.dtos;

import com.xpto.distancelearning.course.enums.UserStatus;
import com.xpto.distancelearning.course.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private UserStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
}