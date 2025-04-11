package stdiscm.p4.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stdiscm.p4.course.model.CourseSection;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {
}
