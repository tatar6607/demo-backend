package com.linbankbackend.controller;

import com.linbankbackend.configuration.jwt.JwtUtils;
import com.linbankbackend.dao.UserDAO;
import com.linbankbackend.model.User;
import com.linbankbackend.payload.request.ChangePasswordForm;
import com.linbankbackend.payload.request.UserInfoForm;
import com.linbankbackend.payload.response.LoginResponse;
import com.linbankbackend.payload.response.ResponseMessage;
import com.linbankbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/userInfoUpdate")
    public ResponseEntity<?> userInfoUpdate(@Valid @RequestBody UserInfoForm userInfoForm, @RequestHeader(name = "Authorization") String token) {
        System.out.println("Token: =============" + token);
//        String ssn = userInfoForm.getSsn();
//        ResponseMessage response = new ResponseMessage();
//        if(ssn == null || (!ssn.equals(jwtUtils.extractSSN(token) || )) {
//            response.setIsSuccess(false);
//            response.setMessage("SSN already registered.");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }

//        String ssn = jwtUtils.extractSSN(token.substring(7));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("currentUser.getSsn() = " + currentUser.getSsn());
//        String ssn = userInfoForm.getSsn();
//        User user = userService.getUserBySSN(ssn);
//        if(!user.getSsn().equals(userInfoForm.getSsn())) {
//            user.setSsn(userInfoForm.getSsn());
//        }
//        if(!user.getFirstname().equals(userInfoForm.getFirstName())) {
//            user.setFirstname(userInfoForm.getFirstName());
//        }
//        if(!user.getLastname().equals(userInfoForm.getLastName())) {
//            user.setLastname(userInfoForm.getLastName());
//        }
//        if(!user.getAddress().equals(userInfoForm.getAddress())) {
//            user.setAddress(userInfoForm.getAddress());
//        }
//        if(!user.getMobilePhoneNumber().equals(userInfoForm.getMobilePhoneNumber())) {
//            user.setMobilePhoneNumber(userInfoForm.getMobilePhoneNumber());
//        }
//        if(!user.getEmail().equals(userInfoForm.getEmail())) {
//            user.setEmail(userInfoForm.getEmail());
//        }


        user.setSsn(userInfoForm.getSsn());
        user.setFirstname(userInfoForm.getFirstName());
        user.setLastname(userInfoForm.getLastName());
        user.setAddress(userInfoForm.getAddress());
        user.setMobilePhoneNumber(userInfoForm.getMobilePhoneNumber());
        user.setEmail(userInfoForm.getEmail());



        System.out.println("========== Kayit ==========");
        userService.saveUser(user);
        UserDAO userDAO = userService.getUserDAO(user);
        token = jwtUtils.generateToken(user);
        System.out.println("------" + token  +"------");
        return new ResponseEntity<>(new LoginResponse(userDAO, token), HttpStatus.OK);

    }


    @PutMapping(path = "/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm) {
        ResponseMessage response = new ResponseMessage();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentPassword = user.getPassword();
//        String encodedPassword = passwordEncoder.encode(changePasswordForm.getCurrentPassword());
//        System.out.println("encodedPassword = " + encodedPassword);
//        System.out.println("currentPassword = " + currentPassword);

        boolean isPasswordMatch = passwordEncoder.matches(changePasswordForm.getCurrentPassword(),currentPassword);
        if(!isPasswordMatch) {
            response.setIsSuccess(false);
            response.setMessage("Your current password wrong. Please check your current password.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
            response.setIsSuccess(false);
            response.setMessage("Your new password does not match with the confirm password.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String encodedNewPassword = passwordEncoder.encode(changePasswordForm.getNewPassword());
        user.setPassword(encodedNewPassword);
        userService.saveUser(user);

        response.setIsSuccess(true);
        response.setMessage("Your password changed successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
