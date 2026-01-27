package com.turnoya.business.infrastructure.adapters.in.web.controller;

import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.usecases.GetBusinessUseCase;
import com.turnoya.business.application.usecases.RegisterBusinessUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
@Tag(name = "Negocios", description = "API para gestión de negocios")
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final GetBusinessUseCase getBusinessUseCase;

    @Operation(
            summary = "Crear un nuevo negocio",
            description = "Comprueba que no hay un negocio ya creado anteriormente y si no lo está registra unio nuevo y lo devuelve"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Negocio creado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un negocio registrado para este propietario"
            )
    })
    @PostMapping
    public ResponseEntity<BusinessResponse> registerBusiness(
            @RequestHeader("X-User-Id") String ownerId,
            @Valid @RequestBody RegisterBusinessRequest request) {

        BusinessResponse response = registerBusinessUseCase.execute(ownerId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener negocio por ID",
            description = "Recupera un negocio específico usando su identificador único"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Negocio encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Negocio no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable UUID id) {
        return getBusinessUseCase.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Obtener negocio por propietario",
            description = "Recupera un negocio usando el identificador de su propietario"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Negocio encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró negocio para este propietario"
            )
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<BusinessResponse>> getBusinessByOwnerId(@PathVariable String ownerId) {
        List<BusinessResponse> businesses = getBusinessUseCase.getByOwnerId(ownerId);
        return ResponseEntity.ok(businesses);
    }

    @Operation(
            summary = "Listar negocios por categoría",
            description = "Obtiene todos los negocios que pertenecen a una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de negocios (puede estar vacía)"
            )
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BusinessResponse>> getBusinessesByCategory(
            @PathVariable String category) {
        List<BusinessResponse> businesses = getBusinessUseCase.getByCategory(category);
        return ResponseEntity.ok(businesses);
    }

    @Operation(
            summary = "Listar negocios por categoría",
            description = "Obtiene todos los negocios que pertenecen a una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de negocios (puede estar vacía)"
            )
    })
    @GetMapping("/city/{city}")
    public ResponseEntity<List<BusinessResponse>> getBusinessesByCity(
            @PathVariable String city) {
        List<BusinessResponse> businesses = getBusinessUseCase.getByCity(city);
        return ResponseEntity.ok(businesses);
    }

    @Operation(
            summary = "Listar todos los negocios",
            description = "Obtiene el listado completo de todos los negocios registrados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista completa de negocios"
    )
    @GetMapping
    public ResponseEntity<List<BusinessResponse>> getAllBusinesses() {
        List<BusinessResponse> businesses = getBusinessUseCase.getAll();
        return ResponseEntity.ok(businesses);
    }

}