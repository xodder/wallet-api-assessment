package com.fundquest.assessment.user.helpers;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateUserRequestDTO {
    private String name;
    private String email;
    private String password;
}
