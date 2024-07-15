package br.com.octonaut.planner.controller;

import br.com.octonaut.planner.dto.*;
import br.com.octonaut.planner.model.Trip;
import br.com.octonaut.planner.repository.TripRepository;
import br.com.octonaut.planner.service.ActivityService;
import br.com.octonaut.planner.service.LinkService;
import br.com.octonaut.planner.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    public ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository tripRepository;

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequest request) {
        Trip newTrip = new Trip(request);

        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToEvent(request.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripResponse(newTrip.getId()));
    }

    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequest request) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();

            ParticipantResponse response = this.participantService.registerParticipantToEvent(request.email(), updatedTrip);

            if(updatedTrip.isConfirmed()) {
                this.participantService.triggerConfirmationEmailToParticipant(request.email());
            }

            return ResponseEntity.ok(response);
        }

         return ResponseEntity.notFound().build();
    }

    @GetMapping({"/{tripId}"})
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping({"/{tripId}/confirm"})
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();
            updatedTrip.setConfirmed(true);

            this.tripRepository.save(updatedTrip);

            this.participantService.triggerConfirmationEmailToParticipants(tripId);

            return ResponseEntity.ok(updatedTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping({"/{tripId}"})
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequest request) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();
            updatedTrip.setEndsAt(LocalDateTime.parse(request.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            updatedTrip.setStartsAt(LocalDateTime.parse(request.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            updatedTrip.setDestination(request.destination());

            this.tripRepository.save(updatedTrip);

            return ResponseEntity.ok(updatedTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(@PathVariable UUID tripId) {
        List<ParticipantDTO> participantsList = this.participantService.findAllParticipantByTripId(tripId);

        return ResponseEntity.ok(participantsList);
    }

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID tripId, @RequestBody ActivityRequest request) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();

            ActivityResponse response = this.activityService.registerActivity(request, updatedTrip);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable UUID tripId) {
        List<ActivityDTO> activityList = this.activityService.findAllActivitiesByTripId(tripId);

        return ResponseEntity.ok(activityList);
    }

    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequest request) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();

            LinkResponse response = this.linkService.registerLink(request, updatedTrip);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkDTO>> getAllLinks(@PathVariable UUID tripId) {
        List<LinkDTO> linkList = this.linkService.findAllLinksByTripId(tripId);

        return ResponseEntity.ok(linkList);
    }


}
