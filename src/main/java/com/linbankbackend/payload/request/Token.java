package com.linbankbackend.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Token {
    @NotBlank
    private String token;
}
