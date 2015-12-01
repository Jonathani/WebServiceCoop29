package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import model.Cliente;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
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
	private String respuesta;
	private String tipoDocumento;
	private String numeroDocumento;
	private String fechaNacimiento;
	private String genero;
	private String estadoCivil;
	private String tipoCredito;
	private String convenio;
	private String cargas;

	public ClienteBean() {
		super();
		cliente = new Cliente();
		formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	}

	public String consulta() throws Exception {

		SAXBuilder builder = new SAXBuilder();
		File filexml = new File("datos.xml");

		// Se crea el documento a traves del archivo
		Document document = (Document) builder.build(filexml);

		// Se obtiene la raiz 'tables'
		Element rootNode = document.getRootElement();

		// Se obtiene la lista de hijos de la raiz 'tables'
		List list = rootNode.getChildren("cliente");

		for (int i = 0; i < list.size(); i++) {

			// Se obtiene el elemento 'cliente'
			Element cliente = (Element) list.get(i);

			// Se obtiene el atributo 'nombre' que esta en el tag 'cliente'
			String nombreTabla = cliente.getAttributeValue("nombre");

			System.out.println("Cliente No : " + nombreTabla);

			// Se obtiene la lista de hijos del tag 'cliente'
			List lista_campos = cliente.getChildren();

			// Se recorre la lista de campos
			for (int j = 0; j < lista_campos.size(); j++) {
				// Se obtiene el elemento 'campo'
				Element campo = (Element) lista_campos.get(j);

				this.tipoDocumento = campo.getChildTextTrim("tipoDocumento");

				this.numeroDocumento = campo.getChildTextTrim("cedulaClie");

				this.fechaNacimiento = campo
						.getChildTextTrim("fechaNacimiento");

				this.genero = campo.getChildTextTrim("genero");

				this.estadoCivil = campo.getChildTextTrim("estadoCivil");

				this.tipoCredito = campo.getChildTextTrim("tipoCredito");

				this.convenio = campo.getChildTextTrim("convenio");

				this.cargas = campo.getChildTextTrim("cargas");

			}
		}

		// ClienteBean bean = new ClienteBean();

		String[] variables2 = { "tipoDocumento", "numeroDocumento",
				"fechaNacimiento", "genero", "EstadoCivil", "TipoCredito",
				"Convenio", "Cargas" };

		Object[] values2 = { this.tipoDocumento, this.numeroDocumento,
				this.fechaNacimiento, this.genero, this.estadoCivil,
				this.tipoCredito, this.convenio, this.cargas };
		// Object[] values2 = { "C", "1718297383", "1989-10-02", "M", "S", "C",
		// "S", "N" };

		SOAPMessage inputMessage2;
		inputMessage2 = createSOAPRequest("ObtenerReporte29deOctubre",
				variables2, values2);
		String response = sendMessage(inputMessage2);
		respuesta = format(response);
		System.out.println(respuesta);
		System.out.println(format(response));
		return respuesta;
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

}
