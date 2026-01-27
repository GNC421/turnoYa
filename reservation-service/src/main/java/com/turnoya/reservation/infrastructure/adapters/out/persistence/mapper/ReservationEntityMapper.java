package com.turnoya.reservation.infrastructure.adapters.out.persistence.mapper;

import com.turnoya.reservation.domain.model.Reservation;
import com.turnoya.reservation.domain.model.ReservationStatus;
import com.turnoya.reservation.infrastructure.adapters.out.persistence.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationEntityMapper {

    public ReservationEntity toEntity(Reservation domain) {
        if (domain == null) {
            return null;
        }

        return ReservationEntity.builder()
                .id(domain.getId())
                .businessId(domain.getBusinessId())
                .userId(domain.getUserId())
                .startDateTime(domain.getStartDateTime())
                .endDateTime(domain.getEndDateTime())
                .status(mapStatusToEntity(domain.getStatus()))
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Reservation.from(
                entity.getId(),
                entity.getBusinessId(),
                entity.getUserId(),
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                mapStatusToDomain(entity.getStatus()),
                entity.getCreatedAt()
        );
    }

    private ReservationEntity.ReservationStatus mapStatusToEntity(ReservationStatus domainStatus) {
        if (domainStatus == null) {
            return ReservationEntity.ReservationStatus.BOOKED;
        }

        return ReservationEntity.ReservationStatus.valueOf(domainStatus.name());
    }

    private ReservationStatus mapStatusToDomain(ReservationEntity.ReservationStatus entityStatus) {
        if (entityStatus == null) {
            return ReservationStatus.BOOKED;
        }

        return ReservationStatus.valueOf(entityStatus.name());
    }
}
