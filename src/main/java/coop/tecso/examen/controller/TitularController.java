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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coop.tecso.examen.dto.CuentaCorrienteDto;
import coop.tecso.examen.dto.TitularDto;
import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Titular;
import coop.tecso.examen.repository.CuentaCorrienteRepository;
import coop.tecso.examen.repository.TitularRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/titular")
public class TitularController {

	@Autowired
	private TitularRepository titularRepository;	
	@Autowired
	private CuentaCorrienteRepository cuentaCorrienteRepository;

	@GetMapping("/obtener/{id}")
	public ResponseEntity<Serializable> obtener(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (titularRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe ningun titular con el id proporcionado", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Titular t = titularRepository.getOne(id);
		
		Titular tDto = new Titular();
		tDto.setId(t.getId());
		tDto.setRut(t.getRut());
		
		return new ResponseEntity<Serializable>(tDto, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Serializable> eliminar(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (titularRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe titular con el id proporcionado", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Titular t = titularRepository.getOne(id);
		cuentaCorrienteRepository.deleteAll(t.getCuentasCorrientes());
		titularRepository.delete(t);
		
		return new ResponseEntity<Serializable>("Titular elimiado correctamente",
				HttpStatus.OK);
		
	}
	
	@GetMapping("/listar")
	public ResponseEntity<Serializable> listar() {
		
		List<TitularDto> titularesDto = new ArrayList<>();

		for (Titular t : titularRepository.findAll()) {

			List<CuentaCorriente> cuentasPorTitular = cuentaCorrienteRepository.findByTitular(t);
			List<CuentaCorrienteDto> cuentasPorTitularDto = new ArrayList<>();
			
			for (CuentaCorriente cc : cuentasPorTitular) {
				CuentaCorrienteDto ccDto = convertToDto(cc);
				cuentasPorTitularDto.add(ccDto);
			}
			TitularDto dto = convertToDto(t, cuentasPorTitularDto);
			titularesDto.add(dto);
		}
		return new ResponseEntity<Serializable>((Serializable) titularesDto, HttpStatus.OK);
	}
	
	@GetMapping("/listar/conCuentas")
	public ResponseEntity<Serializable> listarConCuenta() {
		
		List<TitularDto> titularesDto = new ArrayList<>();

		for (Titular t : titularRepository.findAllByCuentasCorrientesNotNull()) {
			
			List<CuentaCorriente> cuentasPorTitular = cuentaCorrienteRepository.findByTitular(t);
			List<CuentaCorrienteDto> cuentasPorTitularDto = new ArrayList<>();
			
			for (CuentaCorriente cc : cuentasPorTitular) {
				
				CuentaCorrienteDto ccDto = convertToDto(cc);
				cuentasPorTitularDto.add(ccDto);
			}
			
			TitularDto dto = convertToDto(t, cuentasPorTitularDto);
			titularesDto.add(dto);
		}
		return new ResponseEntity<Serializable>((Serializable) titularesDto, HttpStatus.OK);
	}
	
	private CuentaCorrienteDto convertToDto(CuentaCorriente cc) {
		CuentaCorrienteDto ccDto = new CuentaCorrienteDto();
		ccDto.setId(cc.getId());
		ccDto.setNumero(cc.getNumero());
		return ccDto;
	}
	
	@SuppressWarnings("unused")
	private TitularDto convertToDto(Titular t) {
		TitularDto tDto = new TitularDto();
		tDto.setId(t.getId());
		tDto.setRut(t.getRut());
		return tDto;
	}
	
	private TitularDto convertToDto(Titular t, List<CuentaCorrienteDto> ctsDto) {
		TitularDto tDto = new TitularDto();
		tDto.setId(t.getId());
		tDto.setRut(t.getRut());
		tDto.setCuentasCorrientes(ctsDto);
		return tDto;
	}
}
