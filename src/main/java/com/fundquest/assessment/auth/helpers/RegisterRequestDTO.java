package com.fundquest.assessment.auth.helpers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
}
