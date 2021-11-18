package com.client.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

public class TestSoapClient {

	static {
		System.setProperty("javax.net.ssl.trustStore", TestSoapClient.class.getClassLoader().getResource("ePay5Client.jks").getFile());
		System.setProperty("javax.net.ssl.trustStorePassword", "Change1$$");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
	}

	private static final String SOAP_ENVELOP_PREFIX = "soapenv";

	private static final String EPAY_ENVELOP_PREFIX = "epay";
	private static final String EPAY_NAMESPACE = "http://dsg.dubai.gov.ae/schema/epay";

	private static final String WSSE_ENVELOP_PREFIX = "wsse";
	private static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	private static final String WSU_ENVELOP_PREFIX = "wsu";
	private static final String WSU_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

	private static final String SOAP_URL = "https://epayment.qa.dubai.ae/ePayHub/processRequestAPI";
	private static final String SOAP_ACTION = "http://dsg.dubai.gov.ae/ws/epay/echo";

	public static void main(String[] args) throws Exception {

		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.mmm'Z'").format(new Date());

		SOAPMessage message = MessageFactory.newInstance().createMessage();

		SOAPEnvelope envelope = createEpaySoapEnvelope(message);

		SOAPHeader header = envelope.getHeader();
		header.addNamespaceDeclaration(WSSE_ENVELOP_PREFIX, WSSE_NAMESPACE);
		header.setPrefix(SOAP_ENVELOP_PREFIX);

		SOAPElement timestamp = addWsuElement(addWsseElement(header, "Security"), "Timestamp");
		addWsuElement(timestamp, "Created").setValue(currentTimestamp);
		addWsuElement(timestamp, "Expires").setValue(currentTimestamp);

		SOAPBody body = envelope.getBody();
		body.setPrefix(SOAP_ENVELOP_PREFIX);

		SOAPElement generateTransactionToken = addEpayElement(body, "generateTransactionToken");

		TransactionInfo transactionInfo = new TransactionInfo();
		transactionInfo.setSpCode("ZFND");
		transactionInfo.setServCode("zakat");
		transactionInfo.setSptrn("2856820087");
		transactionInfo.setAmount("10.00");
		transactionInfo.setTimestamp("2021-11-14T08:22:32.006+05:00");
		transactionInfo.setDescription("Test Transaction");
		transactionInfo.setType("sale");
		transactionInfo.setVersionCode("2.1");
		transactionInfo.setPaymentChannel("100");
		setTransactionInfoTo(addEpayElement(generateTransactionToken, "transactionInfo"), transactionInfo);

		UserInfo userInfo = new UserInfo();
		userInfo.setIsAuthenticated("false");
		userInfo.setUserId("32396");
		userInfo.setUserName("tester001");
		userInfo.setFullNameEn("ing-e ca VanNister");
		userInfo.setFullNameAr("first mid last");
		userInfo.setMobileNo("97150332968");
		userInfo.setEmail("tstuser0@rta.com");
		userInfo.setEmirateCode("DXB");
		userInfo.setPoBox("PO");
		setUserInfoTo(addEpayElement(generateTransactionToken, "userInfo"), userInfo);

		setSoapAction(SOAP_ACTION, message);
		message.writeTo(System.out);
		System.out.println("\n\n\n\n\n");
		secureIt();
		invokeSoapService(SOAP_URL, message).writeTo(System.out);

	}

	private static void setTransactionInfoTo(SOAPElement soapElement, TransactionInfo transactionInfo)
			throws Exception {

		for (Map.Entry<String, String> entry : transactionInfo.toMap().entrySet()) {
			if (!entry.equals(null)) {
				addEpayElement(soapElement, entry.getKey()).setValue(entry.getValue());
			}
		}
	}

	private static void setUserInfoTo(SOAPElement soapElement, UserInfo userInfo) throws Exception {

		for (Map.Entry<String, String> entry : userInfo.toMap().entrySet()) {
			if (!entry.equals(null)) {
				addEpayElement(soapElement, entry.getKey()).setValue(entry.getValue());
			}
		}
	}

	private static SOAPEnvelope createEpaySoapEnvelope(SOAPMessage message) throws Exception {
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		envelope.removeNamespaceDeclaration(envelope.getPrefix());
		envelope.setPrefix(SOAP_ENVELOP_PREFIX);
		envelope.addNamespaceDeclaration(EPAY_ENVELOP_PREFIX, EPAY_NAMESPACE);
		return envelope;
	}

	private static SOAPElement addEpayElement(SOAPElement soapElement, String localPartName) throws Exception {
		return soapElement.addChildElement(new QName(EPAY_NAMESPACE, localPartName, EPAY_ENVELOP_PREFIX));
	}

	private static SOAPElement addWsseElement(SOAPElement soapElement, String localPartName) throws Exception {
		return soapElement.addChildElement(new QName(WSSE_NAMESPACE, localPartName, WSSE_ENVELOP_PREFIX));
	}

	private static SOAPElement addWsuElement(SOAPElement soapElement, String localPartName) throws Exception {
		return soapElement.addChildElement(new QName(WSU_NAMESPACE, localPartName, WSU_ENVELOP_PREFIX));
	}

	private static SOAPMessage invokeSoapService(String url, SOAPMessage message) throws Exception {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = soapConnectionFactory.createConnection();
		java.net.URL endpoint = new URL(url);
		return connection.call(message, endpoint);
	}

	private static void setSoapAction(String action, SOAPMessage message) {
		message.getMimeHeaders().addHeader("SOAPAction", action);
	}

	private static void secureIt() throws Exception {
		// SSL Keystore
		File pKeyFile = new File(TestSoapClient.class.getClassLoader().getResource("ePay5Client-pkcs12.jks").getFile());
		String pKeyPassword = "123456";
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream keyInput = new FileInputStream(pKeyFile);
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

		// Set ssl context with private key and truststore details
		SSLContext sc = SSLContext.getInstance("TLSv1.2");
		sc.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
		SSLSocketFactory sockFact = sc.getSocketFactory();

		// Add ssl context to https connection
		HttpsURLConnection.setDefaultSSLSocketFactory(sockFact);
	}

}
