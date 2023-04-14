package com.fundquest.assessment.auth.helpers;

import com.fundquest.assessment.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private User user;
    private String token;
}
