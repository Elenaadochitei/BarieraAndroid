package com.example.myapplication;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    /*
    {
        "id": 5,
        "username": "ionut",
        "email": null,
        "roles": [
            "ROLE_ADMIN"
        ],
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpb251dCIsImlhdCI6MTYwNTAwNTQ3NSwiZXhwIjoxNjA1MDkxODc1fQ.eAnDPogRxjc8WXKo7aPU0rInGJfQwuPa6o6k_qyZv9bV3NMlNQMeCxGPdlKxAJtV9tmoDY2rVk_ATfwLEK_5Tw",
        "tokenType": "Bearer"
    }
     */
    private String id;
    private String username;
    private String email;
    private Set<ERole> roles;
    private String accessToken;
    private String tokenType;
}
