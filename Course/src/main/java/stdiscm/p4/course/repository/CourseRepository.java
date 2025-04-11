package stdiscm.p4.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stdiscm.p4.course.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
