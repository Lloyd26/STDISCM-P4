package stdiscm.p4.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Key;

@SpringBootApplication
public class AuthNode {

	public static void main(String[] args) {
		SpringApplication.run(AuthNode.class, args);

		// To generate a secret key for JWT:
		/*
		Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		String secretString = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println("Generated Secret Key (Base64): " + secretString);
		*/
	}

}
