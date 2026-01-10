package spring.miniprojet.service;

import spring.miniprojet.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    User save(User user);

    User update(Long id, User user);

    void delete(Long id);

    void changePassword(Long userId, String oldPassword, String newPassword);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
