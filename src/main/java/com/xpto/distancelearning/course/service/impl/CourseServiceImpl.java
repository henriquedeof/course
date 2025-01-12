package com.xpto.distancelearning.course.service.impl;

import com.xpto.distancelearning.course.dtos.NotificationCommandDto;
import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.models.LessonModel;
import com.xpto.distancelearning.course.models.ModuleModel;
import com.xpto.distancelearning.course.models.UserModel;
import com.xpto.distancelearning.course.publishers.NotificationCommandPublisher;
import com.xpto.distancelearning.course.repositories.CourseRepository;
import com.xpto.distancelearning.course.repositories.LessonRepository;
import com.xpto.distancelearning.course.repositories.ModuleRepository;
import com.xpto.distancelearning.course.repositories.UserRepository;
import com.xpto.distancelearning.course.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationCommandPublisher notificationCommandPublisher;

//    @Autowired
//    private AuthUserClient authUserClient;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!CollectionUtils.isEmpty(moduleModelList)) {
            for (ModuleModel moduleModel : moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
                if (!CollectionUtils.isEmpty(lessonModelList)) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courseRepository.delete(courseModel);

//        NOTE: Code commented as the communication is now asynchronous
//        List<UserModel> userModelList = userRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
//        if(!userModelList.isEmpty()){
//            userRepository.deleteAll(userModelList);
//            deleteCourseUserInAuthUser = true;
//        }
//
//        if (deleteCourseUserInAuthUser) {
//            authUserClient.deleteCourseInAuthUser(courseModel.getCourseId());
//        }
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    //public Page<CourseModel> findAll(SpecificationTemplate.CourseSpec spec, Pageable pageable) {
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModel user) {
        courseRepository.saveCourseUser(course.getCourseId(), user.getUserId());

        // If notificationCommandPublisher.publishNotificationCommand(notificationCommandDto); fails, the user will be subscribed to the course anyway
        // because the method saveCourseUser(course.getCourseId(), user.getUserId()); is called before the notification is sent.
        try {
            var notificationCommandDto = new NotificationCommandDto();
            notificationCommandDto.setTitle("Welcome to the course: " + course.getName());
            notificationCommandDto.setMessage(user.getFullName() + " your subscription has been added successfully!");
            notificationCommandDto.setUserId(user.getUserId());
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);
        } catch (Exception e) {
            log.warn("Error sending notification!");
        }
    }
}
