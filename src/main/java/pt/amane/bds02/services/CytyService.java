package pt.amane.bds02.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.amane.bds02.dto.CityDTO;
import pt.amane.bds02.entities.City;
import pt.amane.bds02.repositories.CityRepository;
import pt.amane.bds02.services.exceptions.ObjectNotFoundException;

@Service
public class CytyService {

	@Autowired
	private CityRepository repository;

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		Optional<City> objId = repository.findById(id);
		City city = objId.orElseThrow(
				() -> new ObjectNotFoundException("Object not found! Id: " + id + ", Type: " + City.class.getName()));
		return new CityDTO(city);
	}

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> list = repository.findAll();
		return list.stream().map(dto -> new CityDTO(dto)).collect(Collectors.toList());
	}

	@Transactional
	public CityDTO create(CityDTO dto) {
		City city = new City();
		city.setName(dto.getName());
		city = repository.save(city);
		return new CityDTO(city);
	}

}
