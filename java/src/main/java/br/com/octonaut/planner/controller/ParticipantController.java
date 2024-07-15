package br.com.octonaut.planner.controller;

import br.com.octonaut.planner.dto.ParticipantRequest;
import br.com.octonaut.planner.model.Participant;
import br.com.octonaut.planner.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{tripId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequest request) {
        Optional<Participant> participant = this.participantRepository.findById(tripId);

        if(participant.isPresent()) {
            Participant updatedParticipant = participant.get();
            updatedParticipant.setConfirmed(true);
            updatedParticipant.setName(request.name());

            this.participantRepository.save(updatedParticipant);

            return ResponseEntity.ok(updatedParticipant);
        }

            return ResponseEntity.notFound().build();
    }
}
