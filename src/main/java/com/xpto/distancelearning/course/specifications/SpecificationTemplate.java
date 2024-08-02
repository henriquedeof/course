package com.xpto.distancelearning.course.specifications;


import com.xpto.distancelearning.course.models.CourseModel;
import com.xpto.distancelearning.course.models.LessonModel;
import com.xpto.distancelearning.course.models.ModuleModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    /**
     * Specification template for CourseModel.
     *
     * Converts the parameters (filters) in the URL to a Specification object.
     * For example, it can convert these parameters to Enums, Dates, BigDecimal, etc.
     *
     * @See ResolverConfig.
     */
    @And({
        @Spec(path = "courseLevel", spec = Equal.class), // Enum type (Use Equal for Enum type)
        @Spec(path = "courseStatus", spec = Equal.class), // Enum type (Use Equal for Enum type)
        @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(course.get("courseId"), courseId),
                    criteriaBuilder.isMember(module, coursesModules)
            );
        };
    }

    public static Specification<LessonModel> lessonModuleId(UUID moduleId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
            return criteriaBuilder.and(
                    criteriaBuilder.equal(module.get("moduleId"), moduleId),
                    criteriaBuilder.isMember(lesson, moduleLessons)
            );
        };
    }
}
