package com.egatetutor.backend.controller;

import javax.validation.Valid;
import com.egatetutor.backend.model.UserInfo;
import com.egatetutor.backend.model.responsemodel.JwtRequest;
import com.egatetutor.backend.model.responsemodel.JwtResponse;
import com.egatetutor.backend.model.responsemodel.UserResponse;
import com.egatetutor.backend.repository.UserRepository;
import com.egatetutor.backend.service.UserService;
import com.egatetutor.backend.util.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping("/register")
	public ResponseEntity createUser(@Valid @RequestBody UserInfo userdetails)
			throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserInfo userDto = modelMapper.map(userdetails, UserInfo.class);
		UserInfo createdUser = null;
		try {
			createdUser = userService.createUser(userDto);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Exception("Email ID already Registered"));
		}
		UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);
		return ResponseEntity.status(HttpStatus.OK).body(userResponse);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
		UserInfo userEntity = userRepository.findByEmailId(userDetails.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		JwtResponse jwtResponse = new JwtResponse(token, userEntity);
		return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
