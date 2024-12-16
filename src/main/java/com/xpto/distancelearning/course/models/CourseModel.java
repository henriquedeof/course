package com.xpto.distancelearning.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xpto.distancelearning.course.enums.CourseLevel;
import com.xpto.distancelearning.course.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // To ignore the null fields in the JSON response
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 250)
    private String description;

    @Column
    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // To save the Enum as a String in the DB
    private CourseStatus courseStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Column(nullable = false)
    private UUID userInstructor;

    /*
     * JsonProperty (uses Jackson for serialisation/deserialisation) to ignore the modules in the JSON response.
     * The 'modules' field will be ignored when the object is converted to JSON, but it will be populated when JSON is converted to the object.
     *
     *
     * CascadeType.ALL: All cascade operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH) will be applied to the related entities. When an operation is performed on the parent entity, the same operation will be cascaded to the child entities.
     * orphanRemoval = true: This means that if a child entity is removed from the collection of the parent entity, it will be automatically deleted from the database.
     *      This is useful for ensuring that there are no orphaned records in the database that are no longer associated with a parent entity.
     *      In my case, if there is a Module that is not linked to a Course, then the Module will be deleted.
     *
     *
     * @Fetch(FetchMode.SUBSELECT or SELECT) Instead of fetching the collection for each parent entity individually, Hibernate will fetch all collections in a single query using a subselect (improve performance).
     * @Fetch(FetchMode.JOIN) This way, the collection is fetched EAGERLY using a single join query, even though I previously defined FetchType.LAZY.
     *
     *
     * @OnDelete(action = OnDeleteAction.CASCADE): Delegate the deletion of the modules to the database. This way, the database will delete the modules when the course is deleted.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    //@OnDelete(action = OnDeleteAction.CASCADE) // Delegate the deletion of the modules to the database. This way, the database will delete the modules when the course is deleted.
    private Set<ModuleModel> modules;

    /*
     * JsonProperty.Access.WRITE_ONLY: The property will only be used during deserialization (reading JSON into Java object).
     * JsonProperty.Access.READ_ONLY: The property will only be used during serialization (writing Java object to JSON).
     * JsonProperty.Access.READ_WRITE: The property will be used for both serialization and deserialization (default behavior).
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<CourseUserModel> coursesUsers;

    public CourseUserModel convertToCourseUserModel(UUID userID){
        return new CourseUserModel(null, userID, this);
    }
}
