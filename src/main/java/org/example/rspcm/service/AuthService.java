package org.example.rspcm.service;

import org.example.rspcm.config.AppProperties;
import org.example.rspcm.dto.auth.AuthResponse;
import org.example.rspcm.dto.auth.LoginRequest;
import org.example.rspcm.dto.auth.RegisterRequest;
import org.example.rspcm.dto.auth.VerifyOtpRequest;
import org.example.rspcm.exception.BadRequestException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.AppUser;
import org.example.rspcm.model.entity.OtpVerification;
import org.example.rspcm.repository.AppUserRepository;
import org.example.rspcm.repository.OtpVerificationRepository;
import org.example.rspcm.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailService mailService;
    private final AppProperties appProperties;
    private final UserProfileSyncService userProfileSyncService;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email allaqachon mavjud");
        }

        AppUser user = AppUser.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .roles(roleService.resolveRoles(request.roles()))
                .build();

        AppUser saved = userRepository.save(user);
        userProfileSyncService.sync(saved);
        sendOtp(request.email());
        return "Ro'yxatdan o'tish yakunlandi. OTP emailingizga yuborildi.";
    }

    @Transactional
    public String resendOtp(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Foydalanuvchi topilmadi"));
        if (user.isEnabled()) {
            throw new BadRequestException("Bu akkaunt allaqachon tasdiqlangan");
        }
        sendOtp(email);
        return "Yangi OTP yuborildi.";
    }

    @Transactional
    public String verifyOtp(VerifyOtpRequest request) {
        OtpVerification otp = otpRepository.findFirstByEmailAndCodeAndUsedFalseOrderByIdDesc(request.email(), request.code())
                .orElseThrow(() -> new BadRequestException("OTP noto'g'ri"));
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP muddati tugagan");
        }
        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("Foydalanuvchi topilmadi"));
        user.setEnabled(true);
        otp.setUsed(true);
        userRepository.save(user);
        otpRepository.save(otp);
        return "Akkaunt muvaffaqiyatli tasdiqlandi.";
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return userRepository.findByEmail(request.email())
                .map(user -> {
                    if (!user.isEnabled()) {
                        throw new BadRequestException("Akkaunt tasdiqlanmagan, OTP kiriting");
                    }
                    String token = jwtService.generateToken(user);
                    Set<String> roles = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
                    return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), roles, token);
                })
                .orElseGet(() -> {
                    Set<String> roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(role -> role.replace("ROLE_", ""))
                            .collect(Collectors.toSet());
                    String token = jwtService.generateToken(request.email(), roles, null);
                    String fullName = appProperties.getAdmin().getFullName() == null
                            ? "InMemory Admin"
                            : appProperties.getAdmin().getFullName();
                    return new AuthResponse(null, fullName, request.email(), roles, token);
                });
    }

    private void sendOtp(String email) {
        String code = "%06d".formatted(new Random().nextInt(1_000_000));
        OtpVerification otp = OtpVerification.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(appProperties.getOtp().getExpirationMinutes()))
                .used(false)
                .build();
        otpRepository.save(otp);
        mailService.sendOtp(email, code);
    }
}
