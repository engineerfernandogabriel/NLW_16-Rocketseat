package br.com.octonaut.planner.repository;

import br.com.octonaut.planner.model.Activity;
import br.com.octonaut.planner.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {

    List<Link> findAllByTripId(UUID tripId);
}
