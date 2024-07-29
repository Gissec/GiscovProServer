package com.example.proserver.security;

import com.example.proserver.constans.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private UUID userid;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userid.toString()));
    }

    @Override
    public String getPassword() {
        return Constants.EMPTY;
    }

    @Override
    public String getUsername() {
        return userid.toString();
    }
}
