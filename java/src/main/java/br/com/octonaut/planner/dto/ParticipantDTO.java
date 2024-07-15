package br.com.octonaut.planner.dto;

import java.util.UUID;

public record ParticipantDTO(UUID id, String name, String email, boolean isConfirmed) {
}
