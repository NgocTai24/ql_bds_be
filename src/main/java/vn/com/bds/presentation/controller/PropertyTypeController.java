package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.CategoryRequest;
import vn.com.bds.presentation.dto.PropertyTypeDto;
import vn.com.bds.usecase.ManagePropertyTypeUseCase;

import java.util.List;
import java.util.UUID; // <-- Import UUID
import java.util.stream.Collectors;

@RestController
@RequestMapping("/property-types") // Base path for PropertyType APIs
@RequiredArgsConstructor
public class PropertyTypeController {

    private final ManagePropertyTypeUseCase managePropertyTypeUseCase;

    // Get All or By ListingType ID
    @GetMapping
    public ResponseEntity<ApiResponse<List<PropertyTypeDto>>> getAll(
            @RequestParam(required = false) UUID listingTypeId) { // <-- Use UUID
        List<PropertyType> models;
        if (listingTypeId != null) {
            models = managePropertyTypeUseCase.getPropertyTypesByListingTypeId(listingTypeId); // <-- Pass UUID
        } else {
            models = managePropertyTypeUseCase.getAllPropertyTypes();
        }
        List<PropertyTypeDto> dtos = models.stream()
                .map(PropertyTypeDto::fromModel)
                .collect(Collectors.toList());
        String message = (listingTypeId != null) ? "Property types fetched for listing type" : "All property types fetched successfully";
        return ApiResponse.success(dtos, message);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyTypeDto>> getById(@PathVariable UUID id) { // <-- Use UUID
        PropertyType model = managePropertyTypeUseCase.getPropertyTypeById(id); // <-- Pass UUID
        return ApiResponse.success(PropertyTypeDto.fromModel(model), "Property type fetched successfully");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can create
    public ResponseEntity<ApiResponse<PropertyTypeDto>> create(@RequestBody CategoryRequest request) {
        ManagePropertyTypeUseCase.PropertyTypeCommand command = ManagePropertyTypeUseCase.PropertyTypeCommand.builder()
                .name(request.getName())
                .listingTypeId(request.getListingTypeId()) // <-- Pass UUID from request DTO
                .build();
        PropertyType created = managePropertyTypeUseCase.createPropertyType(command);
        return ApiResponse.created(PropertyTypeDto.fromModel(created), "Property type created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can update
    public ResponseEntity<ApiResponse<PropertyTypeDto>> update(@PathVariable UUID id, @RequestBody CategoryRequest request) { // <-- Use UUID
        ManagePropertyTypeUseCase.PropertyTypeCommand command = ManagePropertyTypeUseCase.PropertyTypeCommand.builder()
                .name(request.getName())
                .listingTypeId(request.getListingTypeId()) // <-- Pass UUID from request DTO
                .build();
        PropertyType updated = managePropertyTypeUseCase.updatePropertyType(id, command); // <-- Pass UUID
        return ApiResponse.success(PropertyTypeDto.fromModel(updated), "Property type updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can delete
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) { // <-- Use UUID
        managePropertyTypeUseCase.deletePropertyType(id); // <-- Pass UUID
        return ApiResponse.success("Property type deleted successfully");
    }
}