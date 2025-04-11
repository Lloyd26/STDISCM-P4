package stdiscm.p4.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.course.model.Course;
import stdiscm.p4.course.repository.CourseRepository;
import stdiscm.p4.course.repository.CourseSectionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @GetMapping({"", "/"})
    public @ResponseBody List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{course}/sections")
    public @ResponseBody List<Object[]> getCourseSections(@PathVariable(name = "course") String courseCode) {
        return courseSectionRepository.findAllSectionsByCourse(courseCode);
    }
}
