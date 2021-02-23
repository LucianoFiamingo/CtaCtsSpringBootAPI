package coop.tecso.examen.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coop.tecso.examen.model.CuentaCorriente;
import coop.tecso.examen.model.Movimiento;
import coop.tecso.examen.repository.MovimientoRepository;
import coop.tecso.examen.service.MovimientoService;

@Service
public class MovimientoServiceImpl implements MovimientoService {

	@Autowired
	private MovimientoRepository movimientoRepository;

	@Override
	public List<Movimiento> findByCuentaCorriente(CuentaCorriente cuentaCorriente) {
		return movimientoRepository.findByCuentaCorriente(cuentaCorriente);
	}	

	@Override
	public List<Movimiento> findByCuentaCorrienteOrderByFecha(CuentaCorriente cuentaCorriente) {
		return movimientoRepository.findByCuentaCorrienteOrderByFecha(cuentaCorriente);
	}
	
	@Override
	public Integer getDescubiertoMaximoSegunMoneda(String moneda) {

		Integer descubiertoMaximo = null;
		switch (moneda) {
		case "PESOS":
			descubiertoMaximo = 1000;
			break;
		case "DOLAR":
			descubiertoMaximo = 300;
			break;
		case "EURO":
			descubiertoMaximo = 150;
			break;
		default:
			return descubiertoMaximo;
		}
		return descubiertoMaximo;

	}

	@Override
	public Boolean validarDescubierto(Float saldo, Float monto, Integer descubiertoMaximo) {
		
		if((saldo - monto)*-1 > descubiertoMaximo) {
				return false;
		}
		return true;
	}

	@Override
	public List<Movimiento> OrdenarListaMovimientosPorFechaDesc(List<Movimiento> listMov) {
		
		Comparator<Movimiento> compareByDate = (Movimiento o1, Movimiento o2) ->
        o2.getFecha().compareTo( o1.getFecha());
        
		listMov.sort(compareByDate);
		
		return listMov;
	}
	
}
