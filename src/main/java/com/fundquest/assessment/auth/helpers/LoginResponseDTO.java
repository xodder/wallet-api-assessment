package com.fundquest.assessment.auth.helpers;

import com.fundquest.assessment.user.User;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoginResponseDTO {
    private User user;
    private String token;
}
