package com.csumb.cst363;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;


public class DataGenerate {
	
	static final String DBURL = "jdbc:mysql://localhost:3306/HospitalDB";
	static final String USERID = "root";
	static final String PASSWORD = "J@perona1";
	
	static ArrayList<String> patientSSNs = new ArrayList<>();
	static ArrayList<String> doctorSSNs = new ArrayList<>();
	static final String[] specialties = {"Internal Medicine", "Family Medicine", "Pediatrics", "Orthopedics", "Dermatology", "Cardiology", "Gynecology", "Gastroenterology", "Psychiatry", "Oncology"};
	static final String[] drugs = {"Drug1", "Drug2", "Drug3", "Drug4", "Drug5"};
	
	public static void main(String[] args) {
		
		Random gen = new Random();
		try (Connection conn = DriverManager.getConnection(DBURL, USERID, PASSWORD);) {
	
			PreparedStatement ps;
			ResultSet rs;
			int id;
			int row_count;
			
			// Delete all prescription rows
			ps = conn.prepareStatement("delete from prescription");
			row_count = ps.executeUpdate();
			System.out.println("rows deleted from prescription: "+row_count);
			// Delete all doctor rows
			ps = conn.prepareStatement("delete from doctor");
			row_count = ps.executeUpdate();
			System.out.println("rows deleted from doctor: "+row_count);
			// Delete all patient rows
			ps = conn.prepareStatement("delete from patient");
			row_count = ps.executeUpdate();
			System.out.println("rows deleted from patient: "+row_count);

			// Prepare statements for insertion
			String sqlInsertDoctor = "insert into doctor(last_name, first_name, specialty, practice_since, ssn) values(?, ?, ?, ?, ?)";
			String sqlInsertPatient = "insert into patient(last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String sqlInsertPrescription = "insert into prescription(drugName, quantity, patient_ssn, patientFirstName, patientLastName, doctor_ssn, doctorFirstName, doctorLastName) values(?, ?, ?, ?, ?, ?, ?, ?)";
			String[] keycols = {"id"};
			String ssn;
			boolean duplicateSSN = false;
	
			ps = conn.prepareStatement(sqlInsertDoctor, keycols);
			for (int k=1; k<=10; k++) {
				ssn = Integer.toString(123450000+gen.nextInt(10000));
				//checks for duplicate SSNs
				for (int j = 0; j < doctorSSNs.size(); j++) {
					if (ssn.equals(doctorSSNs.get(j))) {
						k--;
						duplicateSSN = true;
					}
				}
				if (!duplicateSSN) {
					ps.setString(5, ssn);
					doctorSSNs.add(ssn);
					String practice_since = Integer.toString(2000+gen.nextInt(20));
					ps.setString(1, "DoctorLast"+k);
					ps.setString(2, "DoctorFirst"+k);
					ps.setString(3, specialties[k%specialties.length]);
					ps.setString(4, practice_since);
					row_count = ps.executeUpdate();
					System.out.println("row inserted for doctor "+k);
				}
				duplicateSSN = false;
			}
			
			// Insert patients
			ps = conn.prepareStatement(sqlInsertPatient, keycols);
			for (int k=1; k<=100; k++) {
				ssn = Integer.toString(123450000+gen.nextInt(10000));
				
				//checks for duplicate SSNs
				for (int j = 0; j < patientSSNs.size(); j++) {
					if (ssn.equals(patientSSNs.get(j))) {
						k--;
						duplicateSSN = true;
					}
				}
				if (!duplicateSSN) {
					ps.setString(4, ssn);
					patientSSNs.add(ssn);
					
					int birthYear = 1900 + gen.nextInt(123);
					String birthdate = birthYear + "-" + (1+gen.nextInt(12)) + "-" + (1+gen.nextInt(28));
		
					ps.setString(1, "PatientLast"+k);
					ps.setString(2, "PatientFirst"+k);
					ps.setString(3, birthdate);
					ps.setString(5, "Street "+k);
					ps.setString(6, "City "+k);
					ps.setString(7, "State "+k);
					ps.setString(8, "Zip "+k);
					ps.setString(9, "DoctorLast"+ (1+gen.nextInt(10)));
					row_count = ps.executeUpdate();
					if (row_count > 0) {
						System.out.println("row inserted for patient "+row_count);
					} else {
						k--;
					}
				}
			}
			// Insert prescriptions
			ps = conn.prepareStatement(sqlInsertPrescription, keycols);
			for (int k = 1; k <= 100; k++) {
				String patient_ssn = patientSSNs.get(gen.nextInt(patientSSNs.size()));
				String doctor_ssn = doctorSSNs.get(gen.nextInt(doctorSSNs.size()));
				// Select a random drug from the "Drug" table
				String sqlSelectDrug = "SELECT trade_name FROM Drug ORDER BY RAND()LIMIT 1";
	
				PreparedStatement selectDrugStatement = conn.prepareStatement(sqlSelectDrug);
	
				ResultSet drugResultSet = selectDrugStatement.executeQuery();
				if (drugResultSet.next()) {
					String drugName = drugResultSet.getString("trade_name");
					int quantity = 1 + gen.nextInt(100);
					int patientNumber = 1 + gen.nextInt(100);
					String patientFirstName = "PatientFirst" + patientNumber;
					String patientLastName = "PatientLast" + patientNumber;
					int drNumber = 1 + gen.nextInt(10);
					String doctorFirstName = "DoctorFirst" + drNumber;
					String doctorLastName = "DoctorLast" + drNumber;
					ps.setString(1, drugName);
					ps.setInt(2, quantity);
					ps.setString(3, patient_ssn);
					ps.setString(4, patientFirstName);
					ps.setString(5, patientLastName);
					ps.setString(6, doctor_ssn);
					ps.setString(7, doctorFirstName);
					ps.setString(8, doctorLastName);
					row_count = ps.executeUpdate();
					System.out.println("row inserted for prescription " + row_count);
				}
			}
	
			// display all rows for doctors
			System.out.println("All doctors");
			String sqlSELECT = "select id, last_name, first_name, specialty, practice_since, ssn from doctor";
	
			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
				String last_name = rs.getString("last_name");
				String first_name = rs.getString("first_name");
				String specialty = rs.getString("specialty");
				String practice_since = rs.getString("practice_since");
				ssn = rs.getString("ssn");
				System.out.printf("%10d %-30s %-20s %4s %11s \n", id, last_name+", "+first_name, specialty, practice_since, ssn); }
	
			// display all rows for patients
			System.out.println("All patients");
			sqlSELECT = "select patientId, last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName from patient";
			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("patientId");
				String last_name = rs.getString("last_name");
				String first_name = rs.getString("first_name");
				String birthdate = rs.getString("birthdate");
				ssn = rs.getString("ssn");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zipcode = rs.getString("zipcode");
				String primary = rs.getString("primaryName");
	
				System.out.printf("%10d %-30s %-20s %10s %11s %-15s %-10s %-10s %-30s \n", id, last_name+", "+first_name, birthdate, ssn, street, city, state, zipcode, primary);
	
			}
			// display all rows for prescriptions
			System.out.println("All prescriptions");
			sqlSELECT = "select RXNumber, drugName, quantity, patient_ssn, patientFirstName, patientLastName, doctor_ssn, doctorFirstName, doctorLastName from prescription";
	
			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				String rxid = rs.getString("RXNumber");
				String drugName = rs.getString("drugName");
				int quantity = rs.getInt("quantity");
				String patient_ssn = rs.getString("patient_ssn");
				String patientFirstName = rs.getString("patientFirstName");
				String patientLastName = rs.getString("patientLastName");
				String doctor_ssn = rs.getString("doctor_ssn");
				String doctorFirstName = rs.getString("doctorFirstName");
				String doctorLastName = rs.getString("doctorLastName");
	
				System.out.printf("%10s %-30s %-20d %13s %20s %20s %13s %20s %20s \n", rxid, drugName, quantity, patient_ssn, patientFirstName, patientLastName, doctor_ssn, doctorFirstName, doctorLastName);
			}
		} catch (SQLException e) {
			System.out.println("Error: SQLException "+e.getMessage());
		}
	}
}