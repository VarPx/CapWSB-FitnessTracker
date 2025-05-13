package pl.wsb.fitnesstracker.user.api;

import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for read-only access to {@link User} entities.
 */
public interface UserProvider {

    Optional<User> getUser(Long userId);

    Optional<User> getUserByEmail(String email);

    List<User> findAllUsers();

    List<User> findByEmailFragment(String fragment);

    List<User> findOlderThan(int age);
}
