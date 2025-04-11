package stdiscm.p4.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stdiscm.p4.auth.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByIdNumberAndPassword(Integer idNumber, String password);

}
