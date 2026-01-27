package com.turnoya.user.application.ports.input;

import com.turnoya.user.application.dto.request.ValidateCredentialsRequest;
import com.turnoya.user.application.dto.response.ValidateCredentialsResponse;

public interface ValidateCredentialsPort {
    ValidateCredentialsResponse execute(ValidateCredentialsRequest request);
}