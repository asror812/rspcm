package org.example.rspcm.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class CompositeUserDetailsService implements UserDetailsService {

    private final CustomUserDetailsService customUserDetailsService;
    private final InMemoryAdminUserDetailsService inMemoryAdminUserDetailsService;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        try {
            return customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ignored) {
            return inMemoryAdminUserDetailsService.loadUserByUsername(username);
        }
    }
}
