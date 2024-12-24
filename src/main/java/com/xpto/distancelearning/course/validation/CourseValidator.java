package com.xpto.distancelearning.course.validation;

import com.xpto.distancelearning.course.dtos.CourseDto;
import com.xpto.distancelearning.course.enums.UserType;
import com.xpto.distancelearning.course.models.UserModel;
import com.xpto.distancelearning.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

/**
 * This is a custom validator.
 */
@Component
public class CourseValidator implements Validator { // I need to implement the method validate

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private UserService userService;

//    @Autowired
//    private AuthUserClient authUserClient;

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto, errors); // Validate all the fields/attributes annotations (e.g. @NotBlank, @NotNull, etc.) of the CourseDto.
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        Optional<UserModel> userModelOptional = userService.findById(userInstructor);
        if(!userModelOptional.isPresent()) {
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
        }
        if(userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())){
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
        }

//        ResponseEntity<UserDto> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
//            if(responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)){
//                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
//            }
//        } catch (HttpStatusCodeException e) {
//            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
//                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
//            }
//        }
    }
}
