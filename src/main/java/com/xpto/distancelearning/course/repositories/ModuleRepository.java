package com.xpto.distancelearning.course.repositories;

import com.xpto.distancelearning.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    /**
     * Find a module by title and configure 'course' as EAGER.
     * Note that it will return the course as well, even though the attribute 'course' has been configured as LAZY.
     * Also note that it will not return the lessons as I have not set the 'lessons' attribute as EAGER.
     *
     * @param title The title of the module
     * @return ModuleModel
     */
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

    //@Query("SELECT m FROM ModuleModel m WHERE m.title LIKE %:title%")
    @Query(value = "select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from tb_modules where course_course_id = :courseId and module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
