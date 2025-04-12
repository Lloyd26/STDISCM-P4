package stdiscm.p4.grades.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.grades.repository.GradeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping
    public @ResponseBody List<Object[]> getGradesByStudentId(@RequestParam(name = "studentId") Integer studentId) {
        return gradeRepository.findGradesByStudentId(studentId);
    }
}
