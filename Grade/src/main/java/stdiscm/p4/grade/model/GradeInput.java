package main.java.stdiscm.p4.grade.model;

public class GradeInput {
    private Integer studentId;
    private Integer sectionId;
    private Double grade;

    public GradeInput() {}

    public GradeInput(Integer studentId, Integer sectionId, Double grade) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.grade = grade;
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