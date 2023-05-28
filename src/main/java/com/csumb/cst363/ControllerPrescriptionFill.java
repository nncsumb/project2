package com.csumb.cst363;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class ControllerPrescriptionFill {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@GetMapping("/prescription/fill")
	public String getFillForm(Model model) {
		model.addAttribute("prescription", new Prescription());
		return "prescription_fill";
	}
	@PostMapping("/prescription/fill")
	public String processFillForm(Prescription p, Model model) {
		try (Connection conn = getConnection()) {
			String sql = "SELECT * FROM Prescription WHERE RXNumber = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, p.getRxid());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				p.setRxid(resultSet.getString("RXNumber"));
				p.setDoctorLastName(resultSet.getString("DoctorLastName"));
				p.setPatientLastName(resultSet.getString("PatientLastName"));
				p.setDrugName(resultSet.getString("DrugName"));
				p.setQuantity(resultSet.getInt("Quantity"));
				p.setDoctor_ssn(resultSet.getString("Doctor_SSN"));
				p.setPatient_ssn(resultSet.getString("Patient_SSN"));
				sql = "SELECT * FROM PHARMACY WHERE Name = ? AND Address = ?";

				statement = conn.prepareStatement(sql);
				statement.setString(1, p.getPharmacyName());
				statement.setString(2, p.getPharmacyAddress());
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					p.setPharmacyPhone(resultSet.getString("PhoneNumber"));
					Date dateFilled = Date.valueOf(LocalDate.now());
					p.setDateFilled(dateFilled.toString());

					sql = "UPDATE Prescription SET DateFilled = ? WHERE RXNumber = ?";

					statement = conn.prepareStatement(sql);
					statement.setDate(1, dateFilled);
					statement.setString(2, p.getRxid());
					statement.executeUpdate();

					model.addAttribute("message", "Prescription has been filled.");

							model.addAttribute("prescription", p);
					return "prescription_show";
				} else {
					model.addAttribute("message", "Invalid Pharmacy Details.");
					model.addAttribute("prescription", p);
					return "prescription_fill";
				}
			} else {
				model.addAttribute("message", "Invalid Prescription ID.");
				model.addAttribute("prescription", p);
				return "prescription_fill";
			}
		} catch (SQLException e) {
			model.addAttribute("message", "Error occurred: " + e.getMessage());
			model.addAttribute("prescription", p);
			return "prescription_fill";
		}
	}
	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}
}