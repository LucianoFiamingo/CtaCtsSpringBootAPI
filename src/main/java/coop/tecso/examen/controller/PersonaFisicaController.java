package coop.tecso.examen.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coop.tecso.examen.dto.CuentaCorrienteDto;
import coop.tecso.examen.dto.PersonaFisicaDto;
import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.PersonaFisica;
import coop.tecso.examen.repository.CuentaCorrienteRepository;
import coop.tecso.examen.repository.PersonaFisicaRepository;
import coop.tecso.examen.repository.TitularRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/personaFisica")
public class PersonaFisicaController {

	@Autowired
	private PersonaFisicaRepository personaFisicaRepository;
	@Autowired
	private CuentaCorrienteRepository cuentaCorrienteRepository;
	@Autowired
	private TitularRepository titularRepository;

	@PostMapping("/crear")
	public ResponseEntity<Serializable> crear(@RequestBody PersonaFisica personaFisica) {

		if (personaFisica.getId() != null) {
			return new ResponseEntity<Serializable>("El id debe ser nulo", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaFisica.getRut() == null) {
			return new ResponseEntity<Serializable>("El RUT es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (titularRepository.existsByRut(personaFisica.getRut())) {
			return new ResponseEntity<Serializable>("Ya existe una persona con el mismo RUT", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}


		CuentaCorriente cc = new CuentaCorriente();
		if (personaFisica.getCuentasCorrientes() != null && 
				personaFisica.getCuentasCorrientes().get(0).getNumero() != null) {
			
			if (!cuentaCorrienteRepository.existsByNumero(
					personaFisica.getCuentasCorrientes().get(0).getNumero())) {
				return new ResponseEntity<Serializable>("No existe una cuenta con ese numero", 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			cc = cuentaCorrienteRepository.findByNumero(personaFisica.getCuentasCorrientes().get(0).getNumero());
			
		}
		if(personaFisica.getCuentasCorrientes() == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar una cuenta corriente del titular", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaFisica.getCuentasCorrientes().get(0).getMoneda() == null 
				&& personaFisica.getCuentasCorrientes().get(0).getSaldo() == null 
				&& personaFisica.getCuentasCorrientes().get(0).getNumero() != null) {
			return new ResponseEntity<Serializable>("Complete los datos requeridos", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PersonaFisica per = personaFisicaRepository.save(personaFisica);

		CuentaCorriente ccSave = personaFisica.getCuentasCorrientes().get(0);
		ccSave.setTitular(personaFisica);
		cc = cuentaCorrienteRepository.save(ccSave);
		
		PersonaFisicaDto dto = convertToDto(per);
		CuentaCorrienteDto ccDto = convertToDto(cc);
		List<CuentaCorrienteDto> cuentasCorrientes = new ArrayList<>();
		cuentasCorrientes.add(ccDto);
		dto.setCuentasCorrientes(cuentasCorrientes);
		return new ResponseEntity<Serializable>(dto, HttpStatus.OK);
	}

	@GetMapping("/obtener/{id}")
	public ResponseEntity<Serializable> obtener(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (!personaFisicaRepository.existsById(id)) {
			return new ResponseEntity<Serializable>("No existe ninguna persona con el id proporcionado", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PersonaFisica per = personaFisicaRepository.getOne(id);
		
		PersonaFisicaDto perDto = convertToDto(per);	
		return new ResponseEntity<Serializable>(perDto, HttpStatus.OK);
		
	}

	@PutMapping("/actualizar")
	public ResponseEntity<Serializable> actualizar(@RequestBody PersonaFisica personaFisica) {

		if (personaFisica.getId() == null) {
			return new ResponseEntity<Serializable>(personaFisica, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaFisicaRepository.getOne(personaFisica.getId()) == null) {
			return new ResponseEntity<Serializable>(personaFisica, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		PersonaFisica per = personaFisicaRepository.save(personaFisica);
		return new ResponseEntity<Serializable>(per, HttpStatus.OK);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Serializable> eliminar(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (personaFisicaRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe ninguna persona fisica con el id proporcionado", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PersonaFisica per = personaFisicaRepository.getOne(id);
		personaFisicaRepository.delete(per);
		
		return new ResponseEntity<Serializable>("Persona fisica elimiada correctamente",
				HttpStatus.OK);
		
	}

	@GetMapping("/listar")
	public ResponseEntity<Serializable> listar() {

		List<PersonaFisicaDto> personas = new ArrayList<>();

		for (PersonaFisica per : personaFisicaRepository.findAll()) {

			List<CuentaCorriente> cuentasPorPersona = cuentaCorrienteRepository.findByTitular(per);
			List<CuentaCorrienteDto> cuentasPorPersonaDto = new ArrayList<>();
			
			for (CuentaCorriente cc : cuentasPorPersona) {
				
				CuentaCorrienteDto ccDto = convertToDto(cc);
				cuentasPorPersonaDto.add(ccDto);
			}
			
			PersonaFisicaDto perDto = convertToDto(per, cuentasPorPersonaDto);
			
			personas.add(perDto);
		}
		return new ResponseEntity<Serializable>((Serializable) personas, HttpStatus.OK);
	}
	
	private CuentaCorrienteDto convertToDto(CuentaCorriente cc) {
		CuentaCorrienteDto ccDto = new CuentaCorrienteDto();
		ccDto.setId(cc.getId());
		ccDto.setNumero(cc.getNumero());
		return ccDto;
	}
	
	
	private PersonaFisicaDto convertToDto(PersonaFisica per) {
		PersonaFisicaDto perDto = new PersonaFisicaDto();
		perDto.setId(per.getId());
		perDto.setIsoCode(per.getIsoCode());
		perDto.setRut(per.getRut());
		perDto.setNombre(per.getNombre());
		perDto.setApellido(per.getApellido());
		return perDto;
	}
	
	private PersonaFisicaDto convertToDto(PersonaFisica per, List<CuentaCorrienteDto> ctsDto) {
		PersonaFisicaDto perDto = new PersonaFisicaDto();
		perDto.setId(per.getId());
		perDto.setIsoCode(per.getIsoCode());
		perDto.setRut(per.getRut());
		perDto.setNombre(per.getNombre());
		perDto.setApellido(per.getApellido());
		perDto.setCuentasCorrientes(ctsDto);
		return perDto;
	}
}
