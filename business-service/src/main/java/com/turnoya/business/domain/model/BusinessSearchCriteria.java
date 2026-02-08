package com.turnoya.business.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class BusinessSearchCriteria {
    private String name;
    private String category;
    private String city;
    private String status;
    private Pageable pageable;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasCategory() {
        return category != null && !category.isBlank();
    }

    public boolean hasCity() {
        return city != null && !city.isBlank();
    }

    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }
}