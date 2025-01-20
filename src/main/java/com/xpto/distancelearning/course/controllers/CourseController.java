package com.xpto.distancelearning.course.controllers;

import com.xpto.distancelearning.course.dtos.CourseDto;
import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.service.CourseService;
import com.xpto.distancelearning.course.specifications.SpecificationTemplate;
import com.xpto.distancelearning.course.validation.CourseValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseValidator courseValidator;

    // NOTE 1: Because I am NOT using 'groups' in the CourseDto annotations (@NotBlank, @NotNull, etc.), I need to use the @Valid (NOT @Validated) annotation in the controller.
        // See UserDto that uses Validation Groups or 'groups'
    // NOTE 2: Using the CourseValidator for validations. All the field annotations of the CourseDto (e.g. @NotBlank, @NotNull, etc.) are validated in the CourseValidator class
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    // public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) { // This was the original line. See NOTE 1.
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto courseDto, Errors errors) { // This is the new line, where I removed the @Valid annotation to use the 'CourseValidator' custom validation. See NOTE 2.
        log.debug("POST saveCourse courseDto received {} ", courseDto.toString());
        courseValidator.validate(courseDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        courseService.delete(courseModelOptional.get());
        log.info("Course deleted successfully courseId {} ", courseId);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully"); // I should have used HttpStatus.NO_CONTENT here
    }

    // Because I am NOT using 'groups' in the CourseDto annotations (@NotBlank, @NotNull, etc.), I need to use the @Valid (NOT @Validated) annotation in the controller.
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        var courseModel = courseModelOptional.get();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }
    // Another way to implement the PUT method:
//    @PutMapping("/{courseId}")
//    public ResponseEntity<Object> updateCourse(@PathVariable(value="courseId") UUID courseId,
//                                               @RequestBody @Valid CourseDto courseDto){
//        log.debug("PUT updateCourse courseDto received {} ", courseDto.toString());
//        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
//        if(!courseModelOptional.isPresent()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
//        }
//        var courseModel = courseModelOptional.get();
//        courseModel.setName(courseDto.getName());
//        courseModel.setDescription(courseDto.getDescription());
//        courseModel.setImageUrl(courseDto.getImageUrl());
//        courseModel.setCourseStatus(courseDto.getCourseStatus());
//        courseModel.setCourseLevel(courseDto.getCourseLevel());
//        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
//        courseService.save(courseModel);
//        log.debug("PUT updateCourse courseId saved {} ", courseModel.getCourseId());
//        log.info("Course updated successfully courseId {} ", courseModel.getCourseId());
//        return ResponseEntity.status(HttpStatus.OK).body(courseModel);
//    }

//    @GetMapping
//    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
//        return ResponseEntity.ok(courseService.findAll(spec, pageable));
//    }
@PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId){

        //return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
        if(userId != null){
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
        }


        // NOTE: The code below is commented because the communication is now asynchronous
//        if(userId != null){
//            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable));
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
//        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.ok(courseModelOptional.get());
    }
}
