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

			String query = "SELECT login, pwd FROM users WHERE login = ?";

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", "root", "");
					PreparedStatement stmt = con.prepareStatement(query);) {

				stmt.setString(1, username);

				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					String user = rs.getString("login");
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

	private boolean checkPwd(String pwd, String pwdHash) {
		String md5Hex = DigestUtils.md5Hex(pwd);

		return md5Hex.equals(pwdHash);
	}

}
