package main.java.stdiscm.p4.grade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.grade.model.Grade;
import stdiscm.p4.grade.model.GradeInput;
import stdiscm.p4.grade.repository.GradeRepository;
import stdiscm.p4.grade.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeService gradeService;

    // Get grades for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable Integer studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(grades);
    }

    // Get grades for a section
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Grade>> getGradesBySectionId(@PathVariable Integer sectionId) {
        List<Grade> grades = gradeRepository.findBySectionId(sectionId);
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(grades);
    }

    // Faculty uploads a grade.
    @PostMapping("/upload")
    public ResponseEntity<String> uploadGrade(@RequestBody GradeInput gradeInput) {
        try {
            gradeService.uploadGrade(gradeInput.getStudentId(), gradeInput.getSectionId(), gradeInput.getGrade());
            return ResponseEntity.status(HttpStatus.CREATED).body("Grade uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload grade.");
        }
    }
}
