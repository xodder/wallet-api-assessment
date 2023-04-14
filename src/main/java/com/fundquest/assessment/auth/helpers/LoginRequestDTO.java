package com.fundquest.assessment.auth.helpers;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoginRequestDTO {
    private String email;
    private String password;
}
