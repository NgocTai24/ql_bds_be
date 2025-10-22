package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.ListingType;

import java.util.List;
import java.util.UUID; // <-- Import

public interface ManageListingTypeUseCase {
    List<ListingType> getAllListingTypes();
    ListingType getListingTypeById(UUID id); // <-- Change to UUID
    ListingType createListingType(ListingTypeCommand command);
    ListingType updateListingType(UUID id, ListingTypeCommand command); // <-- Change to UUID
    void deleteListingType(UUID id); // <-- Change to UUID
    // Command stays the same
    @Value @Builder class ListingTypeCommand { String name; }
}