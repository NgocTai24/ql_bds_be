package vn.com.bds.domain.repository;

// import java.io.InputStream; // <-- XÓA IMPORT NÀY
import java.io.IOException; // <-- CÓ THỂ CẦN THÊM

public interface ImageStorageRepository {
    /**
     * Tải file lên và trả về URL công khai
     *
     * @param fileBytes Dữ liệu của file dưới dạng byte array
     * @param fileName  Tên file gốc (dùng cho Cloudinary)
     * @return URL của file đã tải lên
     * @throws IOException Nếu có lỗi đọc file
     */
    String upload(byte[] fileBytes, String fileName) throws IOException;
    void delete(String fileUrl);
}