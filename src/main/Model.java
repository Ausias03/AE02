package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Model {
	
	private String XMLROUTE = "resources" + File.separator + "xml";

	public boolean checkUserInfo(String username, char[] pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String query = "SELECT pwd FROM users WHERE login = ?";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", "root", "");
					PreparedStatement stmt = con.prepareStatement(query);) {

				stmt.setString(1, username);

				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					String hashPwd = rs.getString("pwd");

					return checkPwd(new String(pwd), hashPwd);
				} else {
					return false;
				}
			}
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public boolean signUpUser(String username, char[] pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String queryCreate = "CREATE USER ? IDENTIFIED BY ?";
			String queryPermissions = "GRANT SELECT on population.population TO ?";
			String queryInsert = "INSERT INTO users (login, pwd, typus) VALUES (?, ?, ?)";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", "root", "");
					PreparedStatement stmtCreate = con.prepareStatement(queryCreate);
					PreparedStatement stmtPermissions = con.prepareStatement(queryPermissions);
					PreparedStatement stmtInsert = con.prepareStatement(queryInsert);) {

				String pwdHash = DigestUtils.md5Hex(new String(pwd));

				// Create user
				stmtCreate.setString(1, username);
				stmtCreate.setString(2, pwdHash);
				stmtCreate.executeUpdate();

				// Give permissions to user
				stmtPermissions.setString(1, username);
				stmtPermissions.executeUpdate();

				// Insert user in user table
				stmtInsert.setString(1, username);
				stmtInsert.setString(2, pwdHash);
				stmtInsert.setString(3, "client");
				int affectedRows = stmtInsert.executeUpdate();
				return affectedRows > 0;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public boolean importCSV(String route) {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String queryDeleteTable = "DROP TABLE IF EXISTS population";

			FileReader fr = new FileReader(new File(route), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(fr);
			String queryCreateTable = createTableQuery(br.readLine());
			br.close();
			fr.close();

			String queryInsertRegistry = "INSERT INTO population (country, population, density, area, fertility, age, urban, share)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", "root", "");
					PreparedStatement stmtDelete = con.prepareStatement(queryDeleteTable);
					PreparedStatement stmtCreate = con.prepareStatement(queryCreateTable);
					PreparedStatement stmtInsert = con.prepareStatement(queryInsertRegistry);) {

				// Delete table
				stmtDelete.executeUpdate();

				// Create table
				stmtCreate.executeUpdate();
				
				// Creation of XML files
				createXMLFiles(readCSV(route));

				// Insert registries
				ArrayList<Country> countries = readXML();
				for (Country cou : countries) {
					stmtInsert.setString(1, cou.getCountry());
					stmtInsert.setString(2, cou.getPopulation());
					stmtInsert.setString(3, cou.getDensity());
					stmtInsert.setString(4, cou.getArea());
					stmtInsert.setString(5, cou.getFertility());
					stmtInsert.setString(6, cou.getAge());
					stmtInsert.setString(7, cou.getUrban());
					stmtInsert.setString(8, cou.getShare());
					
					int resInsertar = stmtInsert.executeUpdate();
					if (resInsertar != 1) {
						stmtDelete.executeUpdate();
						return false;
					}
				}
			}
			
			return true;
		} catch (SQLException | ClassNotFoundException | IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public String retrieveXMLS() {
		ArrayList<Country> countries = readXML();
		String countriesData = "";
		for (Country con : countries) {
			countriesData += con.toString() + System.lineSeparator();
		}
		return countriesData;
	}

	private boolean checkPwd(String pwd, String pwdHash) {
		return DigestUtils.md5Hex(pwd).equals(pwdHash);
	}

	private String createTableQuery(String headerCSV) {
		String[] fields = headerCSV.split(";");

		String queryCreateTable = "CREATE TABLE population (" + "id INT(10) AUTO_INCREMENT,";

		for (String field : fields) {
			queryCreateTable += field + " VARCHAR(30),";
		}

		queryCreateTable += "PRIMARY KEY(id))";

		return queryCreateTable;
	}

	private void createXMLFiles(ArrayList<Country> countries) {
		File xmlDir = new File("resources/xml");
		if (!xmlDir.exists()) {
			xmlDir.mkdir();
		}

		try {
			for (Country con : countries) {
				DocumentBuilderFactory dCoun = DocumentBuilderFactory.newInstance();
				DocumentBuilder build = dCoun.newDocumentBuilder();
				Document doc = build.newDocument();

				Element country = doc.createElement("country");
				doc.appendChild(country);

				Element countryName = doc.createElement("countryName");
				countryName.appendChild(doc.createTextNode(con.getCountry()));
				country.appendChild(countryName);

				Element population = doc.createElement("population");
				population.appendChild(doc.createTextNode(con.getPopulation()));
				country.appendChild(population);

				Element density = doc.createElement("density");
				density.appendChild(doc.createTextNode(con.getDensity()));
				country.appendChild(density);

				Element area = doc.createElement("area");
				area.appendChild(doc.createTextNode(con.getArea()));
				country.appendChild(area);

				Element fertility = doc.createElement("fertility");
				fertility.appendChild(doc.createTextNode(con.getFertility()));
				country.appendChild(fertility);

				Element age = doc.createElement("age");
				age.appendChild(doc.createTextNode(con.getAge()));
				country.appendChild(age);

				Element urban = doc.createElement("urban");
				urban.appendChild(doc.createTextNode(con.getUrban()));
				country.appendChild(urban);

				Element share = doc.createElement("share");
				share.appendChild(doc.createTextNode(con.getShare()));
				country.appendChild(share);

				TransformerFactory tranFactory = TransformerFactory.newInstance();
				Transformer aTransformer = tranFactory.newTransformer();
				aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
				aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

				DOMSource source = new DOMSource(doc);
				try {
					String fileName = removeAccents(con.getCountry()) + ".xml";
					FileWriter fw = new FileWriter(xmlDir + File.separator + fileName);
					StreamResult result = new StreamResult(fw);
					aTransformer.transform(source, result);
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ParserConfigurationException ex) {
			System.out.println("Error construyendo el documento");
		} catch (TransformerException ex) {
			System.out.println("Error escribiendo el documento");
		}
	}

	private ArrayList<Country> readCSV(String routeCSV) {
		ArrayList<Country> countries = new ArrayList<Country>();
		try {
			FileReader fr = new FileReader(new File(routeCSV), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			String linea = br.readLine();
			while (linea != null) {
				String[] fields = linea.split(";");
				countries.add(new Country(fields));
				linea = br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return countries;
	}
	
	private ArrayList<Country> readXML() {
		ArrayList<Country> countries = new ArrayList<Country>();
		
		File xmlDir = new File(XMLROUTE);
		
		for (File xmlFile : xmlDir.listFiles()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document document = dBuilder.parse(xmlFile);

				NodeList nodeList = document.getElementsByTagName("country");

				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) node;
						Country country = new Country(eElement.getElementsByTagName("countryName").item(0).getTextContent(),
								eElement.getElementsByTagName("population").item(0).getTextContent(),
								eElement.getElementsByTagName("density").item(0).getTextContent(),
								eElement.getElementsByTagName("area").item(0).getTextContent(),
								eElement.getElementsByTagName("fertility").item(0).getTextContent(),
								eElement.getElementsByTagName("age").item(0).getTextContent(),
								eElement.getElementsByTagName("urban").item(0).getTextContent(),
								eElement.getElementsByTagName("share").item(0).getTextContent());

						countries.add(country);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return countries;
	}
	
	/**
	 * Mètode que lleva els accents i normalitza un text proporcionat
	 * 
	 * @param text String Text que es vol normalitzar
	 * @return String Text normalitzat sense accents
	 */
	private String removeAccents(String text) {
		String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
		normalizedText = normalizedText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return normalizedText;
	}

}
