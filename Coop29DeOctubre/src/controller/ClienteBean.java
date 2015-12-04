package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Cliente;
import model.Cliente2;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;

@ManagedBean
@ViewScoped
public class ClienteBean {

	private Cliente cliente;
	private DateFormat formatoFecha;
	private String respuesta;
	private String tipoDocumento;
	private String numeroDocumento;
	private String fechaNacimiento;
	private String genero;
	private String estadoCivil;
	private String tipoCredito;
	private String convenio;
	private String cargas;
	private String cedulaAux;

	public ClienteBean() {
		super();
		cliente = new Cliente();
		formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	}

	public String consulta() throws Exception {

		XStream xstream = new XStream();
		Cliente cliente;
		cliente = new Cliente("C", "1718297383", "1989-10-02", "M", "S", "C",
				"S", "N");
		xstream.alias("cliente", Cliente.class);

		String xml = xstream.toXML(cliente);

		XStream xStream = new XStream();
		xStream.processAnnotations(Cliente2.class);
		xstream.alias("tipoDocumento", String.class);
		xstream.alias("numeroDocumento", String.class);
		xstream.alias("fechaNacimiento", String.class);
		xstream.alias("genero", String.class);
		xstream.alias("estadoCivil", String.class);
		xstream.alias("tipoCredito", String.class);
		xstream.alias("convenio", String.class);
		xstream.alias("cargas", String.class);

		Cliente2 clienteObject = (Cliente2) xStream.fromXML(xml);
		this.cedulaAux = clienteObject.getNumeroDocumento();

		File file;
		FileReader fileReader;
		BufferedReader br;
		String leer;

		file = new File("C:/Users/Fernando/Desktop/Buro/Consultas/Buro" + "["
				+ this.cedulaAux + "]" + ".xml");
		long dias = diasTranscurridos();

		if (file.exists() && file.isFile() || file.length() > 0
				|| file.canRead()) {
			if (dias <= 45) {
				fileReader = new FileReader(file);
				br = new BufferedReader(fileReader);
				while ((leer = br.readLine()) != null) {
					System.out.println("Ya existe");
					System.out.println(diasTranscurridos());
				}
				br.close();
				return "Ya existe";
			} else {
				file.delete();
				String[] variables2 = { "tipoDocumento", "numeroDocumento",
						"fechaNacimiento", "genero", "EstadoCivil",
						"TipoCredito", "Convenio", "Cargas" };

				Object[] values2 = { clienteObject.getTipoDocumento(),
						clienteObject.getNumeroDocumento(),
						clienteObject.getFechaNacimiento(),
						clienteObject.getGenero(),
						clienteObject.getEstadoCivil(),
						clienteObject.getTipoCredito(),
						clienteObject.getConvenio(), clienteObject.getCargas() };
				// Object[] values2 = { "C", "1718297383", "1989-10-02", "M",
				// "S",
				// "C",
				// "S", "N" };

				SOAPMessage inputMessage2;
				inputMessage2 = createSOAPRequest("ObtenerReporte29deOctubre",
						variables2, values2);
				String response = sendMessage(inputMessage2);
				respuesta = format(response);
				System.out.println(respuesta);
				guardarArchivo();
				System.out.println(format(response));
				return respuesta;
			}

		} else {
			String[] variables2 = { "tipoDocumento", "numeroDocumento",
					"fechaNacimiento", "genero", "EstadoCivil", "TipoCredito",
					"Convenio", "Cargas" };

			Object[] values2 = { clienteObject.getTipoDocumento(),
					clienteObject.getNumeroDocumento(),
					clienteObject.getFechaNacimiento(),
					clienteObject.getGenero(), clienteObject.getEstadoCivil(),
					clienteObject.getTipoCredito(),
					clienteObject.getConvenio(), clienteObject.getCargas() };
			// Object[] values2 = { "C", "1718297383", "1989-10-02", "M",
			// "S",
			// "C",
			// "S", "N" };

			SOAPMessage inputMessage2;
			inputMessage2 = createSOAPRequest("ObtenerReporte29deOctubre",
					variables2, values2);
			String response = sendMessage(inputMessage2);
			respuesta = format(response);
			System.out.println(respuesta);
			guardarArchivo();
			System.out.println(format(response));
			return respuesta;
		}

	}

	public long diasTranscurridos() {

		File file;
		file = new File("C:/Users/Fernando/Desktop/Buro/Consultas/Buro" + "["
				+ this.cedulaAux + "]" + ".xml");

		long ms = file.lastModified();

		Date fechaCreacion = new Date(ms);
		Calendar c = new GregorianCalendar();
		c.setTime(fechaCreacion);

		int diaCreacion, mesCreacion, annioCreacion;

		diaCreacion = c.get(Calendar.DATE);
		mesCreacion = c.get(Calendar.MONTH);
		annioCreacion = c.get(Calendar.YEAR);

		Date fechaActual = new Date();
		Calendar a = new GregorianCalendar();
		a.setTime(fechaActual);

		int diaActual, mesActual, annioActual;
		diaActual = a.get(Calendar.DATE);
		mesActual = a.get(Calendar.MONTH);
		annioActual = a.get(Calendar.YEAR);

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		// Establecer las fechas
		cal1.set(annioCreacion, mesCreacion, diaCreacion);
		cal2.set(annioActual, mesActual, diaActual);

		// conseguir la representacion de la fecha en milisegundos
		long milis1 = cal1.getTimeInMillis();
		long milis2 = cal2.getTimeInMillis();

		// calcular la diferencia en milisengundos
		long diff = milis2 - milis1;

		// calcular la diferencia en dias
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;
	}

