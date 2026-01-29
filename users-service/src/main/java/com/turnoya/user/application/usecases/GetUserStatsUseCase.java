package com.turnoya.user.application.usecases;

import com.turnoya.user.application.dto.request.UpdateUserRequest;
import com.turnoya.user.application.dto.response.UserResponse;
import com.turnoya.user.application.dto.response.UserStatsResponse;
import com.turnoya.user.application.ports.input.GetUserStatsPort;
import com.turnoya.user.application.ports.output.ReservationServicePort;
import com.turnoya.user.application.ports.output.UserRepositoryPort;
import com.turnoya.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserStatsUseCase implements GetUserStatsPort {
    private final ReservationServicePort reservationServicePort;
    private final UserRepositoryPort repository;

    @Override
    @Transactional
    public List<UserStatsResponse> getStats(List<UUID> userIds) {
        List<UserStatsResponse> toReturn = new LinkedList<>();
        Map<UUID, Double> reservationsResponse = reservationServicePort.getPercentageBooksFailed(userIds);
        for(UUID id: userIds) {
            User u = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
            toReturn.add(UserStatsResponse.builder()
                    .id(id)
                    .email(u.getEmail())
                    .name(u.getName())
                    .phone(u.getPhone())
                    .booksFailed(reservationsResponse.get(id))
                    .build());
        }
        return toReturn;
    }
}
