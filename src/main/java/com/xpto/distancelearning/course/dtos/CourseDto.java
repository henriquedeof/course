package com.xpto.distancelearning.course.dtos;

import com.xpto.distancelearning.course.enums.CourseLevel;
import com.xpto.distancelearning.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDto {

    // Because I am NOT using 'groups' (see UserDto) in the NotBlank validation, I needed to use the @Valid annotation in the controller.
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String imageUrl;

    @NotNull
    private CourseStatus courseStatus;

    @NotNull
    private UUID userInstructor;

    @NotNull
    private CourseLevel courseLevel;
}