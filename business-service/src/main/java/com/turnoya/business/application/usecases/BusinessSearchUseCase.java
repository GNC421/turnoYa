package com.turnoya.business.application.usecases;

import com.turnoya.business.application.dto.request.BusinessSearchRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.mappers.BusinessMapper;
import com.turnoya.business.application.ports.input.BusinessSearchUseCasePort;
import com.turnoya.business.domain.model.Business;
import com.turnoya.business.domain.model.BusinessSearchCriteria;
import com.turnoya.business.domain.ports.out.BusinessRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.turnoya.business.application.mappers.BusinessMapper.toResponseList;


@Service
@RequiredArgsConstructor
public class BusinessSearchUseCase implements BusinessSearchUseCasePort {

    private final BusinessRepositoryPort businessRepository;

    @Transactional(readOnly = true)
    public List<BusinessResponse> execute(BusinessSearchRequest query) {
        // 1. Validación de entrada
        validateSearchQuery(query);

        // 2. Construcción de criterios de búsqueda
        BusinessSearchCriteria criteria = BusinessSearchCriteria.builder()
                .name(query.name())
                .category(query.category())
                .city(query.city())
                .status(query.status())
                .pageable(PageRequest.of(query.page(), query.size()))
                .build();

        // 3. Llamada al repositorio
        List<Business> businesses = businessRepository.search(criteria);

        // 4. Transformación a respuesta
        return toResponseList(businesses);
    }

    private void validateSearchQuery(BusinessSearchRequest query) {
        if (query.page() < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (query.size() < 1 || query.size() > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }
    }
}