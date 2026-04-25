package org.example.rspcm.service;

import org.example.rspcm.config.AppProperties;
import org.example.rspcm.dto.auth.AuthResponse;
import org.example.rspcm.dto.auth.LoginRequest;
import org.example.rspcm.dto.auth.RegisterRequest;
import org.example.rspcm.dto.auth.VerifyOtpRequest;
import org.example.rspcm.exception.ErrorCodes;
import org.example.rspcm.exception.ErrorMessageException;
import org.example.rspcm.exception.NotFoundException;
import org.example.rspcm.model.entity.User;
import org.example.rspcm.model.entity.OtpVerification;
import org.example.rspcm.model.entity.Role;
import org.example.rspcm.repository.OtpVerificationRepository;
import org.example.rspcm.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailService mailService;
    private final AppProperties appProperties;
    private final UserProfileSyncService userProfileSyncService;
    private final Random random = new Random();

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ErrorMessageException("Email allaqachon mavjud", ErrorCodes.AlreadyExists);
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .roles(roleService.resolveRoles(request.roles()))
                .build();

        User saved = userRepository.save(user);
        userProfileSyncService.sync(saved);
        sendOtp(request.email());
        return "Ro'yxatdan o'tish yakunlandi. OTP emailingizga yuborildi.";
    }

    @Transactional
    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Foydalanuvchi topilmadi"));
        if (user.isEnabled()) {
            throw new ErrorMessageException("Bu akkaunt allaqachon tasdiqlangan", ErrorCodes.AlreadyExists);
        }

        sendOtp(email);
        return "Yangi OTP yuborildi.";
    }

    @Transactional
    public String verifyOtp(VerifyOtpRequest request) {
        OtpVerification otp = otpRepository
                .findFirstByEmailAndCodeAndUsedFalseOrderByIdDesc(request.email(), request.code())
                .orElseThrow(() -> new ErrorMessageException("OTP noto'g'ri", ErrorCodes.InvalidParams));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ErrorMessageException("OTP muddati tugagan", ErrorCodes.InvalidParams);
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("Foydalanuvchi topilmadi"));
        user.setEnabled(true);
        otp.setUsed(true);
        userRepository.save(user);
        otpRepository.save(otp);
        return "Akkaunt muvaffaqiyatli tasdiqlandi.";
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        return userRepository.findByEmail(request.email())
                .map(user -> {
                    if (!user.isEnabled()) {
                        throw new ErrorMessageException("Akkaunt tasdiqlanmagan, OTP kiriting",
                                ErrorCodes.InvalidParams);
                    }

                    String token = jwtService.generateToken(user);
                    Set<String> roles = user.getRoles().stream()
                            .map(Role::getName).collect(Collectors.toSet());

                    return new AuthResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                            roles, token);
                })
                .orElseGet(() -> {
                    Set<String> roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(role -> role.replace("ROLE_", ""))
                            .collect(Collectors.toSet());
                    String token = jwtService.generateToken(request.email(), roles, null);
                    String adminFullName = appProperties.getAdmin().getFullName() == null
                            ? "InMemory Admin"
                            : appProperties.getAdmin().getFullName();
                    return new AuthResponse(
                            null,
                            extractFirstName(adminFullName),
                            extractLastName(adminFullName),
                            request.email(),
                            roles,
                            token);
                });
    }

    public AuthResponse issueSwaggerAdminToken() {
        AppProperties.Admin admin = appProperties.getAdmin();
        if (!admin.isEnabled() || isBlank(admin.getUsername())) {
            throw new ErrorMessageException("In-memory admin o'chirilgan yoki username sozlanmagan",
                    ErrorCodes.BadRequest);
        }

        Set<String> roles = Set.of("ADMIN");
        String token = jwtService.generateToken(admin.getUsername(), roles, null);
        String adminFullName = admin.getFullName() == null || admin.getFullName().isBlank()
                ? "InMemory Admin"
                : admin.getFullName();
        return new AuthResponse(
                null,
                extractFirstName(adminFullName),
                extractLastName(adminFullName),
                admin.getUsername(),
                roles,
                token);
    }

    public String resolveSwaggerPanelToken() {
        String configured = appProperties.getSwagger().getPanelToken();
        if (!isBlank(configured)) {
            return configured.trim();
        }
        return issueSwaggerAdminToken().token();
    }

    private void sendOtp(String email) {
        String code = "%06d".formatted(random.nextInt(1_000_000));
        OtpVerification otp = OtpVerification.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(appProperties.getOtp().getExpirationMinutes()))
                .used(false)
                .build();
        otpRepository.save(otp);
        mailService.sendOtp(email, code);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String extractFirstName(String fullName) {
        if (isBlank(fullName)) {
            return "";
        }
        String normalized = fullName.trim().replaceAll("\\s+", " ");
        int firstSpace = normalized.indexOf(' ');
        if (firstSpace < 0) {
            return normalized;
        }
        return normalized.substring(0, firstSpace);
    }

    private String extractLastName(String fullName) {
        if (isBlank(fullName)) {
            return "";
        }
        String normalized = fullName.trim().replaceAll("\\s+", " ");
        int firstSpace = normalized.indexOf(' ');
        if (firstSpace < 0) {
            return "";
        }
        return normalized.substring(firstSpace + 1);
    }
}
