package stdiscm.p4.course.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import stdiscm.p4.course.model.CourseSection;
import stdiscm.p4.course.model.Enrollment;
import stdiscm.p4.course.repository.CourseSectionRepository;
import stdiscm.p4.course.repository.EnrollmentRepository;

import java.security.Key;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${nodes.grades.address}")
    private String gradesAddress;

    public Integer getStudentIdFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String token = authorizationHeader.substring(7);
            try {
                Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
                return Integer.parseInt(Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public void enrollStudent(Integer studentId, Integer sectionId) {
        Optional<CourseSection> sectionOptional = courseSectionRepository.findById(sectionId);
        if (sectionOptional.isEmpty()) {
            throw new IllegalStateException("Invalid Section ID");
        }

        CourseSection section = sectionOptional.get();
        Integer courseId = section.getCourse().getCourseId();

        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentId);
        for (Enrollment existingEnrollment : existingEnrollments) {
            if (existingEnrollment.getCourseSection() != null &&
                    existingEnrollment.getCourseSection().getCourse() != null &&
                    existingEnrollment.getCourseSection().getCourse().getCourseId().equals(courseId)) {
                throw new IllegalStateException("Student is already enrolled in another section of this course");
            }
        }

        if (enrollmentRepository.findByStudentIdAndSectionId(studentId, sectionId).isPresent()) {
            throw new IllegalStateException("Student is already enrolled in this section");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setSectionId(sectionId);
        enrollmentRepository.save(enrollment);
    }

    public void dropStudent(Integer studentId, Integer sectionId) {
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findByStudentIdAndSectionId(studentId, sectionId);
        if (enrollmentOptional.isEmpty()) {
            throw new IllegalStateException("Enrollment record not found for student " + studentId + " and section " + sectionId);
        }

        Enrollment enrollment = enrollmentOptional.get();
        Integer enrollmentId = enrollment.getId();

        try {
            String deleteGradesUrl = "http://" + gradesAddress + "/api/grades?enrollmentId=" + enrollmentId;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(deleteGradesUrl, HttpMethod.DELETE, requestEntity, String.class);

            enrollmentRepository.delete(enrollmentOptional.get());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error deleting grades for enrollment " + enrollmentId + ": " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Unable to connect to the Grades node.");
        } catch (Exception e) {
            throw new RuntimeException("Error during drop operation (grades deletion).", e);
        }
    }
}
