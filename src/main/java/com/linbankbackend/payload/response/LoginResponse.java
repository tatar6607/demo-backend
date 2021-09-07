package com.linbankbackend.payload.response;

import com.linbankbackend.dao.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UserDAO user;
    private String jwt;
}
