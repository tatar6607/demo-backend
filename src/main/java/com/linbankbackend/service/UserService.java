package com.linbankbackend.service;

import com.linbankbackend.dao.UserDAO;
import com.linbankbackend.model.User;
import com.linbankbackend.payload.request.RegisterForm;
import com.linbankbackend.payload.response.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface UserService {
    Boolean isExistEmail(String email);
    Boolean isExistSSN(String ssn);
    void saveUser(User user);
    UserDAO getUserDAO(User user);
    UserDAO getUserDAOBySSN(String ssn);

}