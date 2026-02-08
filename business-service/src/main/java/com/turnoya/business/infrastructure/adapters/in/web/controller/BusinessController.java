package com.turnoya.business.infrastructure.adapters.in.web.controller;

import com.turnoya.business.application.dto.request.BusinessSearchRequest;
import com.turnoya.business.application.dto.request.RegisterBusinessRequest;
import com.turnoya.business.application.dto.response.BusinessResponse;
import com.turnoya.business.application.ports.input.BusinessSearchUseCasePort;
import com.turnoya.business.application.ports.input.GetBusinessUseCasePort;
import com.turnoya.business.application.ports.input.RegisterBusinessUseCasePort;
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

    private final RegisterBusinessUseCasePort registerBusinessUseCase;
    private final GetBusinessUseCasePort getBusinessUseCase;
    private final BusinessSearchUseCasePort businessSearchUseCase;

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

    @GetMapping("/search")
    public ResponseEntity<List<BusinessResponse>> searchBusinesses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        BusinessSearchRequest query = new BusinessSearchRequest(name, category, city, status, page, size);

        List<BusinessResponse> businesses = businessSearchUseCase.execute(query);
        return ResponseEntity.ok(businesses);
    }

}