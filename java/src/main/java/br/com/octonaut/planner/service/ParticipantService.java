package br.com.octonaut.planner.service;

import br.com.octonaut.planner.dto.ParticipantDTO;
import br.com.octonaut.planner.dto.ParticipantResponse;
import br.com.octonaut.planner.model.Participant;
import br.com.octonaut.planner.model.Trip;
import br.com.octonaut.planner.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> parcipantsToInvite, Trip trip) {
        //parcipantsToInvite.stream().map(email -> new Participant(email, trip)).forEach(participantRepository::save);

        List<Participant> participants = parcipantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);
    }

    public ParticipantResponse registerParticipantToEvent(String email, Trip trip) {
        //parcipantsToInvite.stream().map(email -> new Participant(email, trip)).forEach(participantRepository::save);

        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        return new ParticipantResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {

    }

    public void triggerConfirmationEmailToParticipant(String email) {

    }

    public List<ParticipantDTO> findAllParticipantByTripId(UUID tripId) {
        return this.participantRepository.findAllByTripId(tripId).stream().map(participant -> new ParticipantDTO(participant.getId(), participant.getName(), participant.getEmail(), participant.isConfirmed())).toList();
    }

}