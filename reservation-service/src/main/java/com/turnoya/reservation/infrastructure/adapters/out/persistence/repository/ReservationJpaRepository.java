package com.turnoya.reservation.infrastructure.adapters.out.persistence.repository;

import com.turnoya.reservation.infrastructure.adapters.out.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {

    List<ReservationEntity> findByUserId(UUID userId);

    List<ReservationEntity> findByBusinessId(UUID businessId);

    List<ReservationEntity> findByBusinessIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(UUID businessId, LocalDateTime start, LocalDateTime end);
}