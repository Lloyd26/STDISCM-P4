package stdiscm.p4.view.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import stdiscm.p4.course.model.Course;

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

        String coursesApiUrl = "http://" + courseServiceAddress + "/api/courses/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Course>> response = restTemplate.exchange(
                    coursesApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );

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
}
