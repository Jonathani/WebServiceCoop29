package model;

import java.util.Date;

public class Cliente {
	private String tipoDocumento;
	private String numeroDocumento;
	private Date fechaNacimiento;
	private String fechaNacimiento1;
	private String genero;
	private String EstadoCivil;
	private String TipoCredito;
	private String Convenio;
	private String Cargas;

	public Cliente() {
		super();
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEstadoCivil() {
		return EstadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		EstadoCivil = estadoCivil;
	}

	public String getTipoCredito() {
		return TipoCredito;
	}

	public void setTipoCredito(String tipoCredito) {
		TipoCredito = tipoCredito;
	}

	public String getConvenio() {
		return Convenio;
	}

	public void setConvenio(String convenio) {
		Convenio = convenio;
	}

	public String getCargas() {
		return Cargas;
	}

	public void setCargas(String cargas) {
		Cargas = cargas;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaNacimiento1() {
		return fechaNacimiento1;
	}

	public void setFechaNacimiento1(String fechaNacimiento1) {
		this.fechaNacimiento1 = fechaNacimiento1;
	}

}
