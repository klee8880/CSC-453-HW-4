package main;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class zipcode {

	private static final String url = "jdbc:oracle:thin:@acadoradbprd01.dpu.depaul.edu:1521:ACADPRD0";
	private static final String QUERY = "select rl.name, rl.zipcode, z.latitude, z.longitude from restaurant_locations rl, zipcode z where rl.zipcode = z.zip";
	
	
	
	public static void main(String [] args) throws SQLException, IOException {
	
		Connection conn = null;
		
		System.out.println("Program Start:");
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, "klee66", "cdm1347412");
		}
	    catch (ClassNotFoundException ex) {System.err.println("Class not found " + ex.getMessage());}
	    catch (SQLException ex)           {System.err.println(ex.getMessage());return;}
		
		System.out.println("Connection successful");
		
		//b.
		Scanner file = new Scanner(new File("src/data/ChIzipcode.csv"));
		Statement dbStatment = conn.createStatement();
		
		if (!file.hasNextLine()) {
		
			dbStatment.close();
			file.close();
			conn.close();
			return;
		}
		
		file.nextLine();
		
		while (file.hasNextLine()) {
			//TODO: Check for previous entry
			
			//Get csv & decompose
			String []csv = file.nextLine().replaceAll("\"", "").split(",");
			
			//Build statement
			StringBuilder statement = new StringBuilder("INSERT INTO zipcode (zip,city,st,latitude,longitude,timezone,dst) values(");
			
			statement.append(Integer.parseInt(csv[0])); statement.append(", '");
			statement.append(csv[1]); statement.append("', '");
			statement.append(csv[2]); statement.append("', ");
			statement.append(Double.parseDouble(csv[3])); statement.append(", ");
			statement.append(Double.parseDouble(csv[4])); statement.append(", ");
			statement.append(Integer.parseInt(csv[5]));	statement.append(", ");
			statement.append(Integer.parseInt(csv[6]));	statement.append(")");
			
			System.out.println(statement.toString());
			
			try {
				dbStatment.executeUpdate(statement.toString());
			}
			catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
		//c.
		ResultSet rs = dbStatment.executeQuery(QUERY);
		
		while (rs.next()) {
			System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", \"" + rs.getString(3) + "\", \""+ rs.getString(4) + "\"");
		}
		dbStatment.close();
		file.close();
		conn.close();
		
	}
	
}
