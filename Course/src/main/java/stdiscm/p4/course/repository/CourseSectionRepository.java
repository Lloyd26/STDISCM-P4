package stdiscm.p4.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stdiscm.p4.course.model.CourseSection;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {
    
    @Query(value = "SELECT\n" +
            "DISTINCT cs.id AS `ClassNbr`,\n" +
            "c.code AS `Course`,\n" +
            "cs.section AS `Section`,\n" +
            "CONCAT(f.first_name, ' ', f.last_name) AS `Faculty`,\n" +
            "cs.enrollment_cap AS `EnrlCap`,\n" +
            "(SELECT COUNT(*) FROM enrollments WHERE section_id=cs.id) AS `Enrolled`\n" +
            "FROM enrollments AS e\n" +
            "JOIN course_sections AS cs\n" +
            "ON e.section_id=cs.id\n" +
            "JOIN students AS s\n" +
            "ON e.student_id=s.id_number\n" +
            "JOIN faculty AS f\n" +
            "ON cs.faculty_id=f.id_number\n" +
            "JOIN courses AS c\n" +
            "ON cs.course_id=c.id\n" +
            "WHERE c.code=:courseCode\n" +
            "ORDER BY cs.section ASC",
    nativeQuery = true)
    List<Object[]> findAllSectionsByCourse(@Param("courseCode") String courseCode);
}
