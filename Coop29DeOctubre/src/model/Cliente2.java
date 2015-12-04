package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("cliente")
public class Cliente2 {

	@XStreamAlias("tipoDocumento")
	private String tipoDocumento;
	@XStreamAlias("numeroDocumento")
	private String numeroDocumento;
	@XStreamAlias("fechaNacimiento")
	private String fechaNacimiento;
	@XStreamAlias("genero")
	private String genero;
	@XStreamAlias("estadoCivil")
	private String estadoCivil;
	@XStreamAlias("tipoCredito")
	private String tipoCredito;
	@XStreamAlias("convenio")
	private String convenio;
	@XStreamAlias("cargas")
	private String cargas;

	public Cliente2() {
		super();
	}

	public Cliente2(String tipoDocumento, String numeroDocumento,
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

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
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
