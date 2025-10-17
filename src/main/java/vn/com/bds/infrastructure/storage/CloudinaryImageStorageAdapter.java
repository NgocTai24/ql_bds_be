package vn.com.bds.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.bds.domain.repository.ImageStorageRepository;

import java.io.InputStream;
import java.util.Map;

@Component // Báo cho Spring biết đây là 1 Bean
@RequiredArgsConstructor // Tự động inject Cloudinary bean
public class CloudinaryImageStorageAdapter implements ImageStorageRepository {

    private final Cloudinary cloudinary; // Cloudinary SDK bean

    @Override
    public String upload(InputStream fileStream, String fileName) {
        try {
            // Sử dụng SDK của Cloudinary để upload
            Map uploadResult = cloudinary.uploader().upload(fileStream, ObjectUtils.emptyMap());
            // Trả về URL an toàn
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            // Xử lý lỗi (ví dụ: ném ra 1 exception tùy chỉnh)
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        // Logic xóa file trên Cloudinary
    }
}