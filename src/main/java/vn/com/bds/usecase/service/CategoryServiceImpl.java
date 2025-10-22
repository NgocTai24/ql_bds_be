package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.domain.model.ListingType;
import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.domain.repository.ListingTypeRepository;
import vn.com.bds.domain.repository.PropertyTypeRepository;
import vn.com.bds.usecase.ManageListingTypeUseCase;
import vn.com.bds.usecase.ManagePropertyTypeUseCase;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements ManageListingTypeUseCase, ManagePropertyTypeUseCase {

    private final ListingTypeRepository listingTypeRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    // --- ListingType Methods (Updated to UUID) ---
    @Override @Transactional(readOnly = true)
    public List<ListingType> getAllListingTypes() {
        return listingTypeRepository.findAll();
    }

    @Override @Transactional(readOnly = true)
    public ListingType getListingTypeById(UUID id) { // <-- Changed to UUID
        return listingTypeRepository.findById(id) // <-- Pass UUID
                .orElseThrow(() -> new ResourceNotFoundException("ListingType not found: " + id));
    }

    @Override
    public ListingType createListingType(ListingTypeCommand command) {
        ListingType newType = ListingType.builder().name(command.getName()).build();
        return listingTypeRepository.save(newType);
    }

    @Override
    public ListingType updateListingType(UUID id, ListingTypeCommand command) { // <-- Changed to UUID
        ListingType existingType = getListingTypeById(id); // <-- Pass UUID
        existingType.setName(command.getName());
        return listingTypeRepository.save(existingType);
    }

    @Override
    public void deleteListingType(UUID id) { // <-- Changed to UUID
        if (!listingTypeRepository.existsById(id)) { // <-- Pass UUID
            throw new ResourceNotFoundException("ListingType not found: " + id);
        }
        listingTypeRepository.deleteById(id); // <-- Pass UUID
    }

    // --- PropertyType Methods (Updated to UUID) ---
    @Override @Transactional(readOnly = true)
    public List<PropertyType> getAllPropertyTypes() {
        return propertyTypeRepository.findAll();
    }

    @Override @Transactional(readOnly = true)
    public List<PropertyType> getPropertyTypesByListingTypeId(UUID listingTypeId) { // <-- Changed to UUID
        return propertyTypeRepository.findByListingTypeId(listingTypeId); // <-- Pass UUID
    }

    @Override @Transactional(readOnly = true)
    public PropertyType getPropertyTypeById(UUID id) { // <-- Changed to UUID
        return propertyTypeRepository.findById(id) // <-- Pass UUID
                .orElseThrow(() -> new ResourceNotFoundException("PropertyType not found: " + id));
    }

    @Override
    public PropertyType createPropertyType(PropertyTypeCommand command) {
        // Find parent by UUID
        ListingType parent = listingTypeRepository.findById(command.getListingTypeId()) // <-- Pass UUID
                .orElseThrow(() -> new ResourceNotFoundException("Parent ListingType not found: " + command.getListingTypeId()));

        PropertyType newType = PropertyType.builder()
                .name(command.getName())
                .listingType(parent)
                .build();
        return propertyTypeRepository.save(newType);
    }

    @Override
    public PropertyType updatePropertyType(UUID id, PropertyTypeCommand command) { // <-- Changed to UUID
        PropertyType existingType = getPropertyTypeById(id); // <-- Pass UUID
        // Find parent by UUID
        ListingType parent = listingTypeRepository.findById(command.getListingTypeId()) // <-- Pass UUID
                .orElseThrow(() -> new ResourceNotFoundException("Parent ListingType not found: " + command.getListingTypeId()));

        existingType.setName(command.getName());
        existingType.setListingType(parent);
        return propertyTypeRepository.save(existingType);
    }

    @Override
    public void deletePropertyType(UUID id) { // <-- Changed to UUID
        if (!propertyTypeRepository.existsById(id)) { // <-- Pass UUID
            throw new ResourceNotFoundException("PropertyType not found: " + id);
        }
        propertyTypeRepository.deleteById(id); // <-- Pass UUID
    }
}