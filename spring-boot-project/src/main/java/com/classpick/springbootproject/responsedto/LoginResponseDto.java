package com.classpick.springbootproject.responsedto;

import com.classpick.springbootproject.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private UserStatus userstatus;


}
