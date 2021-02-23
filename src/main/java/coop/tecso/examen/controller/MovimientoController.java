package coop.tecso.examen.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coop.tecso.examen.dto.MovimientoDto;
import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Movimiento;
import coop.tecso.examen.repository.CuentaCorrienteRepository;
import coop.tecso.examen.repository.MovimientoRepository;
import coop.tecso.examen.service.MovimientoService;

@RestController
@RequestMapping("/movimiento")
public class MovimientoController {

	@Autowired
	private MovimientoRepository movimientoRepository;
	@Autowired
	private CuentaCorrienteRepository cuentaCorrienteRepository;
	@Autowired
	private MovimientoService movimientoService;
	
	@PostMapping("/agregar")
	public ResponseEntity<Serializable> agregar(@RequestBody Movimiento movimiento){
		
		if (movimiento.getId() != null) {
			return new ResponseEntity<Serializable>("El id debe ser nulo", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (movimiento.getCuentaCorriente() == null) {
			return new ResponseEntity<Serializable>("La cuenta proporcionada no debe ser nula", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (movimiento.getCuentaCorriente().getId() == null) {
			return new ResponseEntity<Serializable>("El id de la cuenta proporcionada no debe ser nula", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if ((cuentaCorrienteRepository.existsById(movimiento.getCuentaCorriente().getId())) == false) {
			return new ResponseEntity<Serializable>("No existe la cuenta corriente proporcionada", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (movimiento.getImporte() == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar un importe", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (movimiento.getFecha() == null) {
			return new ResponseEntity<Serializable>("Debe proporcionar una fecha", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		CuentaCorriente cc = cuentaCorrienteRepository.getOne(movimiento.getCuentaCorriente().getId());
		
		Integer descubiertoMaximo = movimientoService.getDescubiertoMaximoSegunMoneda(cc.getMoneda());
		if(descubiertoMaximo == null) {
			return new ResponseEntity<Serializable>("El descubierto supera el limite permitido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Float saldo = cc.getSaldo();
		Float monto = movimiento.getImporte();
		Boolean descubiertoValido = movimientoService.validarDescubierto(saldo, monto, descubiertoMaximo);
		if(descubiertoValido == false) {
			return new ResponseEntity<Serializable>("El descubierto supera el limite permitido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Float saldoFinal = saldo - monto;
		cc.setSaldo(saldoFinal);
		movimiento.setCuentaCorriente(cc);
		
		Movimiento mov = movimientoRepository.save(movimiento);
		cuentaCorrienteRepository.save(cc);
		
		MovimientoDto dto = convertToDto(mov);
		
		return new ResponseEntity<Serializable>(dto, HttpStatus.OK);
	}

	@GetMapping("/listar/{id}")
	public ResponseEntity<Serializable> listar(@PathVariable("id") Long id) {

		if (id == null) {
			return new ResponseEntity<Serializable>("El id de la cuenta corriente es requerido", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		CuentaCorriente cc = cuentaCorrienteRepository.getOne(id);
		
		List<MovimientoDto> result = new ArrayList<>();
		for (Movimiento mov : movimientoService.findByCuentaCorrienteOrderByFecha(cc)) {

			MovimientoDto dto = convertToDto(mov);
			
			result.add(dto);
		}
		
		return new ResponseEntity<Serializable>((Serializable) result, HttpStatus.OK);
	}

	private MovimientoDto convertToDto(Movimiento mov) {
		MovimientoDto dto = new MovimientoDto();
		dto.setId(mov.getId());
		dto.setIsoCode(mov.getIsoCode());
		dto.setDescripcion(mov.getDescripcion());
		dto.setFecha(mov.getFecha());
		dto.setImporte(mov.getImporte());
		dto.setTipoMovimiento(mov.getTipoMovimiento());
		return dto;
	}
}
