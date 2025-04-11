package stdiscm.p4.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stdiscm.p4.auth.model.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
}
