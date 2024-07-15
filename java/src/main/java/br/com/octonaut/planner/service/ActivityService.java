package br.com.octonaut.planner.service;

import br.com.octonaut.planner.dto.ActivityDTO;
import br.com.octonaut.planner.dto.ActivityResponse;
import br.com.octonaut.planner.dto.ActivityRequest;
import br.com.octonaut.planner.model.Activity;
import br.com.octonaut.planner.model.Trip;
import br.com.octonaut.planner.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequest request, Trip trip) {
        Activity newActivity = new Activity(request.title(), request.occurs_At(), trip);

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());

    }

    public List<ActivityDTO> findAllActivitiesByTripId(UUID tripId) {
        return this.activityRepository.findAllByTripId(tripId).stream().map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
