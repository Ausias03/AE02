package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

public class Model {

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

				// Cree usuari
				stmtCreate.setString(1, username);
				stmtCreate.setString(2, pwdHash);
				stmtCreate.executeUpdate();

				// Li done permisos
				stmtPermissions.setString(1, username);
				stmtPermissions.executeUpdate();

				// Inserció del usuari en la tabla users
				stmtInsert.setString(1, username);
				stmtInsert.setString(2, pwdHash);
				stmtInsert.setString(3, "user");
				int affectedRows = stmtInsert.executeUpdate();
				return affectedRows > 0;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	private boolean checkPwd(String pwd, String pwdHash) {
		return DigestUtils.md5Hex(pwd).equals(pwdHash);
	}

}
