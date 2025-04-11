package stdiscm.p4.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.course.model.DropRequest;
import stdiscm.p4.course.model.EnrollmentRequest;
import stdiscm.p4.course.repository.EnrollmentRepository;
import stdiscm.p4.course.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/classes")
    public @ResponseBody List<Object[]> getStudentClasses(@RequestParam(name = "id") Integer studentId) {
        return enrollmentRepository.findClassesByStudentId(studentId);
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> postEnroll(@RequestBody EnrollmentRequest enrollmentRequest) {
        Integer studentId = enrollmentRequest.getStudentId();
        Integer sectionId = enrollmentRequest.getSectionId();

        try {
            enrollmentService.enrollStudent(studentId, sectionId);
            return new ResponseEntity<>("Successfully enrolled", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error during enrollment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> deleteDrop(@RequestBody DropRequest dropRequest) {
        Integer studentId = dropRequest.getStudentId();
        Integer sectionId = dropRequest.getSectionId();

        try {
            enrollmentService.dropStudent(studentId, sectionId);
            return new ResponseEntity<>("Successfully dropped", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error during drop operation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
