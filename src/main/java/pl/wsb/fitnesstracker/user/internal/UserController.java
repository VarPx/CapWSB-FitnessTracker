package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.*;

import java.util.List;

/**
 * REST controller for managing users in the FitnessTracker system.
 * Provides CRUD operations as well as search functionality.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    /**
     * Retrieves all users in the system.
     *
     * @return list of users as {@link UserDto}
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Adds a new user to the system.
     *
     * @param userDto the user data to be added
     * @return the newly created user
     */
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userService.createUser(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Retrieves a specific user by ID.
     *
     * @param id the ID of the user
     * @return the user's details
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.toDto(
                userService.getUser(id)
                        .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * Updates a user's information.
     *
     * @param id      the ID of the user to update
     * @param userDto updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updated = userService.updateUser(id, userMapper.toEntity(userDto));
        return userMapper.toDto(updated);
    }

    /**
     * Searches for users by email fragment or by minimum age.
     * Only one of the parameters should be provided.
     *
     * @param email fragment of email to search (case-insensitive)
     * @param ageGt minimum age of users to include
     * @return list of users matching the criteria
     */
    @GetMapping("/search")
    public List<UserShortDto> searchUsers(@RequestParam(required = false) String email,
                                          @RequestParam(required = false) Integer ageGt) {
        if (email != null) {
            return userService.findByEmailFragment(email).stream()
                    .map(user -> new UserShortDto(user.getId(), user.getFirstName(), user.getLastName()))
                    .toList();
        } else if (ageGt != null) {
            return userService.findOlderThan(ageGt).stream()
                    .map(user -> new UserShortDto(user.getId(), user.getFirstName(), user.getLastName()))
                    .toList();
        }
        throw new IllegalArgumentException("Specify either email or ageGt as query parameter.");
    }

    /**
     * Retrieves a short list of users with only ID and names.
     *
     * @return list of simplified user info
     */
    @GetMapping("/short")
    public List<UserShortDto> getAllUsersShort() {
        return userService.findAllUsers()
                .stream()
                .map(user -> new UserShortDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName()))
                .toList();
    }
}
