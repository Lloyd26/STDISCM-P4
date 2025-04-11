package stdiscm.p4.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.course.repository.EnrollmentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/classes")
    public @ResponseBody List<Object[]> getStudentClasses(@RequestParam(name = "id") Integer studentId) {
        return enrollmentRepository.findClassesByStudentId(studentId);
    }
}
