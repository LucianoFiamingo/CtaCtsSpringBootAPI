package coop.tecso.examen.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coop.tecso.examen.dto.CuentaCorrienteDto;
import coop.tecso.examen.dto.TitularDto;
import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Titular;
import coop.tecso.examen.repository.CuentaCorrienteRepository;
import coop.tecso.examen.repository.MovimientoRepository;
import coop.tecso.examen.repository.TitularRepository;

@RestController
@RequestMapping("/cuentaCorriente")
@CrossOrigin(origins = "http://localhost:4200")
public class CuentaCorrienteController {

	@Autowired
	private CuentaCorrienteRepository cuentaCorrienteRepository;
	@Autowired
	private MovimientoRepository movimientoRepository;
	@Autowired
	private TitularRepository titularRepository;
	
	@PostMapping("/crear")
	public ResponseEntity<Serializable> crear(@Valid @RequestBody CuentaCorriente cuentaCorriente) {

		if (cuentaCorriente.getId() != null) {
			return new ResponseEntity<Serializable>("El id debe ser nulo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cuentaCorriente.getMoneda() == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar una moneda", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (!cuentaCorriente.getMoneda().equals("PESOS") &&
			!cuentaCorriente.getMoneda().equals("DOLAR") &&
			!cuentaCorriente.getMoneda().equals("EURO")) {
			return new ResponseEntity<Serializable>("Moneda incorrecta", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Titular t = new Titular();
		if (cuentaCorriente.getTitular() != null && cuentaCorriente.getTitular().getId() != null) {
			
			if (!titularRepository.existsById(cuentaCorriente.getTitular().getId())) {
				return new ResponseEntity<Serializable>("No existe un titular con ese id", 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			t = titularRepository.getOne(cuentaCorriente.getTitular().getId());
			
		}
		if (titularRepository.existsByRut(cuentaCorriente.getTitular().getRut())) {
			return new ResponseEntity<Serializable>("Ya existe un titular con el mismo RUT", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			t = titularRepository.save(cuentaCorriente.getTitular());
		}
		
		cuentaCorriente.setTitular(t);
		CuentaCorriente cc = cuentaCorrienteRepository.save(cuentaCorriente);
		
		TitularDto tDto = convertTo(t);
		CuentaCorrienteDto ccDto = convertToDto(cc, tDto);
		return new ResponseEntity<Serializable>(ccDto, HttpStatus.OK);
		
	}

	@GetMapping("/eliminar/{id}")
	public ResponseEntity<Serializable> eliminar(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar el id de la cuenta", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cuentaCorrienteRepository.existsById(id) == false) {
			return new ResponseEntity<Serializable>("No existe una cuenta con el id proporcionado", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		CuentaCorriente cc = cuentaCorrienteRepository.getOne(id);
		
		if (movimientoRepository.existsByCuentaCorriente(cc)) {
			return new ResponseEntity<Serializable>("No puede eliminarse la cuenta corriente debido a que la misma tiene movimientos realizados", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		cuentaCorrienteRepository.delete(cc);
		return new ResponseEntity<Serializable>("Cuenta eliminada correctamente", HttpStatus.OK);
	}

	@GetMapping("/listar")
	public ResponseEntity<Serializable> listar() {

		List<CuentaCorrienteDto> result = new ArrayList<>();

		for (CuentaCorriente cc : cuentaCorrienteRepository.findAll()) {
			
			TitularDto tDto = convertTo(cc.getTitular());
			CuentaCorrienteDto dto = convertToDto(cc, tDto);
			result.add(dto);
		}
		return new ResponseEntity<Serializable>((Serializable) result, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	private CuentaCorrienteDto convertToDto(CuentaCorriente cc) {
		CuentaCorrienteDto dto = new CuentaCorrienteDto();
		dto.setId(cc.getId());
		dto.setIsoCode(cc.getIsoCode());
		dto.setNumero(cc.getNumero());
		dto.setMoneda(cc.getMoneda());
		dto.setSaldo(cc.getSaldo());
		return dto;
	}
	
	private CuentaCorrienteDto convertToDto(CuentaCorriente cc, TitularDto titular) {
		CuentaCorrienteDto dto = new CuentaCorrienteDto();
		dto.setId(cc.getId());
		dto.setIsoCode(cc.getIsoCode());
		dto.setNumero(cc.getNumero());
		dto.setMoneda(cc.getMoneda());
		dto.setSaldo(cc.getSaldo());
		dto.setTitular(titular);
		return dto;
	}
	
	private TitularDto convertTo(Titular t) {
		TitularDto tDto = new TitularDto();
		tDto.setId(t.getId());
		tDto.setRut(t.getRut());
		return tDto;
	}
}
