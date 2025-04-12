package stdiscm.p4.grades.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "grade", nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeValue grade;

    @Column(name = "enrollment_id")
    private Integer enrollmentId;

    /*@ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;*/

    public Grade() {}

    public Grade(Integer id, GradeValue grade, Integer enrollmentId) {
        this.id = id;
        this.grade = grade;
        this.enrollmentId = enrollmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GradeValue getGrade() {
        return grade;
    }

    public void setGrade(GradeValue grade) {
        this.grade = grade;
    }

    /*public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }*/

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public enum GradeValue {
        _0_0("0.0"),
        _1_0("1.0"),
        _1_5("1.5"),
        _2_0("2.0"),
        _2_5("2.5"),
        _3_0("3.0"),
        _3_5("3.5"),
        _4_0("4.0");

        private final String value;

        GradeValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
