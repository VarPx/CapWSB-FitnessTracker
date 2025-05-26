package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.internal.ActivityType;

@Component
public class TrainingMapper {

    /**
     * Converts a Training entity to its corresponding DTO.
     *
     * @param training the Training entity
     * @return the TrainingDto with mapped fields
     */
    public TrainingDto toDto(Training training) {
        TrainingDto dto = new TrainingDto();
        dto.setId(training.getId());
        dto.setUserId(training.getUser().getId());
        dto.setStartTime(training.getStartTime());
        dto.setEndTime(training.getEndTime());
        dto.setActivityType(training.getActivityType().name());
        dto.setDistance(training.getDistance());
        dto.setAverageSpeed(training.getAverageSpeed());
        return dto;
    }

    /**
     * Converts a TrainingDto and a User entity to a new Training entity.
     *
     * @param dto  the Training DTO
     * @param user the User entity associated with the training
     * @return the Training entity
     */
    public Training toEntity(TrainingDto dto, User user) {
        return new Training(
                user,
                dto.getStartTime(),
                dto.getEndTime(),
                ActivityType.valueOf(dto.getActivityType().toUpperCase()),
                dto.getDistance(),
                dto.getAverageSpeed()
        );
    }
}
