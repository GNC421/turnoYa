package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.response.UserStatsResponse;

public interface GetUserStatsPort {
    UserStatsResponse getStats();
}
