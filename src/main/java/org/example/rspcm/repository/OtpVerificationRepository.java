package org.example.rspcm.repository;

import org.example.rspcm.model.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findFirstByEmailAndCodeAndUsedFalseOrderByIdDesc(String email, String code);
    Optional<OtpVerification> findFirstByEmailAndUsedFalseOrderByIdDesc(String email);
}
