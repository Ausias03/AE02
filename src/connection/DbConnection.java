package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private Connection conexion;

	public Connection getConexion() {
		return conexion;
	}

	public DbConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/population";
		String user = "root";
		String pwd = "";

		this.conexion = DriverManager.getConnection(url, user, pwd);
	}
}
