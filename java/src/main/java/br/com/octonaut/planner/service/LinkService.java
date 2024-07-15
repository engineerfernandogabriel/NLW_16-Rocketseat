package br.com.octonaut.planner.service;

import br.com.octonaut.planner.dto.LinkDTO;
import br.com.octonaut.planner.dto.LinkRequest;
import br.com.octonaut.planner.dto.LinkResponse;
import br.com.octonaut.planner.model.Link;
import br.com.octonaut.planner.model.Trip;
import br.com.octonaut.planner.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse registerLink(LinkRequest request, Trip trip) {
        Link newLink = new Link(request.title(), request.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkDTO> findAllLinksByTripId (UUID linkId) {
        return this.linkRepository.findAllByTripId(linkId).stream().map(link -> new LinkDTO(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
