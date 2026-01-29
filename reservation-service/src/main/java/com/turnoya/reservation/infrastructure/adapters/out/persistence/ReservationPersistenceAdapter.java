package com.turnoya.reservation.infrastructure.adapters.out.persistence;

import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.ports.out.ReservationRepositoryPort;
import com.turnoya.reservation.infrastructure.adapters.out.persistence.entity.ReservationEntity;
import com.turnoya.reservation.infrastructure.adapters.out.persistence.mapper.ReservationEntityMapper;
import com.turnoya.reservation.infrastructure.adapters.out.persistence.repository.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationPersistenceAdapter implements ReservationRepositoryPort {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    @Override
    public Reservation save(Reservation business) {
        ReservationEntity entity = mapper.toEntity(business);
        ReservationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Reservation> findByUser(UUID userId) {
        return jpaRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Reservation> findByBusinessIdAndDateTimeRange(UUID businessId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByBusinessIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(businessId,start,end)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
