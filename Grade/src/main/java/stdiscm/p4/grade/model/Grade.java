package main.java.stdiscm.p4.grade.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "section_id")
    private Integer sectionId;

    @Column(name = "grade")
    private Double grade;

    //constructors
    public Grade(){}
    public Grade(Integer id, Integer studentId, Integer sectionId, Double grade) {
        this.id = id;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.grade = grade;
    }

    //getters and setters
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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
