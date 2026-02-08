package com.turnoya.business.application.ports.input;

import com.turnoya.business.application.dto.request.BusinessSearchRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;

import java.util.List;

public interface BusinessSearchUseCasePort {
    List<BusinessResponse>  execute(BusinessSearchRequest query);
}
