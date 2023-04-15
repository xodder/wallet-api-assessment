
package com.fundquest.assessment.auth.helpers;

import com.fundquest.assessment.users.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    public User user;
    public String token;
}
