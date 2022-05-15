package com; 
import java.sql.*;

public class Payment 
{ 	//A common method to connect to the DB
	private Connection connect() 
	{ 
		Connection con = null; 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/payments", "root", "");
			// For testing
			System.out.print("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public String insertPay(String fname,String lname, String method, String name, String price, String email) 
			 { 
			 String output = ""; 
			 try {
					Connection con = connect();
					if (con == null) {
						return "Error while connecting to the database";
					}
					// create a prepared statement
					String query = " insert into payment(`PID`,`fname`,`lname`,`pMethod`,`itemName`,`itemPrice`,`email`)"
							+ " values (?, ?, ?, ?, ?)";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					// binding values
					preparedStmt.setInt(1, 0);
					preparedStmt.setString(2, fname);
					preparedStmt.setString(3, lname);
					preparedStmt.setString(4, method);
					preparedStmt.setString(5, name);
					preparedStmt.setDouble(6, Double.parseDouble(price));
					preparedStmt.setString(7, email);

					// execute the statement
					preparedStmt.execute();
					con.close();
					String newPay = readPayments(); 
					 output = "{\"status\":\"success\", \"data\": \"" + 
					 newPay + "\"}";
					output = "Inserted successfully";
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while inserting the payment.\"}"; 
					 System.err.println(e.getMessage());
				}
				return output;
			} 
			
	public String readPayments()
	{ 
	 String output = ""; 
	 try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>First Name</th>" + "<th>Last Name</th>" + "<th>Payment Method</th>" + "<th>Item Name</th><th>Item Price</th>"
					+ "<th>Email</th>" + "<th>Update</th><th>Remove</th></tr>";
			String query = "select * from payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String PID = Integer.toString(rs.getInt("PID"));
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");

				String pMethod = rs.getString("pMethod");
				String itemName = rs.getString("itemName");
				String itemPrice = Double.toString(rs.getDouble("itemPrice"));
				String email = rs.getString("email");
				// Add a row into the html table
				output += "<tr><td>" + fname + "</td>";
				output += "<tr><td>" + lname + "</td>";
				
				output += "<tr><td>" + pMethod + "</td>";
				output += "<td>" + itemName + "</td>";
				output += "<td>" + itemPrice + "</td>";

				output += "<td>" + email + "</td>";
	// buttons
	output += "<td><input name='btnUpdate' type='button' value='Update' "
	+ "class='btnUpdate btn btn-secondary' data-pid='" + PID + "'></td>"
	+ "<td><input name='btnRemove' type='button' value='Remove' "
	+ "class='btnRemove btn btn-danger' data-pid='" + PID + "'></td></tr>"; 
	 } 
	 con.close(); 
	 // Complete the html table
	 output += "</table>"; 
	 } 
	catch (Exception e) 
	 { 
	 output = "Error while reading the payments."; 
	 System.err.println(e.getMessage()); 
	 } 
	return output; 
	}
		
	public String updatePay(String ID, String fname, String lname, String method, String name, String price, String email) 
			 { 
			 String output = ""; 
			 try {
					Connection con = connect();
					if (con == null) {
						return "Error while connecting to the database for updating.";
					}
					// create a prepared statement
					String query = "UPDATE payment SET fname=?,lname=?,pMethod=?,itemName=?,itemPrice=?,email=? WHERE PID=?";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					// binding values
					preparedStmt.setString(1, fname);
					preparedStmt.setString(2, lname);
					preparedStmt.setString(3, method);
					preparedStmt.setString(4, name);
					preparedStmt.setDouble(5, Double.parseDouble(price));
					preparedStmt.setString(6, email);
					preparedStmt.setInt(5, Integer.parseInt(ID));
					// execute the statement
					preparedStmt.execute();
					con.close();
					String newPay = readPayments(); 
					 output = "{\"status\":\"success\", \"data\": \"" + 
					 newPay + "\"}"; 
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while updating the Payments.\"}"; 
					 System.err.println(e.getMessage()); 
				}
				return output;
			}
			 
	public String deletePay(String PID) 
	 { 
	 String output = ""; 
	 try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from payment where PID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(PID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newPay = readPayments(); 
			 output = "{\"status\":\"success\", \"data\": \"" + 
			 newPay + "\"}"; 
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while deleting the Payment.\"}"; 
			 System.err.println(e.getMessage());
		}
		return output;
	}
} 