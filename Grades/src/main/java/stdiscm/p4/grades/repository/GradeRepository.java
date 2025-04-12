package stdiscm.p4.grades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stdiscm.p4.grades.model.Grade;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

    @Query(value = "SELECT\n" +
            "c.code AS `Course Code`,\n" +
            "c.name AS `Course Name`,\n" +
            "g.grade AS `Grade`,\n" +
            "c.units AS `Unit(s)`\n" +
            "FROM grades AS g\n" +
            "JOIN enrollments AS e\n" +
            "ON g.enrollment_id=e.id\n" +
            "JOIN course_sections AS cs\n" +
            "ON e.section_id=cs.id\n" +
            "JOIN courses AS c\n" +
            "ON cs.course_id=c.id\n" +
            "WHERE e.student_id=:studentId",
    nativeQuery = true)
    List<Object[]> findGradesByStudentId(@Param("studentId") Integer studentId);
}
