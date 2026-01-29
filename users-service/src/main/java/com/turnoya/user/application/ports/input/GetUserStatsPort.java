package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.response.UserStatsResponse;

import java.util.List;
import java.util.UUID;

public interface GetUserStatsPort {
    List<UserStatsResponse> getStats(List<UUID> userIds);
}
