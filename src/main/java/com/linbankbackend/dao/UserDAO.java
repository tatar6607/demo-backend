package com.linbankbackend.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linbankbackend.model.ERoles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {
    private Long id;
    private String ssn;
    private String firstname;
    private String lastname;
    private String address;
    private String mobilePhoneNumber;
    private String email;

    @JsonIgnore
    private String password;
    private Date createdDate;
    private LocalDateTime lastLoggedIn;
    private ERoles role;


}
