package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.PropertyType;
import java.util.List;
import java.util.UUID; // <-- Import

public interface ManagePropertyTypeUseCase {
    List<PropertyType> getAllPropertyTypes();
    List<PropertyType> getPropertyTypesByListingTypeId(UUID listingTypeId); // <-- Change to UUID
    PropertyType getPropertyTypeById(UUID id); // <-- Change to UUID
    PropertyType createPropertyType(PropertyTypeCommand command);
    PropertyType updatePropertyType(UUID id, PropertyTypeCommand command); // <-- Change to UUID
    void deletePropertyType(UUID id); // <-- Change to UUID

    @Value @Builder
    class PropertyTypeCommand {
        String name;
        UUID listingTypeId; // <-- Change to UUID
    }
}