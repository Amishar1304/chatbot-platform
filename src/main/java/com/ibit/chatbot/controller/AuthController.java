package com.ibit.chatbot.controller;

import com.ibit.chatbot.dto.LoginRequest;
import com.ibit.chatbot.dto.RegisterRequest;
import com.ibit.chatbot.entity.User;
import com.ibit.chatbot.repository.UserRepository;
import com.ibit.chatbot.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

		// check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email already registered");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setName(request.getName());

		// encrypt password
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(user);

		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElse(null);

		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return ResponseEntity.status(401).body("Invalid email or password");
		}

		// ✅ Use instance method
		String token = jwtService.generateToken(user.getEmail());

		return ResponseEntity.ok(token);
	}

}
