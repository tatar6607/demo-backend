package com.linbankbackend.controller;

import com.linbankbackend.configuration.jwt.JwtUtils;
import com.linbankbackend.dao.UserDAO;
import com.linbankbackend.model.ERoles;
import com.linbankbackend.model.User;
import com.linbankbackend.model.Role;
import com.linbankbackend.payload.request.LoginForm;
import com.linbankbackend.payload.request.RegisterForm;
import com.linbankbackend.payload.request.Token;
import com.linbankbackend.payload.response.LoginResponse;
import com.linbankbackend.payload.response.ResponseMessage;
import com.linbankbackend.repository.RoleRepository;
import com.linbankbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"} , allowedHeaders = "*")
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(path = "/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterForm registerForm) {
        ResponseMessage response = new ResponseMessage();

        if (userService.isExistSSN(registerForm.getSsn())) {
            response.setMessage("SSN number registered another customer.");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userService.isExistEmail(registerForm.getEmail())) {
            response.setMessage("Email already taken.");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!registerForm.getPassword().trim().equals(registerForm.getSecondPassword().trim())) {
            response.setMessage("Passwords must match");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(registerForm.getPassword().trim());
        User user = new User(
                registerForm.getSsn(),
                registerForm.getFirstName(),
                registerForm.getLastName(),
                registerForm.getAddress(),
                registerForm.getMobilePhoneNumber(),
                registerForm.getEmail(),
                encodedPassword);


        Role role = roleRepository.findRoleByName(ERoles.ROLE_CUSTOMER).orElseThrow(
                () -> new RuntimeException("Error: Role not found " + ERoles.ROLE_CUSTOMER)
        );

        user.setRole(role);
        userService.saveUser(user);

        response.setMessage("User registered successfully");
        response.setIsSuccess(true);

//        return new ResponseEntity<>(response, HttpStatus.OK);
//        return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(userService.getUserDAO(user), HttpStatus.OK);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm form) {
        ResponseMessage response = new ResponseMessage();
        if(form.getPassword() == null) {
            response.setMessage("Password bos birakilamaz");
            response.setIsSuccess(false);
            return  new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }


        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(form.getSsn(), form.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);

        User user = (User) authentication.getPrincipal();

        UserDAO userDAO = userService.getUserDAO(user);

        return ResponseEntity.ok(new LoginResponse(userDAO,jwt));

    }

    @PostMapping(path = "/user")
    public ResponseEntity<?> getUserWithToken(@Valid @RequestBody Token token)  {

        ResponseMessage response = new ResponseMessage();
        try {
            if(jwtUtils.isTokenExpired(token.getToken()) || token.getToken() == null) {
                response.setMessage("Token expired or null");
                response.setIsSuccess(false);
                return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            String ssn  = jwtUtils.extractSSN(token.getToken());
            UserDAO user = userService.getUserDAOBySSN(ssn);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {

                response.setMessage("Token expired or null");
                response.setIsSuccess(false);
                return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }



    }

}
