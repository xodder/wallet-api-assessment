package com.fundquest.assessment.user.helpers;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UserCreateRequestDTO {
    private String name;
    private String email;
    private String password;
}
