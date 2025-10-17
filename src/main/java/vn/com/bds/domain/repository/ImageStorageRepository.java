package vn.com.bds.domain.repository;

import java.io.InputStream;

public interface ImageStorageRepository {
    /**
     * Tải file lên và trả về URL công khai
     *
     * @param fileStream Dữ liệu của file
     * @param fileName   Tên file gốc
     * @return URL của file đã tải lên
     */
    String upload(InputStream fileStream, String fileName);

    /**
     * Xóa file dựa trên URL
     *
     * @param fileUrl URL của file cần xóa
     */
    void delete(String fileUrl);
}