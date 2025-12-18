package com.energy.function.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//import lombok.NonNull;
//
//import org.hibernate.annotations.NotFound;

@Data
public class UserSignInDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "签到码不能为空")
    private String signInCode;

    @NotBlank(message = "座位号不能为空")
    private String seatNumber;

}
