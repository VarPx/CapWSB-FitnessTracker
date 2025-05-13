package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation providing CRUD and search operations for {@link User} entities.
 * Implements both {@link UserService} and {@link UserProvider}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    /**
     * Creates a new user in the system.
     *
     * @param user user to be created
     * @return created user
     * @throws IllegalArgumentException if the user already has an ID (i.e. is already persisted)
     */
    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId user ID
     * @return optional containing user if found, empty otherwise
     */
    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email email to search by
     * @return optional containing user if found, empty otherwise
     */
    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of users
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     *
     * @param id ID of the user to be deleted
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user with ID {}", id);
    }

    /**
     * Updates an existing user's information.
     *
     * @param id   ID of the user to update
     * @param user new user data
     * @return updated user
     * @throws IllegalArgumentException if user with given ID is not found
     */
    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(user.getFirstName());
                    existing.setLastName(user.getLastName());
                    existing.setBirthdate(user.getBirthdate());
                    existing.setEmail(user.getEmail());
                    log.info("Updating User {}", existing);
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("User with ID %d not found".formatted(id)));
    }

    /**
     * Finds users whose email contains the given fragment (case-insensitive).
     *
     * @param fragment email fragment
     * @return list of matching users
     */
    @Override
    public List<User> findByEmailFragment(String fragment) {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().toLowerCase().contains(fragment.toLowerCase()))
                .toList();
    }

    /**
     * Finds users older than the specified age.
     *
     * @param age minimum age in years
     * @return list of users older than the specified age
     */
    @Override
    public List<User> findOlderThan(int age) {
        return userRepository.findAll().stream()
                .filter(u -> u.getBirthdate() != null)
                .filter(u -> u.getBirthdate().isBefore(java.time.LocalDate.now().minusYears(age)))
                .toList();
    }
}
