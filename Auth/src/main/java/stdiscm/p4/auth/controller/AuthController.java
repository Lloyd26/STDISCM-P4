package stdiscm.p4.auth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stdiscm.p4.auth.model.JwtResponse;
import stdiscm.p4.auth.model.LoginRequest;
import stdiscm.p4.auth.repository.FacultyRepository;
import stdiscm.p4.auth.repository.StudentRepository;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    @PostMapping("/login")
    public ResponseEntity<?> postLogin(@RequestBody LoginRequest loginRequest) {
        String idNumber = loginRequest.getIdNumber();
        String password = loginRequest.getPassword();

        if (idNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("ID number is empty.");
        }

        if (password.isEmpty()) {
            return ResponseEntity.badRequest().body("Password is empty.");
        }

        if (studentRepository.findByIdNumberAndPassword(Integer.valueOf(idNumber), password) != null) {
            String jwtToken = generateJwtToken(idNumber);
            return ResponseEntity.ok(new JwtResponse(jwtToken));
        }

        return ResponseEntity.badRequest().body("Incorrect ID number or password.");
    }

    @PostMapping("/login/faculty")
    public ResponseEntity<?> postLoginFaculty(@RequestBody LoginRequest loginRequest) {
        String idNumber = loginRequest.getIdNumber();
        String password = loginRequest.getPassword();

        if (idNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("ID number is empty.");
        }

        if (password.isEmpty()) {
            return ResponseEntity.badRequest().body("Password is empty.");
        }

        if (facultyRepository.findByIdNumberAndPassword(Integer.valueOf(idNumber), password) != null) {
            String jwtToken = generateJwtToken(idNumber);
            return ResponseEntity.ok(new JwtResponse(jwtToken));
        }

        return ResponseEntity.badRequest().body("Incorrect ID number or password.");
    }

    private String generateJwtToken(String idNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(idNumber)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
