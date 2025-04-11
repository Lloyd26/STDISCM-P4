package stdiscm.p4.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stdiscm.p4.course.model.Enrollment;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query(value = "SELECT\n" +
            "cs.id AS courseId,\n" +
            "cs.section AS section,\n" +
            "c.code AS courseCode,\n" +
            "c.name AS courseName,\n" +
            "CONCAT(f.first_name, ' ', f.last_name) AS 'faculty'\n" +
            "FROM enrollments e\n" +
            "JOIN course_sections cs\n" +
            "ON e.section_id=cs.id\n" +
            "JOIN courses c\n" +
            "ON cs.course_id=c.id\n" +
            "JOIN faculty f\n" +
            "ON cs.faculty_id=f.id_number\n" +
            "WHERE e.student_id=:studentId",
    nativeQuery = true)
    List<Object[]> findClassesByStudentId(@Param("studentId") Integer studentId);

    Optional<Enrollment> findByStudentIdAndSectionId(Integer studentId, Integer sectionId);
}
