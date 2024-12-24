package com.xpto.distancelearning.course.service.impl;

import com.xpto.distancelearning.course.models.UserModel;
import com.xpto.distancelearning.course.repositories.CourseRepository;
import com.xpto.distancelearning.course.repositories.UserRepository;
import com.xpto.distancelearning.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        courseRepository.deleteCourseUserByUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserModel> findById(UUID userInstructor) {
        return userRepository.findById(userInstructor);
    }


//======================================================================================================================
//    NOTE: Fields deleted as the communication is now asynchronous
//======================================================================================================================

//    @Override
//    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
//        return userRepository.existsByCourseAndUserId(courseModel, userId);
//    }
//
//    @Override
//    public UserModel save(UserModel userModel) {
//        return userRepository.save(userModel);
//    }
//
//    @Transactional
//    @Override
//    public UserModel saveAndSendSubscriptionUserInCourse(UserModel userModel) {
//        userModel = userRepository.save(userModel);
//        authUserClient.postSubscriptionUserInCourse(userModel.getCourse().getCourseId(), userModel.getUserId());
//        return userModel;
//    }
//
//    @Override
//    public boolean existsByUserId(UUID userId) {
//        return userRepository.existsByUserId(userId);
//    }
//
//    @Transactional
//    @Override
//    public void deleteCourseUserByUser(UUID userId) {
//        userRepository.deleteAllByUserId(userId);
//    }
}
