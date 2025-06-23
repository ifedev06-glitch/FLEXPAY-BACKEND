package com.json.flexpay.dto;

import com.json.flexpay.entity.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstname;

    private String lastname;

    private String username;

    private Date dob;

    private long tel;

    private String password;

    private String gender;

    public UserDto(User user) {
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.username = user.getUsername();
        this.dob = user.getDob();
        this.tel = user.getTel();
        this.gender = user.getGender();
    }
}
