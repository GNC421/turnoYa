package com.turnoya.business.infrastructure.adapters.out.persistence.repository;

import com.turnoya.business.domain.model.BusinessCategory;
import com.turnoya.business.infrastructure.adapters.out.persistence.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessJpaRepository extends JpaRepository<BusinessEntity, UUID>, JpaSpecificationExecutor<BusinessEntity> {

    List<BusinessEntity> findByOwnerId(String ownerId);

    @Query(value = "SELECT * FROM businesses WHERE category = :cat", nativeQuery = true)
    List<BusinessEntity> findByCategory(@Param("cat") String category);

    List<BusinessEntity> findByCity(String city);

    @Query("SELECT b FROM BusinessEntity b WHERE b.category = :category AND b.city = :city")
    List<BusinessEntity> findByCategoryAndCity(
            @Param("category") String category,
            @Param("city") String city
    );

    boolean existsByNameAndCity(String name, String city);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(b) > 0 FROM BusinessEntity b WHERE b.name = :name AND b.city = :city AND b.id != :id")
    boolean existsByNameAndCityAndIdNot(
            @Param("name") String name,
            @Param("city") String city,
            @Param("id") UUID id
    );
}