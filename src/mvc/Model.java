package mvc;

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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
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
import org.xml.sax.SAXException;

import classes.Country;

/**
 * Class that contains the logic of the program
 * 
 * @author Ausiàs
 * @version 1.0
 */
public class Model {

	/**
	 * String Log of the user name specified by the user at the log in screen
	 */
	private String sessionUsername;

	/**
	 * String Log of the password specified by the user at the log in screen
	 */
	private String sessionPwd;

	/**
	 * String Typus / profile of the user in the program
	 */
	private String typus;

	/**
	 * String Registry of the last query specified by the user
	 */
	private String queryLog;

	/**
	 * String Route of the folder with the XML files
	 */
	private String XMLROUTE = "resources" + File.separator + "xml";

	/**
	 * Simple Getter
	 * 
	 * @return String Typus / profile of the user in the program
	 */
	public String getTypus() {
		return typus;
	}

	/**
	 * Simple Setter
	 * 
	 * @param queryLog String The last query made by the user
	 */
	public void setQueryLog(String queryLog) {
		this.queryLog = queryLog;
	}

	/**
	 * Method that checks if a user exists by trying to create a connection with the
	 * user's credentials
	 * 
	 * @param username String The name of the user
	 * @param pwd      String The password of the user
	 * @throws Exception If a connection wasn't able to be established due to not
	 *                   finding the user, or any other reasons, the method throws
	 *                   an Exception
	 */
	public void userExists(String username, String pwd) throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String pwdHash = DigestUtils.md5Hex(pwd);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", username, pwdHash);

