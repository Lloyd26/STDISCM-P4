package stdiscm.p4.view.controller;

import com.google.gson.Gson;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import stdiscm.p4.view.model.LoginRequest;

import java.util.List;

@Controller
@RequestMapping("/faculty")
public class ViewFacultyController {

    @Value("${nodes.auth.address}")
    private String authAddress;

    @Value("${nodes.course.address}")
    private String courseServiceAddress;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @GetMapping({"", "/"})
    public String getFaculty(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("faculty_jwtToken");
        if (jwtToken == null) return "login_faculty";

        String facultyId = (String) session.getAttribute("faculty_id_number");
        model.addAttribute("facultyId", facultyId);

        String facultyClassesUrl = "http://" + courseServiceAddress + "/api/courses-faculty/classes?facultyId=" + facultyId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                    facultyClassesUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Object[]>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Object[]> classes = response.getBody();
                model.addAttribute("classes", classes);
            } else {
                model.addAttribute("error", "Failed to fetch classes for faculty " + facultyId + ". Status: " + response.getStatusCode());
            }
            return "home_faculty";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error: " + e.getResponseBodyAsString());
            return "home_faculty";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the Course node.");
            return "home_faculty";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong while trying to fetch classes.");
            return "home_faculty";
        }
    }

    @PostMapping({"", "/"})
    public String postFaculty(@RequestParam("id_number") String idNumber, @RequestParam("password") String password, Model model, HttpSession session) {
        String loginUrl = "http://" + authAddress + "/api/auth/login/faculty";

        LoginRequest loginRequest = new LoginRequest(idNumber, password);
        String requestBody = gson.toJson(loginRequest);

        HttpHeaders headers= new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);

            String jwtToken = response.getBody();
            session.setAttribute("faculty_jwtToken", jwtToken);
            session.setAttribute("faculty_id_number", idNumber);

            return "redirect:/faculty";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error: " + e.getResponseBodyAsString());
            return "login_faculty";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the authentication server.");
            return "login_faculty";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Login failed.");
            return "login_faculty";
        }
    }
}
