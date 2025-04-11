package stdiscm.p4.course.model;

public class DropRequest {
    private Integer studentId;
    private Integer sectionId;

    public DropRequest(Integer studentId, Integer sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
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
