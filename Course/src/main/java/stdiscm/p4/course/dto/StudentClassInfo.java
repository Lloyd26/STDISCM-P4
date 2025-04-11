package stdiscm.p4.course.dto;

public class StudentClassInfo {
    private String section;
    private String courseCode;
    private String courseName;
    private String faculty;

    public StudentClassInfo() {}

    public StudentClassInfo(String section, String courseCode, String courseName, String faculty) {
        this.section = section;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.faculty = faculty;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
