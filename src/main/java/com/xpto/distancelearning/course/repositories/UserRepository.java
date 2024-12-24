package com.xpto.distancelearning.course.repositories;

import com.xpto.distancelearning.course.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

//public interface UserRepository extends JpaRepository<UserModel, UUID> { // Original interface
public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {



//======================================================================================================================
//    NOTE: Fields deleted as the communication is now asynchronous
//======================================================================================================================

//    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);
//
//    @Query(value="select * from tb_courses_users where course_id = :courseId", nativeQuery = true)
//    List<UserModel> findAllCourseUserIntoCourse(@Param("courseId") UUID courseId);
//
//    boolean existsByUserId(UUID userId);
//
//    void deleteAllByUserId(UUID userId);
}
