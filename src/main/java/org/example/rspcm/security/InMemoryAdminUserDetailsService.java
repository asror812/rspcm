package org.example.rspcm.security;

import org.example.rspcm.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryAdminUserDetailsService implements UserDetailsService {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final AppProperties appProperties;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        if (!appProperties.getAdmin().isEnabled()) {
            throw new UsernameNotFoundException("In-memory admin disabled");
        }

        return inMemoryUserDetailsManager.loadUserByUsername(username);
    }
}
