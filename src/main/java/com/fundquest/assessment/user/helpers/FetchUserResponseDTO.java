package com.fundquest.assessment.user.helpers;

import com.fundquest.assessment.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchUserResponseDTO {
    private User user;

    public static FetchUserResponseDTO of(User user) {
        return new FetchUserResponseDTO(user);
    }
}
