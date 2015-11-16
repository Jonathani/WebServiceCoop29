package controller;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.swing.JOptionPane;
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

import model.Cliente;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

@ManagedBean
@ViewScoped
public class ClienteBean {

	private Cliente cliente;
	private DateFormat formatoFecha;
	private boolean mostrarConsulta;
	private String respuesta;

	public ClienteBean() {
		super();
		cliente = new Cliente();
		formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	}

	@PostConstruct
	public void postConstructClientesBean() {
		mostrarConsulta = false;
	}

	public String consulta() throws Exception {

		// ClienteBean client = new ClienteBean();
		// Mensaje2
		String[] variables2 = { "tipoDocumento", "numeroDocumento",
				"fechaNacimiento", "genero", "EstadoCivil", "TipoCredito",
				"Convenio", "Cargas" };
		// Object[] values2 = { this.tipoDocumento, this.numeroDocumento,
		// this.fechaNacimiento, this.genero, this.estadoCivil,
		// this.tipoCredito, this.convenio, this.cargas };

		Object[] values2 = { cliente.getTipoDocumento(),
				cliente.getNumeroDocumento(),
				formatoFecha.format(cliente.getFechaNacimiento()),
				cliente.getGenero(), cliente.getEstadoCivil(),
				cliente.getTipoCredito(), cliente.getConvenio(),
				cliente.getCargas() };
		// Object[] values2 = { "C", "1718297383", "1989-10-02", "M", "S",
		// "C", "S", "N" };

		SOAPMessage inputMessage2;
		inputMessage2 = createSOAPRequest("ObtenerReporte29deOctubre",
				variables2, values2);
		String response = sendMessage(inputMessage2);
		respuesta = format(response);
		System.out.println(respuesta);
		//JOptionPane.showMessageDialog(null, respuesta);
		// System.out.println(response);
		//
		return respuesta;
	}

	public void prueba() {
		mostrarConsulta = true;
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
			// e.printStackTrace();
			// return e.getMessage();
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

	public boolean isMostrarConsulta() {
		return mostrarConsulta;
	}

	public void setMostrarConsulta(boolean mostrarConsulta) {
		this.mostrarConsulta = mostrarConsulta;
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

}
