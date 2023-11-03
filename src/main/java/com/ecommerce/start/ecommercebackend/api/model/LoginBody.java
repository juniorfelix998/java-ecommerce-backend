package com.ecommerce.start.ecommercebackend.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginBody {
    @NotBlank
    @NotNull
    private  String username;
    @NotBlank
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
