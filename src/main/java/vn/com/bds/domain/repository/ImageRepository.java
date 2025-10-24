package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Image;
import java.util.List;

public interface ImageRepository {
    Image save(Image image);
    List<Image> saveAll(List<Image> images); // To save multiple images at once
    // Add findByPostId, delete methods later if needed
}