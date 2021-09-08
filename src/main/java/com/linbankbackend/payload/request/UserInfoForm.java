package com.linbankbackend.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserInfoForm {

    @NotBlank(message = "SSN must be not null")
    private String ssn;

    @NotBlank
    @Size(min = 2, max= 30 ,message = "Please enter a valid firstname")
    private String firstName;

    @NotBlank
    @Size(min = 2, max=30 ,message = "Please enter a valid lastname")
    private String lastName;

    @NotBlank
    @Size(min = 7, max = 50, message = "Address info least at 7 - max 50 ")
    private String address;

    @NotBlank
    @Size(min = 12, max = 12, message = "Please enter a valid phone number")
    private String mobilePhoneNumber;

    @NotBlank
    @Email(message = "Please enter a valid email")
    private String email;

}
