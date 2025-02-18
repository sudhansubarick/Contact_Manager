package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validator.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ContactForm {

    @NotBlank(message="Name is required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Invalid email address [example@gmail.com]")
    private String email;

    @NotBlank(message="Phone number is required")
    @Pattern(regexp="^[0-9]{10}$",message="Invalid phone Number")
    private String PhoneNumber;

    @NotBlank(message="Address is required")
    private String address;

    private String description;

    private boolean favorite;
    
    private String websiteLink;

    private String linkedInLink;

    @ValidFile(message="Invalid File")
    private MultipartFile contactImage;

    private String picture;
}
