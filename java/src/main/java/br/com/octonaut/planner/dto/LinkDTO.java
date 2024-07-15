package br.com.octonaut.planner.dto;

import java.util.UUID;

public record LinkDTO(UUID linkId, String title, String url) {
}
