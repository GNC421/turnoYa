package com.turnoya.business.application.ports.input;

import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;

public interface RegisterBusinessUseCasePort {
    BusinessResponse execute(String ownerId, RegisterBusinessRequest request);
}
