package org.example.rspcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class InMemoryAdminConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(AppProperties appProperties) {
        AppProperties.Admin admin = appProperties.getAdmin();
        if (!admin.isEnabled() || isBlank(admin.getUsername()) || isBlank(admin.getPassword())) {
            return new InMemoryUserDetailsManager();
        }
        return new InMemoryUserDetailsManager(
                User.withUsername(admin.getUsername())
                        .password(new BCryptPasswordEncoder().encode(admin.getPassword()))
                        .roles("ADMIN")
                        .build()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
