package stdiscm.p4.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.course.repository.CourseSectionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/courses-faculty")
public class CourseFacultyController {

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @GetMapping("/classes")
    public @ResponseBody List<Object[]> getFacultyClasses(@RequestParam(name = "facultyId") String facultyId) {
        return courseSectionRepository.findFacultyClassesByFacultyId(facultyId);
    }
}
