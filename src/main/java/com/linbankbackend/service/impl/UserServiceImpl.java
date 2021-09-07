package com.linbankbackend.service.impl;

import com.linbankbackend.dao.UserDAO;
import com.linbankbackend.model.ERoles;
import com.linbankbackend.model.User;
import com.linbankbackend.payload.request.RegisterForm;
import com.linbankbackend.payload.response.ResponseMessage;
import com.linbankbackend.repository.UserRepository;
import com.linbankbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService, UserDetailsService, ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ResponseMessage> inValidMessage(RegisterForm registerForm) {
        ResponseMessage response = new ResponseMessage();
        if (isExistEmail(registerForm.getEmail()) && isExistSSN(registerForm.getSsn())) {
            response.setMessage("Email already taken and SSN number registered another customer.");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (isExistEmail(registerForm.getEmail())) {
            response.setMessage("Email already taken.");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (isExistSSN(registerForm.getSsn())) {
            response.setMessage("SSN number registered another customer.");
            response.setIsSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return null;
        }


    }

    @Override
    public Boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean isExistSSN(String ssn) {
        return userRepository.existsUserBySsn(ssn);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDAO getUserDAO(User user) {
        UserDAO userDAO = new UserDAO();
        userDAO.setId(user.getId());
        userDAO.setSsn(user.getSsn());
        userDAO.setFirstname(user.getFirstname());
        userDAO.setLastname(user.getLastname());
        userDAO.setAddress(user.getAddress());
        userDAO.setMobilePhoneNumber(user.getMobilePhoneNumber());
        userDAO.setEmail(user.getEmail());
        userDAO.setPassword(user.getPassword());
        userDAO.setCreatedDate(user.getCreatedDate());
        userDAO.setLastLoggedIn(user.getLastLoggedIn());
        userDAO.setRole(user.getRole().getName());
        return userDAO;
    }

    @Override
    public UserDAO getUserDAOBySSN(String ssn) {
        User user = userRepository.findBySsn(ssn).orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });
//        UserDAO userDAO = null;
//        if (user.isPresent()) {
//            userDAO = getUserDAO(user.get());
//        }
        return getUserDAO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String ssn) throws UsernameNotFoundException {
        User user = userRepository.findBySsn(ssn).orElseThrow(
                () -> new UsernameNotFoundException("Error : Username not found " + ssn)
        );

        return user;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
        User user = (User) loadUserByUsername(userName);
        user.setLastLoggedIn(LocalDateTime.now());
    }
}
