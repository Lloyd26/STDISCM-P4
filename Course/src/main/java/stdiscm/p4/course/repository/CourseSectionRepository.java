package stdiscm.p4.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stdiscm.p4.course.model.CourseSection;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {

    @Query(value = "SELECT " +
            "cs.id AS `Class Nbr`, " +
            "c.code AS `Course`, " +
            "cs.section AS `Section`, " +
            "cs.enrollment_cap AS `Enrl Cap`, " +
            "(SELECT COUNT(*) FROM enrollments WHERE section_id = cs.id) AS `Enrolled`, " +
            "CONCAT(f.first_name, ' ', f.last_name) AS `Faculty` " +
            "FROM course_sections cs " +
            "JOIN courses c ON cs.course_id = c.id " +
            "JOIN faculty f ON cs.faculty_id = f.id_number " +
            "WHERE c.code = :courseCode " +
            "ORDER BY cs.section ASC",
            nativeQuery = true)
    List<Object[]> findAllSectionsByCourse(@Param("courseCode") String courseCode);

    @Query(value = "SELECT c.code, cs.section\n" +
            "FROM course_sections cs\n" +
            "JOIN faculty f\n" +
            "ON cs.faculty_id=f.id_number\n" +
            "JOIN courses c\n" +
            "ON cs.course_id=c.id\n" +
            "WHERE f.id_number=:facultyId",
    nativeQuery = true)
    List<Object[]> findFacultyClassesByFacultyId(@Param("facultyId") String facultyId);
}
