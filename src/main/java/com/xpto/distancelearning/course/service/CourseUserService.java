package com.xpto.distancelearning.course.service;

import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.models.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);

    CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel);

    boolean existsByUserId(UUID userId);

    void deleteCourseUserByUser(UUID userId);
}
