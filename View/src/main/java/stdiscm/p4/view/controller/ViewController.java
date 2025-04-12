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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import stdiscm.p4.view.model.LoginRequest;

import java.util.List;

@Controller
public class ViewController {

    @Value("${nodes.auth.address}")
    private String authAddress;

    @Value("${nodes.grades.address}")
    private String gradesAddress;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @GetMapping("/")
    public String getHome(Model model, HttpSession session) {
        if (session.getAttribute("jwtToken") != null) {
            model.addAttribute("id_number", session.getAttribute("id_number"));
            return "home";
        }

        return "login";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(name = "id_number") String idNumber, @RequestParam String password, Model model, HttpSession session) {
        String loginUrl = "http://" + authAddress + "/api/auth/login";

        LoginRequest loginRequest = new LoginRequest(idNumber, password);
        String requestBody = gson.toJson(loginRequest);

        HttpHeaders headers= new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);

            String jwtToken = response.getBody();
            session.setAttribute("jwtToken", jwtToken);
            session.setAttribute("id_number", idNumber);

            return "redirect:/";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error: " + e.getResponseBodyAsString());
            return "login";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the authentication server.");
            return "login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Login failed.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String getLogout(@RequestParam(name = "next", required = false) String next, HttpSession session) {
        session.invalidate();
        return "redirect:" + (next != null ? next : "/");
    }

    @GetMapping("/grades")
    public String getGrades(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        String studentId = (String) session.getAttribute("id_number");

        if (jwtToken == null || studentId == null) return "redirect:/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            String gradesApiUrl = "http://" + gradesAddress + "/api/grades?studentId=" + studentId;

            ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                    gradesApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Object[]>>() {}
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                model.addAttribute("error", "Failed to fetch your enrollments.");
                return "grades";
            }

            model.addAttribute("grades", response.getBody());
            return "grades";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("error", "Error fetching data: " + e.getResponseBodyAsString());
            return "grades";
        } catch (ResourceAccessException e) {
            model.addAttribute("error", "Unable to connect to the Grades node.");
            return "grades";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong while trying to fetch your grades.");
            return "grades";
        }
    }

    @GetMapping("/faculty")
    public String getFaculty(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("faculty_jwtToken");
        if (jwtToken == null) return "login_faculty";
        return "home_faculty";
    }

    @PostMapping("/faculty")
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