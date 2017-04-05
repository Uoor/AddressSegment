package com.AddressSegment.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.AddressSegment.logic.service.HttpRequestTemplate;
import com.AddressSegment.logic.service.IHttpResponseHandler;
import com.AddressSegment.metadata.model.CoordinateCode;
import com.AddressSegment.util.Config;

public final class GaodeEncodingServiceInvoker extends HttpRequestTemplate {
	private static final String GAODE_URL = Config.getGaodeUrl();
	private static final String GAODE_RESPOND_TYPE = Config.getGaodeResponseType();
	private static final String GAODE_KEY = Config.getGaodeKEY();
	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder xmlBuilder;

	static {
		try {
			xmlBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Create Xml Document Builder Failed.");
			System.exit(-1);
		}
	}

	private CoordinateCode<String, String> coordinate;

	public CoordinateCode<String, String> getGaodeResult(String address) {
		StringBuffer url = new StringBuffer(GAODE_URL);
		Map<String, String> map = new HashMap<String, String>();
		map.put("address", "…œ∫£ –" + address);
		map.put("output", GAODE_RESPOND_TYPE);
		map.put("key", GAODE_KEY);
		setHttpUrl(url.toString());
		post(new XmlResultParser(), map);
		/*
		 * if (null == chineseWords) { System.out.println("--------"); }
		 */
		return coordinate;
	}

	public CoordinateCode<String, String> getCoordinate() {
		return coordinate;
	}

	class XmlResultParser implements IHttpResponseHandler {
		private Document xmlDocument;

		@Override
		public String handleHttpResponse(
				Map<String, List<String>> responseHeader, 
				InputStream responseStream) {
			try {
				synchronized (xmlBuilder) {
					xmlDocument = xmlBuilder.parse(responseStream);
				}
			} catch (SAXException e) {
				System.err.println("Invalid Xml Document Format.");
			} catch (IOException e) {
				System.err.println("Loading Http Response InputStream Failed.");
			}
			if (checkStatus()) {
				NodeList LocationNodeList = xmlDocument.getElementsByTagName("geocodes");
				Element element;
				coordinate = new CoordinateCode<String, String>();
				element = ((Element) LocationNodeList.item(0));
				String[] strTemp = getChildElementValue(element, "location").split(",");
				coordinate.addCoordinate(strTemp[0], strTemp[1]);
			}
			return "SUCCESS";
		}

		private String getChildElementValue(Element parent, String childElementName) {
			String value = null;
			try {
				value = parent.getElementsByTagName(childElementName).item(0).getTextContent();
			} catch (NullPointerException nullExp) {
				System.err.println(
						"Cannot Found Child Node[" + childElementName + "] Under Element: " + parent.getNodeName());
				value = "";
			}
			return value;
		}

		private boolean checkStatus() {
			Element statusElement = null;
			try {
				statusElement = (Element) xmlDocument.getElementsByTagName("status").item(0);
				if ("1".equals(statusElement.getTextContent())) {
					return true;
				}
			} catch (NullPointerException nullExp) {
				return false;
			}
			return false;
		}

	}

}
