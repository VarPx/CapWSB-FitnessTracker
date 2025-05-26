package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingService;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.util.List;

/**
 * REST controller for handling training-related operations.
 * Supports listing, creating, updating and filtering trainings.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;  // 🟢 użycie interfejsu
    private final TrainingMapper trainingMapper;
    private final UserProvider userProvider;

    /**
     * Retrieves a list of all trainings.
     * @return list of training DTOs
     */
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.findAllTrainings();
    }

    /**
     * Retrieves trainings for a specific user.
     * @param userId user ID
     * @return list of training DTOs
     */
    @GetMapping("/user/{userId}")
    public List<TrainingDto> getTrainingsForUser(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId);
    }

    /**
     * Retrieves trainings that ended after the given date.
     * @param date date in format yyyy-MM-dd
     * @return list of training DTOs
     */
    @GetMapping("/after/{date}")
    public List<TrainingDto> getTrainingsAfter(@PathVariable String date) {
        return trainingService.findTrainingsAfterDate(date);
    }

    /**
     * Retrieves trainings by activity type.
     * @param type activity type (e.g., RUNNING, CYCLING)
     * @return list of training DTOs
     */
    @GetMapping("/activity/{type}")
    public List<TrainingDto> getTrainingsByActivity(@PathVariable String type) {
        return trainingService.findTrainingsByActivityType(type);
    }

    /**
     * Creates a new training.
     * @param dto training DTO
     * @return saved training DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto createTraining(@RequestBody TrainingDto dto) {
        return trainingService.createTraining(dto);
    }

    /**
     * Updates an existing training.
     * @param id training ID
     * @param dto updated training DTO
     * @return updated training DTO
     */
    @PutMapping("/{id}")
    public TrainingDto updateTraining(@PathVariable Long id, @RequestBody TrainingDto dto) {
        return trainingService.updateTraining(id, dto);
    }
}
