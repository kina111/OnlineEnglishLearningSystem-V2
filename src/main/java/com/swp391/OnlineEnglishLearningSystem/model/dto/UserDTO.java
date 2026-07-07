package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.util.PasswordMatches;
import com.swp391.OnlineEnglishLearningSystem.util.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatches(message = "Mật khẩu nhập lại không giống")
public class UserDTO {
    @NotBlank(message = "Vui lòng nhập tên đầy đủ")
    private String fullName;

    @ValidEmail(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    private String password;

    private String confirmedPassword;

    public UserDTO(String fullName, String email, String password, String confirmedPassword) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }
}
