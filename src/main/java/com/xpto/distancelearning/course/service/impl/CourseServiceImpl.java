package com.xpto.distancelearning.course.service.impl;

import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.models.LessonModel;
import com.xpto.distancelearning.course.models.ModuleModel;
import com.xpto.distancelearning.course.repositories.CourseRepository;
import com.xpto.distancelearning.course.repositories.LessonRepository;
import com.xpto.distancelearning.course.repositories.ModuleRepository;
import com.xpto.distancelearning.course.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

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
        courseRepository.delete(courseModel);
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
}
