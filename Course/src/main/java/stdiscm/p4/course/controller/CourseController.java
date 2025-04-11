package stdiscm.p4.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import stdiscm.p4.course.model.Course;
import stdiscm.p4.course.repository.CourseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping({"", "/"})
    public @ResponseBody List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
