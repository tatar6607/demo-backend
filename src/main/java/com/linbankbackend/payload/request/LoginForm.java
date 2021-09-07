package com.linbankbackend.payload.request;

import lombok.Data;

@Data
public class LoginForm {
    private String ssn;
    private String password;
}
