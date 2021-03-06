package model;

import java.util.Date;

public class Cliente {
	private String tipoDocumento;
	private String numeroDocumento;
	private String fechaNacimiento;
	private String fechaNacimiento1;
	private String genero;
	private String estadoCivil;
	private String tipoCredito;
	private String convenio;
	private String cargas;

	public Cliente() {
		super();
	}

	public Cliente(String tipoDocumento, String numeroDocumento,
			String fechaNacimiento, String genero, String estadoCivil,
			String tipoCredito, String convenio, String cargas) {
		super();
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.fechaNacimiento = fechaNacimiento;
		this.genero = genero;
		this.estadoCivil = estadoCivil;
		this.tipoCredito = tipoCredito;
		this.convenio = convenio;
		this.cargas = cargas;
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

	public String getFechaNacimiento1() {
		return fechaNacimiento1;
	}

	public void setFechaNacimiento1(String fechaNacimiento1) {
		this.fechaNacimiento1 = fechaNacimiento1;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(String tipoCredito) {
		this.tipoCredito = tipoCredito;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getCargas() {
		return cargas;
	}

	public void setCargas(String cargas) {
		this.cargas = cargas;
	}

}
