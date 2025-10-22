package vn.com.bds.infrastructure.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.bds.domain.repository.ImageStorageRepository;

// import java.io.InputStream; // <-- XÓA IMPORT NÀY
import java.io.IOException; // <-- THÊM IMPORT NÀY
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryImageStorageAdapter implements ImageStorageRepository {

    private final Cloudinary cloudinary;

    @Override
    // SỬA LẠI THAM SỐ VÀ THÊM "throws IOException"
    public String upload(byte[] fileBytes, String fileName) throws IOException {
        try {
            // SỬA LẠI THAM SỐ ĐẦU TIÊN KHI GỌI CLOUDINARY
            Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap("resource_type", "auto"));

            return uploadResult.get("secure_url").toString();
            // SỬA LẠI KIỂU EXCEPTION ĐỂ BẮT CẢ IOException
        } catch (IOException e) { // Bắt IOException trước
            e.printStackTrace();
            throw e; // Ném lại IOException
        } catch (Exception e) { // Bắt các lỗi khác
            e.printStackTrace();
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        // Logic xóa file trên Cloudinary
    }
}