package stdiscm.p4.view.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stdiscm.p4.course.model.Course;
import stdiscm.p4.view.model.DropRequest;
import stdiscm.p4.view.model.EnrollmentRequest;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class ViewCourseController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${nodes.course.address}")
    private String courseServiceAddress;

    @GetMapping({"", "/"})
    public String getCourses(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) return "redirect:/";

        String studentId = (String) session.getAttribute("id_number");

        String enrollmentClassesApiUrl = "http://" + courseServiceAddress + "/api/enrollment/classes?id=" + studentId;

        String coursesApiUrl = "http://" + courseServiceAddress + "/api/courses/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Course>> response = restTemplate.exchange(
                    coursesApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            ResponseEntity<List<Object[]>> studentClassesResponse = restTemplate.exchange(
                    enrollmentClassesApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Object[]>>() {}
            );

            if (studentClassesResponse.getStatusCode().is2xxSuccessful()) {
                List<Object[]> studentClasses = studentClassesResponse.getBody();
                model.addAttribute("student_classes", studentClasses);
            } else {
                model.addAttribute("error", "Failed to fetch classes for student " + studentId + ". Status: " + studentClassesResponse.getStatusCode());
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Course> courses = response.getBody();
                model.addAttribute("courses", courses);
            } else {
                model.addAttribute("error", "Failed to fetch courses. Status: " + response.getStatusCode());
            }
            return "courses";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error: " + e.getResponseBodyAsString());
            return "courses";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the Course node.");
            return "courses";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong while trying to fetch courses.");
            return "courses";
        }
    }

    @GetMapping("/{course}/sections")
    public String getCourseSections(@PathVariable String course, Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) return "redirect:/";

        model.addAttribute("course", course);

        String studentId = (String) session.getAttribute("id_number");

        String enrollmentClassesApiUrl = "http://" + courseServiceAddress + "/api/enrollment/classes?id=" + studentId;

        String courseSectionsApiUrl = "http://" + courseServiceAddress + "/api/courses/" + course + "/sections";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                    courseSectionsApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Object[]>>() {}
            );

            ResponseEntity<List<Object[]>> studentClassesResponse = restTemplate.exchange(
                    enrollmentClassesApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Object[]>>() {}
            );

            if (studentClassesResponse.getStatusCode().is2xxSuccessful()) {
                List<Object[]> studentClasses = studentClassesResponse.getBody();
                model.addAttribute("student_classes", studentClasses);
            } else {
                model.addAttribute("error", "Failed to fetch classes for student " + studentId + ". Status: " + studentClassesResponse.getStatusCode());
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Object[]> courseSections = response.getBody();
                model.addAttribute("course_sections", courseSections);
            } else {
                model.addAttribute("error", "Failed to fetch courses. Status: " + response.getStatusCode());
            }
            return "course_sections";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error:" + e.getResponseBodyAsString());
            return "course_sections";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the Course node.");
            return "course_sections";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong while trying to fetch sections for the " + course + " course.");
            return "course_sections";
        }
    }

    @PostMapping("/{course}/enroll")
    public String postEnroll(@RequestParam("sectionId") Integer sectionId, RedirectAttributes redirectAttributes, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        Integer studentId = Integer.valueOf((String) session.getAttribute("id_number"));

        if (jwtToken == null || studentId == null) return "redirect:/";

        String enrollApiUrl = "http://" + courseServiceAddress + "/api/enrollment/enroll";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest(studentId, sectionId);
        HttpEntity<EnrollmentRequest> requestEntity = new HttpEntity<>(enrollmentRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    enrollApiUrl,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                redirectAttributes.addFlashAttribute("message", "Successfully enlisted!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to enlist. Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Error enlisting: " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            redirectAttributes.addFlashAttribute("error", "Unable to connect to the Course node.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to enlist.");
        }

        return "redirect:/courses/";
    }

    @PostMapping("/drop")
    public String postDrop(@RequestParam("sectionId") Integer sectionId, Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        Integer studentId = Integer.valueOf((String) session.getAttribute("id_number"));

        if (jwtToken == null || studentId == null) return "redirect:/";

        String dropApiUrl = "http://" + courseServiceAddress + "/api/enrollment/drop";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<DropRequest> requestEntity = new HttpEntity<>(new DropRequest(studentId, sectionId), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    dropApiUrl,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("message", "Successfully dropped class with section ID: " + sectionId);
            } else {
                model.addAttribute("error", "Failed top drop class. Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error dropping class: " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the Course node.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong while trying to drop the class.");
        }

        return "redirect:/courses";
    }
}
