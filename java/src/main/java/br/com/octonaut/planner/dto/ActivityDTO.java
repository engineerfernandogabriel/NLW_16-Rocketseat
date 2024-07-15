package br.com.octonaut.planner.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO (UUID activityId, String title, LocalDateTime occursAt) {
}
