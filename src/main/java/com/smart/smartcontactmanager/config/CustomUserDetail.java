package com.smart.smartcontactmanager.config;

import com.smart.smartcontactmanager.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetail implements UserDetails {

    private User user;

    public CustomUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(user.getRole()); // ROLE_USER or ROLE_ADMIN
    return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Change this based on your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Change this based on your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Change this based on your logic
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




    
}



