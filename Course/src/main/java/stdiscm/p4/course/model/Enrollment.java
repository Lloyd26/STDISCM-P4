package stdiscm.p4.course.model;

import jakarta.persistence.*;
import stdiscm.p4.auth.model.Student;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "section_id")
    private Integer sectionId;

    public Enrollment() {}

    public Enrollment(Integer id, Integer studentId, Integer sectionId, Student student, CourseSection courseSection) {
        this.id = id;
        this.studentId = studentId;
        this.sectionId = sectionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }
}
