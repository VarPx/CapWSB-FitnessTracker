package pl.wsb.fitnesstracker.training.api;

import java.util.List;

/**
 * Service interface for managing Training operations.
 * Provides business logic for CRUD and filtering functionalities.
 */
public interface TrainingService {

    /**
     * Retrieves all trainings available in the system.
     *
     * @return a list of all training DTOs
     */
    List<TrainingDto> findAllTrainings();

    /**
     * Retrieves all trainings for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of training DTOs for the given user
     */
    List<TrainingDto> findTrainingsByUserId(Long userId);

    /**
     * Retrieves all trainings that ended after the given date.
     *
     * @param date the threshold date in format yyyy-MM-dd
     * @return a list of training DTOs
     */
    List<TrainingDto> findTrainingsAfterDate(String date);

    /**
     * Retrieves all trainings filtered by activity type (e.g., RUNNING).
     *
     * @param type the activity type
     * @return a list of training DTOs matching the activity type
     */
    List<TrainingDto> findTrainingsByActivityType(String type);

    /**
     * Creates and persists a new training.
     *
     * @param dto the training DTO with data to create
     * @return the saved training DTO
     */
    TrainingDto createTraining(TrainingDto dto);

    /**
     * Updates an existing training with the provided data.
     *
     * @param id  the ID of the training to update
     * @param dto the updated training DTO
     * @return the updated training DTO
     */
    TrainingDto updateTraining(Long id, TrainingDto dto);
}
