package pt.amane.bds02.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.amane.bds02.dto.EventDTO;
import pt.amane.bds02.entities.City;
import pt.amane.bds02.entities.Event;
import pt.amane.bds02.repositories.EventRepository;
import pt.amane.bds02.services.exceptions.ObjectNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Optional<Event> objId = repository.findById(id);
		Event event = objId.orElseThrow(
				() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + Event.class.getName()));
		return new EventDTO(event);
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> findAll(Pageable pageable) {
		Page<Event> list = repository.findAll(pageable);
		return list.map(dto -> new EventDTO(dto));
	}

	@Transactional
	public EventDTO create(EventDTO dto) {
		Event event = new Event();
		event.setName(dto.getName());
		event = repository.save(event);
		return new EventDTO(event);
	}

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event event = repository.getOne(id);
			copyEventDto(event, dto);
			event = repository.save(event);
			return new EventDTO(event);
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Id not found! Id: " + id);
		}
	}

	public void delete(Long id) {

		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Id not Found! Id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new pt.amane.bds02.services.exceptions.DataIntegrityViolationException(
					"City cannot be deleted! has an assotiated event..");
		}
	}

	private void copyEventDto(Event event, EventDTO dto) {
		event.setName(dto.getName());
		event.setUrl(dto.getUrl());
		event.setDate(dto.getDate());
		event.setCity(new City(dto.getCityId(), null));
	}

}
