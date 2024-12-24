package com.xpto.distancelearning.course.controllers;

import com.xpto.distancelearning.course.dtos.SubscriptionDto;
import com.xpto.distancelearning.course.enums.UserStatus;
import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.models.UserModel;
import com.xpto.distancelearning.course.service.CourseService;
import com.xpto.distancelearning.course.service.UserService;
import com.xpto.distancelearning.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

//    @Autowired
//    private AuthUserClient authUserClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/courses/{courseId}/users")
    //public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "courseId") UUID courseId) {
    public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec,
                                                      @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        //return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        if(courseService.existsByCourseAndUser(courseId, subscriptionDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        Optional<UserModel> userModelOptional = userService.findById(subscriptionDto.getUserId());
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if(userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED.toString())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
        }

        courseService.saveSubscriptionUserInCourse(courseModelOptional.get().getCourseId(), userModelOptional.get().getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");


        // NOTE: The code below is commented because the communication is now asynchronous
//        if(userService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserId())){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
//        }
//        try {
//            responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
//            if(responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
//            }
//        } catch (HttpStatusCodeException e) {
//            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//            }
//        }
//        UserModel userModel = userService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }



//======================================================================================================================
//    NOTE: Fields deleted as the communication is now asynchronous
//======================================================================================================================

//    @DeleteMapping("/courses/users/{userId}")
//    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId){
//        if(!userService.existsByUserId(userId)){
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found.");
//        }
//        userService.deleteCourseUserByUser(userId);
//        return  ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully.");
//    }
}
