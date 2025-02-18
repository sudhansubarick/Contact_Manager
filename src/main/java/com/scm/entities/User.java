package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "users") // set table name
// lombok uses
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User implements UserDetails {

    @Id // set primary key to userId
    private String userId;
    @Column(name = "user_name", nullable = false) // customize coloumn name "name" - "user_name"
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Getter(value=AccessLevel.NONE)
    private String password;
    @Column(length = 1000) // about coloumn length 10000 charecters
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNumber;
    // inforamation
    @Getter(value=AccessLevel.NONE)
    private boolean enabled = false;
    
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    private String emailToken;

    // login by self, google, facebook, x , linkedin, github
    
    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;

    // Add more fields if needed
    // cascadetype.All mean if user delete then its all data delete , user update
    // then user update
    // fetchType.LAZY mean when we get user contact then query will start otherwise
    // not fire query
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //list of roles(User,Admin)
        //SimpleGrantedAuthority carries roles[USER,ADMIN etc]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
       return roles;
    }
    @Override
    public String getUsername() {
       return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
       }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
   return this.enabled ;
}

    @Override
    public String getPassword() {
        return this.password;
    }

 

}