			if (con.isValid(600)) {
				this.sessionUsername = username;
				this.sessionPwd = pwdHash;
				checkCredentials();
			} else {
				throw new Exception("Error de connexió a la base de dades.");
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new Exception("Error de connexió a la base de dades.");
		}
	}

	/**
	 * Method that creates a new user in the database with a 'client' profile
	 * 
	 * @param username String The name of the new user
	 * @param pwd      String The password of the new user
	 * @throws Exception A common Exception with a specific message to describe if
	 *                   something went wrong with the creation of the user
	 */
	public void signUpUser(String username, String pwd) throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String queryCreate = "CREATE USER ? IDENTIFIED BY ?";
			String queryPermissions = "GRANT SELECT on population.population TO ?";
			String queryInsert = "INSERT INTO users (login, pwd, typus) VALUES (?, ?, ?)";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", sessionUsername,
					sessionPwd);
					PreparedStatement stmtCreate = con.prepareStatement(queryCreate);
					PreparedStatement stmtPermissions = con.prepareStatement(queryPermissions);
					PreparedStatement stmtInsert = con.prepareStatement(queryInsert);) {

				String pwdHash = DigestUtils.md5Hex(pwd);

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
				if (affectedRows == 0) {
					throw new Exception("Error en la creació de l'usuari.");
				}
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new Exception("Error en la creació de l'usuari.");
		}
	}

	/**
	 * Method that imports a CSV file and creates a Table with its header, and
	 * inserts registries into the new table with its content
	 * 
	 * @param route String The route where the CSV file is located
	 * @throws Exception A common Exception with a specific message to describe if
	 *                   anything went wrong
	 */
	public void importCSV(String route) throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String queryDeleteTable = "DROP TABLE IF EXISTS population";

			FileReader fr = new FileReader(new File(route), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(fr);
			String csvHeader = br.readLine();
			String queryCreateTable = createTableQuery(csvHeader);
			String queryInsertRegistry = createInsertQuery(csvHeader);
			br.close();
			fr.close();

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", sessionUsername,
					sessionPwd);
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
						throw new Exception("Error en la importació de les dades.");
					}
				}
			}
		} catch (SQLException | ClassNotFoundException | IOException ex) {
			throw new Exception("Error en la importació de les dades.");
		} catch (ParserConfigurationException | TransformerException | SAXException ex) {
			throw new Exception("Error en la lectura/escritura dels fitxers XML");
		}
	}

	/**
	 * Method that executes a query and formats its return into a DefaultTableModel
	 * object
	 * 
	 * @param query String The query specifying the information to retrieve from the
	 *              database
	 * @return DefaultTableModel The return of the query formatted into a
	 *         DefaultTableModel object, fit for a JTable component
	 * @throws Exception A common Exception with a specific message to describe if
	 *                   something went wrong when executing the query or formatting
	 *                   the data
	 */
	public DefaultTableModel executeQuery(String query) throws Exception {
		DefaultTableModel tableModel = new DefaultTableModel();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", sessionUsername,
					sessionPwd); PreparedStatement stmt = con.prepareStatement(query);) {

				ResultSet rs = stmt.executeQuery();

				ResultSetMetaData metaData = rs.getMetaData();
				int columns = metaData.getColumnCount();

				for (int i = 1; i <= columns; i++) {
					tableModel.addColumn(metaData.getColumnName(i));
				}

				while (rs.next()) {
					Object[] row = new Object[columns];
					for (int i = 0; i < columns; i++) {
						row[i] = rs.getObject(i + 1);
					}
					tableModel.addRow(row);
				}
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new Exception("Error, falta de permisos o error de sintaxis.");
		}
		return tableModel;
	}

	/**
	 * Method that constructs a string with the data of multiple XML files
	 * 
	 * @return String A concatenated string with the data of every Country XML file
	 *         that it read
	 * @throws Exception A common Exception with a specific message to describe if
	 *                   something went wrong when reading the XML files
	 */
	public String retrieveXMLS() throws Exception {
		String countriesData = "";
		try {
			ArrayList<Country> countries = readXML();
			for (Country con : countries) {
				countriesData += con.toString() + System.lineSeparator();
			}
		} catch (IOException | SAXException | ParserConfigurationException ex) {
			throw new Exception("Error recuperant els fitxers XML");
		}
		return countriesData;
	}

	/**
	 * Method that exports the returned content of a query into a csv file
	 * 
	 * @throws Exception A common Exception with a specific message to describe if
	 *                   anything went wrong
	 */
	public void exportCSV() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", sessionUsername,
					sessionPwd); PreparedStatement stmt = con.prepareStatement(queryLog);) {

				ResultSet rs = stmt.executeQuery();
				ResultSetMetaData rsm = rs.getMetaData();

				String csvText = "";

				for (int i = 1; i < rsm.getColumnCount(); i++) {
					csvText += rsm.getColumnName(i) + ";";
				}
				csvText = csvText.substring(0, csvText.length() - 1);

				csvText += System.lineSeparator();

				while (rs.next()) {
					for (int i = 1; i < rsm.getColumnCount(); i++) {
						csvText += rs.getString(i) + ";";
					}
					csvText = csvText.substring(0, csvText.length() - 1);
					csvText += System.lineSeparator();
				}

				rs.close();

				writeCsv(csvText);
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new Exception("Error en la consulta.");
		} catch (IOException ex) {
			throw new Exception("Error escrivint l'arxiu.");
		}
	}

	/**
	 * Method that checks a users credentials by trying to execute a query on a
	 * database
	 */
	private void checkCredentials() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String query = "SELECT typus FROM users WHERE login = ?";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", sessionUsername,
					sessionPwd); PreparedStatement stmt = con.prepareStatement(query);) {

				stmt.setString(1, sessionUsername);

				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					this.typus = rs.getString("typus");
				} else {
					this.typus = "client";
				}
			}
		} catch (SQLException | ClassNotFoundException ex) {
			this.typus = "client";
		}
	}

	/**
	 * Method that mounts a dynamic Create Table Query based on the header of a csv
	 * file
	 * 
	 * @param headerCSV String The header of the csv file from which the query is
	 *                  going to be created
	 * @return String A Create Table query with the fields contained in the csv
	 *         header
	 */
	private String createTableQuery(String headerCSV) {
		String[] fields = headerCSV.split(";");

		String queryCreateTable = "CREATE TABLE population (" + "id INT(10) AUTO_INCREMENT,";

		for (String field : fields) {
			queryCreateTable += field + " VARCHAR(30),";
		}

		queryCreateTable += "PRIMARY KEY(id))";

		return queryCreateTable;
	}

	/**
	 * Method that mounts a dynamic Insert Query based on the header of a csv file
	 * 
	 * @param headerCSV String The header of the csv file from which the query is
	 *                  going to be created
	 * @return String An insert query with the fields contained in the csv header
	 */
	private String createInsertQuery(String headerCSV) {
		String[] fields = headerCSV.split(";");

		String queryInsertRegistry = "INSERT INTO population (";
		String queryValues = "VALUES (";

		for (String field : fields) {
			queryInsertRegistry += field + ", ";
			queryValues += "?, ";
		}

		queryInsertRegistry = queryInsertRegistry.substring(0, queryInsertRegistry.length() - 2);
		queryValues = queryValues.substring(0, queryValues.length() - 2);

		queryInsertRegistry += ") " + queryValues + ");";

		return queryInsertRegistry;
	}

	/**
	 * Method that creates an XML file for every Country object contained in an
	 * ArrayList
	 * 
	 * @param countries ArrayList<Country> List with all the objects that are going
	 *                  to be written
	 * @throws ParserConfigurationException If there is an Exception when trying to
	 *                                      write the files
	 * @throws TransformerException         If there is an Exception when trying to
	 *                                      write the files
	 */
	private void createXMLFiles(ArrayList<Country> countries)
			throws ParserConfigurationException, TransformerException {
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
		} catch (ParserConfigurationException | TransformerException ex) {
			throw ex;
		}
	}

	/**
	 * Method that reads a csv file and creates objects with the content of the file
	 * 
	 * @param routeCSV String The route where the csv file is located
	 * @return ArrayList<Country> List with all the Country objects retrieved from
	 *         the csv file
	 * @throws IOException If there is an Exception when trying to read the file
	 */
	private ArrayList<Country> readCSV(String routeCSV) throws IOException {
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
		} catch (IOException ex) {
			throw ex;
		}
		return countries;
	}

	/**
	 * Method that reads a number of XML files and creates objects with the content
	 * of the files
	 * 
	 * @return ArrayList<Country> List with all the Country objects retrieved from
	 *         the XML files
	 * @throws IOException                  If there is an Exception when trying to
	 *                                      read the files
	 * @throws SAXException                 If there is an Exception when trying to
	 *                                      read the files
	 * @throws ParserConfigurationException If there is an Exception when trying to
	 *                                      read the files
	 */
	private ArrayList<Country> readXML() throws IOException, SAXException, ParserConfigurationException {
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
						Country country = new Country(
								eElement.getElementsByTagName("countryName").item(0).getTextContent(),
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
			} catch (IOException | SAXException | ParserConfigurationException ex) {
				throw ex;
			}
		}

		return countries;
	}

	/**
	 * Method that writes a given text into a csv file
	 * 
	 * @param csvText String Text to write
	 * @throws IOException If there is an Exception when trying to write the text
	 */
	private void writeCsv(String csvText) throws IOException {
		try (FileWriter fw = new FileWriter("resources\\csv\\exportedData.csv")) {
			fw.write(csvText);
		} catch (IOException ex) {
			throw ex;
		}
	}

	/**
	 * Method that removes the accents and normalizes a given text.
	 * 
	 * @param text String Text to normalize
	 * @return String Normalized text without accents
	 */
	private String removeAccents(String text) {
		String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
		normalizedText = normalizedText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return normalizedText;
	}

}
