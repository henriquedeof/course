package com.xpto.distancelearning.course.service;

import com.xpto.distancelearning.course.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel save(UserModel userModel);

    void delete(UUID userId);

    Optional<UserModel> findById(UUID userInstructor);


//======================================================================================================================
//    NOTE: Fields deleted as the communication is now asynchronous
//======================================================================================================================

//    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);
//
//    UserModel save(UserModel userModel);
//
//    UserModel saveAndSendSubscriptionUserInCourse(UserModel userModel);
//
//    boolean existsByUserId(UUID userId);
//
//    void deleteCourseUserByUser(UUID userId);
}
