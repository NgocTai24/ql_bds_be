package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository; // Dùng @Repository của Spring
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.UserRepository; // Interface từ domain
import vn.com.bds.infrastructure.repository.datajpa.UserSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.UserEntity;
import vn.com.bds.infrastructure.repository.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository // Đánh dấu đây là 1 Bean và là Adapter Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    // Inject JpaRepository của Spring Data
    private final UserSpringDataRepository springDataRepository;

    @Override
    public User save(User user) {
        // Chuyển domain model sang entity
        UserEntity entity = UserMapper.toEntity(user);
        // Dùng JpaRepository để lưu
        UserEntity savedEntity = springDataRepository.save(entity);
        // Chuyển entity đã lưu ngược lại domain model để trả về
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataRepository.findById(id)
                .map(UserMapper::toDomain); // .map(entity -> UserMapper.toDomain(entity))
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataRepository.existsById(id);
    }
    @Override
    public List<User> findAll() {
        // 1. Gọi hàm findAll() của Spring Data JPA (trả về List<UserEntity>)
        return springDataRepository.findAll().stream()
                // 2. Dùng map để chuyển từng UserEntity sang User (domain model)
                .map(UserMapper::toDomain)
                // 3. Thu thập kết quả thành List<User>
                .collect(Collectors.toList());
    }

}