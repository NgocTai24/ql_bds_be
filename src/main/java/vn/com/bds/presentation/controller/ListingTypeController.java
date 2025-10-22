package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.bds.domain.model.ListingType;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.CategoryRequest;
import vn.com.bds.presentation.dto.ListingTypeDto;
import vn.com.bds.usecase.ManageListingTypeUseCase;

import java.util.List;
import java.util.UUID; // <-- Import UUID
import java.util.stream.Collectors;

@RestController
@RequestMapping("/listing-types") // Base path for ListingType APIs
@RequiredArgsConstructor
public class ListingTypeController {

    private final ManageListingTypeUseCase manageListingTypeUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ListingTypeDto>>> getAll() {
        List<ListingType> models = manageListingTypeUseCase.getAllListingTypes();
        List<ListingTypeDto> dtos = models.stream()
                .map(ListingTypeDto::fromModel)
                .collect(Collectors.toList());
        return ApiResponse.success(dtos, "Listing types fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ListingTypeDto>> getById(@PathVariable UUID id) { // <-- Use UUID
        ListingType model = manageListingTypeUseCase.getListingTypeById(id); // <-- Pass UUID
        return ApiResponse.success(ListingTypeDto.fromModel(model), "Listing type fetched successfully");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can create
    public ResponseEntity<ApiResponse<ListingTypeDto>> create(@RequestBody CategoryRequest request) {
        // Build the command for the use case
        ManageListingTypeUseCase.ListingTypeCommand command = ManageListingTypeUseCase.ListingTypeCommand.builder()
                .name(request.getName())
                .build();
        ListingType created = manageListingTypeUseCase.createListingType(command);
        return ApiResponse.created(ListingTypeDto.fromModel(created), "Listing type created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can update
    public ResponseEntity<ApiResponse<ListingTypeDto>> update(@PathVariable UUID id, @RequestBody CategoryRequest request) { // <-- Use UUID
        ManageListingTypeUseCase.ListingTypeCommand command = ManageListingTypeUseCase.ListingTypeCommand.builder()
                .name(request.getName())
                .build();
        ListingType updated = manageListingTypeUseCase.updateListingType(id, command); // <-- Pass UUID
        return ApiResponse.success(ListingTypeDto.fromModel(updated), "Listing type updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can delete
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) { // <-- Use UUID
        manageListingTypeUseCase.deleteListingType(id); // <-- Pass UUID
        return ApiResponse.success("Listing type deleted successfully");
    }
}