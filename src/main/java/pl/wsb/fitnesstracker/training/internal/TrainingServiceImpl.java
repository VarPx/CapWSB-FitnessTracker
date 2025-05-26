package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.api.TrainingService;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final UserProvider userProvider;

    @Override
    public List<TrainingDto> findAllTrainings() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public List<TrainingDto> findTrainingsByUserId(Long userId) {
        return trainingRepository.findAll().stream()
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public List<TrainingDto> findTrainingsAfterDate(String date) {
        LocalDate parsed = LocalDate.parse(date);
        Date threshold = Date.from(parsed.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return trainingRepository.findAll().stream()
                .filter(t -> t.getEndTime() != null && t.getEndTime().after(threshold))
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public List<TrainingDto> findTrainingsByActivityType(String type) {
        return trainingRepository.findAll().stream()
                .filter(t -> t.getActivityType().name().equalsIgnoreCase(type))
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public TrainingDto createTraining(TrainingDto dto) {
        User user = userProvider.getUser(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Training training = trainingMapper.toEntity(dto, user);
        return trainingMapper.toDto(trainingRepository.save(training));
    }

    @Override
    public TrainingDto updateTraining(Long id, TrainingDto dto) {
        User user = userProvider.getUser(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return trainingRepository.findById(id)
                .map(existing -> {
                    existing.setStartTime(dto.getStartTime());
                    existing.setEndTime(dto.getEndTime());
                    existing.setActivityType(pl.wsb.fitnesstracker.training.internal.ActivityType.valueOf(dto.getActivityType()));
                    existing.setDistance(dto.getDistance());
                    existing.setAverageSpeed(dto.getAverageSpeed());
                    return trainingMapper.toDto(trainingRepository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("Training not found with ID: " + id));
    }
}
