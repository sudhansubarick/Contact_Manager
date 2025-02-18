package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

// This form is use to recieve the data 
public class UserForm {

    @NotBlank(message="Username is required")
    @Size(min=3 , message="minimum 3 charecters is required")
    private String name;

    @NotBlank(message="Email Id is required")
    @Email(message="Invalid Email Address")
    private String email;
    
    @NotBlank(message="password is required")
    //@Size(min=6 , message="Minumum 6 charecters is required")
    @Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
    message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;

    @Size(min=8,max=12, message="Invalid phone number")
    private String phoneNumber;

    @NotBlank(message="About is required")
    private String about;
   
   




}
