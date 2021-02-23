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
import coop.tecso.examen.dto.PersonaJuridicaDto;
import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.PersonaJuridica;
import coop.tecso.examen.repository.CuentaCorrienteRepository;
import coop.tecso.examen.repository.PersonaJuridicaRepository;
import coop.tecso.examen.repository.TitularRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/personaJuridica")
public class PersonaJuridicaController {

	@Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;
	@Autowired
	private CuentaCorrienteRepository cuentaCorrienteRepository;
	@Autowired
	private TitularRepository titularRepository;

	@PostMapping("/crear")
	public ResponseEntity<Serializable> crear(@RequestBody PersonaJuridica personaJuridica) {

		if (personaJuridica.getId() != null) {
			return new ResponseEntity<Serializable>("El id debe ser nulo", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaJuridica.getRut() == null) {
			return new ResponseEntity<Serializable>("El RUT es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (titularRepository.existsByRut(personaJuridica.getRut())) {
			return new ResponseEntity<Serializable>("Ya existe una persona con el mismo RUT", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		CuentaCorriente cc = new CuentaCorriente();
		if (personaJuridica.getCuentasCorrientes() != null && 
				personaJuridica.getCuentasCorrientes().get(0).getNumero() != null) {
			
			if (!cuentaCorrienteRepository.existsByNumero(
					personaJuridica.getCuentasCorrientes().get(0).getNumero())) {
				return new ResponseEntity<Serializable>("No existe una cuenta con ese numero", 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			cc = cuentaCorrienteRepository.findByNumero(personaJuridica.getCuentasCorrientes().get(0).getNumero());
			
		}
		if(personaJuridica.getCuentasCorrientes() == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar una cuenta corriente del titular", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaJuridica.getCuentasCorrientes().get(0).getMoneda() == null && personaJuridica.getCuentasCorrientes().get(0).getSaldo() == null && 
				personaJuridica.getCuentasCorrientes().get(0).getNumero() != null) {
			return new ResponseEntity<Serializable>("Complete los datos requeridos", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PersonaJuridica per = personaJuridicaRepository.save(personaJuridica);
		
		CuentaCorriente ccSave = personaJuridica.getCuentasCorrientes().get(0);
		ccSave.setTitular(personaJuridica);
		cc = cuentaCorrienteRepository.save(ccSave);
		
		List<CuentaCorrienteDto> cuentasCorrientes = new ArrayList<>();
		CuentaCorrienteDto ccDto = convertToDto(cc);
		cuentasCorrientes.add(ccDto);
		
		PersonaJuridicaDto dto = convertToDto(per, cuentasCorrientes);
		
		return new ResponseEntity<Serializable>(dto, HttpStatus.OK);
	}

	@GetMapping("/obtener/{id}")
	public ResponseEntity<Serializable> obtener(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaJuridicaRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe ninguna persona juridica con el id proporcionado", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		PersonaJuridica per = personaJuridicaRepository.getOne(id);
		
		PersonaJuridicaDto perDto = convertToDto(per);
		return new ResponseEntity<Serializable>(perDto, HttpStatus.OK);
		
	}

	@PutMapping("/actualizar")
	public ResponseEntity<Serializable> actualizar(@RequestBody PersonaJuridica personaJuridica) {

		if (personaJuridica.getId() == null) {
			return new ResponseEntity<Serializable>("El id es requerido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (personaJuridicaRepository.existsById(personaJuridica.getId()) == false) {
			return new ResponseEntity<Serializable>("No existe la persona proporcionada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		personaJuridica.setCuentasCorrientes(cuentaCorrienteRepository.findByTitular(personaJuridica));
		PersonaJuridica per = personaJuridicaRepository.save(personaJuridica);
		
		PersonaJuridicaDto dto = convertToDto(per, convertToDto(per.getCuentasCorrientes()));
		return new ResponseEntity<Serializable>(dto, HttpStatus.OK);
	}


	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Serializable> eliminar(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (personaJuridicaRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe ninguna persona fisica con el id proporcionado", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		PersonaJuridica per = personaJuridicaRepository.getOne(id);
		personaJuridicaRepository.delete(per);
		
		return new ResponseEntity<Serializable>("Persona fisica elimiada correctamente",
				HttpStatus.OK);
		
	}

	@GetMapping("/listar")
	public ResponseEntity<Serializable> listar() {

		List<PersonaJuridicaDto> personas = new ArrayList<>();

		for (PersonaJuridica per : personaJuridicaRepository.findAll()) {

			List<CuentaCorriente> cuentasPorPersona = cuentaCorrienteRepository.findByTitular(per);
			List<CuentaCorrienteDto> cuentasPorPersonaDto = new ArrayList<>();
			
			for (CuentaCorriente cc : cuentasPorPersona) {
				
				CuentaCorrienteDto ccDto = convertToDto(cc);
				cuentasPorPersonaDto.add(ccDto);
			}
			
			PersonaJuridicaDto perDto = convertToDto(per, cuentasPorPersonaDto);
			
			personas.add(perDto);
		}
		return new ResponseEntity<Serializable>((Serializable) personas, HttpStatus.OK);
	}
	
	private CuentaCorrienteDto convertToDto(CuentaCorriente cc) {
		CuentaCorrienteDto ccDto = new CuentaCorrienteDto();
		ccDto.setId(cc.getId());
		ccDto.setNumero(cc.getNumero());
		ccDto.setSaldo(cc.getSaldo());
		ccDto.setMoneda(cc.getMoneda());
		return ccDto;
	}
	
	private PersonaJuridicaDto convertToDto(PersonaJuridica per) {
		PersonaJuridicaDto perDto = new PersonaJuridicaDto();
		perDto.setId(per.getId());
		perDto.setIsoCode(per.getIsoCode());
		perDto.setRut(per.getRut());
		perDto.setRazonSocial(per.getRazonSocial());
		perDto.setAnioFundacion(per.getAnioFundacion());
		return perDto;
	}
	
	private PersonaJuridicaDto convertToDto(PersonaJuridica per, List<CuentaCorrienteDto> ctsDto) {
		PersonaJuridicaDto perDto = new PersonaJuridicaDto();
		perDto.setId(per.getId());
		perDto.setIsoCode(per.getIsoCode());
		perDto.setRut(per.getRut());
		perDto.setRazonSocial(per.getRazonSocial());
		perDto.setAnioFundacion(per.getAnioFundacion());
		perDto.setCuentasCorrientes(ctsDto);
		return perDto;
	}
	
	private List<CuentaCorrienteDto> convertToDto(List<CuentaCorriente> cuentasCorrientes) {
		List<CuentaCorrienteDto> cuentasDto = new ArrayList<CuentaCorrienteDto>();
		for (CuentaCorriente cc : cuentasCorrientes) {
			CuentaCorrienteDto ccDto = convertToDto(cc);
			cuentasDto.add(ccDto);
		}
		return cuentasDto;
	}
}