	public void guardarArchivo() throws FileNotFoundException, IOException {
		Date fechaActual = new Date();
		DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		File file;
		FileWriter fileWriter;

		file = new File("C:/Users/Fernando/Desktop/Buro/Consultas/Buro" + "["
				+ this.cedulaAux + "]" + ".xml");
		file.delete();
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write("<fechaConsulta>"
					+ formatoFecha.format(fechaActual) + "</fechaConsulta>"
					+ "\n" + this.respuesta);
			fileWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(ClienteBean.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public static Date fechaFormato(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			System.out.println(fechaDate.toString());
			return fechaDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}

	public String format(String xml) {

		try {
			System.out.println("paso1");
			final InputSource src = new InputSource(new StringReader(xml));
			System.out.println("paso2");
			final Node document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(src).getDocumentElement();
			System.out.println("paso3");
			final Boolean keepDeclaration = Boolean.valueOf(xml
					.startsWith("<?xml"));
			System.out.println("paso4");
			// May need this:
			System.setProperty(DOMImplementationRegistry.PROPERTY,
					"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");
			System.out.println("paso5");
			final DOMImplementationRegistry registry = DOMImplementationRegistry
					.newInstance();
			System.out.println("paso6");
			final DOMImplementationLS impl = (DOMImplementationLS) registry
					.getDOMImplementation("LS");
			System.out.println("paso7");
			final LSSerializer writer = impl.createLSSerializer();
			System.out.println("paso8");
			writer.getDomConfig().setParameter("format-pretty-print",
					Boolean.TRUE);
			System.out.println("paso9");// Set this to true if the output needs
										// to be
			// beautified.
			writer.getDomConfig().setParameter("xml-declaration",
					keepDeclaration); // Set this to true if the declaration is
										// needed to be outputted.
			return writer.writeToString(document);
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	public String sendMessage(SOAPMessage inputMessage) throws Exception {
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory
				.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory
				.createConnection();

		// Send SOAP Message to SOAP Server
		String url = "http://190.216.105.232/29OctubreWSMR/wsCoop29deOctubre.asmx";
		SOAPMessage soapResponse = soapConnection.call(inputMessage, url);

		// print SOAP Response
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		String strMsg = new String(out.toByteArray());
		soapConnection.close();
		System.out.print("Response SOAP Message:");
		System.out.println(strMsg);
		return strMsg;
	}

	public SOAPMessage createSOAPRequest(String methodName, String[] variables,
			Object[] values) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		String prefix = "tem";
		String serverURI = "http://tempuri.org/";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(prefix, serverURI);
		// SOAP HEADER
		SOAPHeader header = envelope.getHeader();
		SOAPElement soapHeader = header.addChildElement("CabeceraCR", prefix);
		SOAPElement soapBodyUser = soapHeader
				.addChildElement("Usuario", prefix);
		soapBodyUser.addTextNode("ws29Octubretest");
		SOAPElement soapBodyPassword = soapHeader.addChildElement("Clave",
				prefix);
		soapBodyPassword.addTextNode("burocr");
		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElemFirst = soapBody.addChildElement(methodName,
				prefix);
		for (int i = 0; i < variables.length; i++) {
			SOAPElement soapBodyElem = soapBodyElemFirst.addChildElement(
					variables[i], prefix);
			if (values[i].getClass().equals(Integer.class)
					|| values[i].getClass().equals(int.class)) {
				soapBodyElem.addTextNode(Integer.toString((Integer) values[i]));
			}
			if (values[i].getClass().equals(Byte.class)
					|| values[i].getClass().equals(byte.class)) {
				soapBodyElem.addTextNode(Byte.toString((Byte) values[i]));
			}
			if (values[i].getClass().equals(byte[].class)) {
				String base64 = DatatypeConverter
						.printBase64Binary((byte[]) values[i]);
				soapBodyElem.addTextNode(base64);
			}
			if (values[i].getClass().equals(String.class)) {
				soapBodyElem.addTextNode((String) values[i]);
			}
		}

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI + methodName);
		soapMessage.saveChanges();
		/* Print the request message */
		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();
		return soapMessage;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DateFormat getFormatoFecha() {
		return formatoFecha;
	}

	public void setFormatoFecha(DateFormat formatoFecha) {
		this.formatoFecha = formatoFecha;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getCedulaAux() {
		return cedulaAux;
	}

	public void setCedulaAux(String cedulaAux) {
		this.cedulaAux = cedulaAux;
	}

}
