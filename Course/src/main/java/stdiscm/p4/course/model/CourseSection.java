package stdiscm.p4.course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course_sections")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(length = 5)
    private String section;

    @Column(name = "faculty_id")
    private Integer facultyId;

    @Column(name = "enrollment_cap")
    private Integer enrollmentCap = 45;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Course course;

    public CourseSection() {}

    public CourseSection(Integer id, Integer courseId, String section, Integer facultyId, Integer enrollmentCap, Course course) {
        this.id = id;
        this.courseId = courseId;
        this.section = section;
        this.facultyId = facultyId;
        this.enrollmentCap = enrollmentCap;
        this.course = course;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(Integer enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
