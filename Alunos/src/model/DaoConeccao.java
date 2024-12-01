package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DaoConeccao {

	private Connection con;	
	
	
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbcarometro";
	private String user = "root";
	private String password = "";
	
	public Connection conectarr() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
			return con;
		} catch (Exception erro) {
			System.out.println(erro);
			return null;
		}
	}
}
