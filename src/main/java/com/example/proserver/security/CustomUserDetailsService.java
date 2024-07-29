package com.example.proserver.security;

import com.example.proserver.error.CustomException;
import com.example.proserver.constans.ServerErrorCodes;
import com.example.proserver.models.UserEntity;
import com.example.proserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(UUID.fromString(identifier))
                .orElseThrow(()-> new CustomException(ServerErrorCodes.USER_NOT_FOUND));
        return new CustomUserDetails(user.getId());
    }
}
