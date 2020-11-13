package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {
	
	private static final String SQL_REQUEST =
			"select temporal from cr_address_person ap "
					+ "inner join cr_person p on p.person_id = ap.person_id "
					+ "inner join cr_address a on a.address_id = ap.address_id "
					+ "where  "
					+ "CURRENT_DATE >= ap.start_date and (CURRENT_DATE <= ap.end_date or ap.end_date is null)"
					+ "and upper(p.sur_name) = upper(?)  "
					+ "and upper(p.given_name) = upper(?) "
					+ "and upper(p.patronymic) = upper(?) "
					+ "and p.date_of_birth = ? "
					+ "and a.street_code = ?  "
					+ "and upper(a.building) = upper(?)  ";
	
	
	private ConnectionBuilder connectionBuilder;
	
	public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
		this.connectionBuilder = connectionBuilder;
	}
	
	private Connection getConnection() throws SQLException {
		return connectionBuilder.getConnection();
		
	}
	
	public PersonResponse checkPerson (PersonRequest request) throws PersonCheckException {
		PersonResponse response = new PersonResponse();
		
		String sql = SQL_REQUEST;
		if (request.getExtension() != null) {
			sql += " and upper(a.extension) = upper(?) ";
		} else {
			sql += " and extension is null ";
		}
		
		if (request.getAppartment() != null) {
			sql += " and upper(a.apartment) = upper(?) ";
		} else {
			sql += " and apartment is null ";
		}
		
		
		try (Connection con = getConnection();
			 PreparedStatement stmt = con.prepareStatement(sql)
		) {
			
			int count = 1;
			
			stmt.setString(count++, request.getSurName());
			stmt.setString(count++, request.getGivenName());
			stmt.setString(count++,request.getPatronomic());
			stmt.setDate(count++, java.sql.Date.valueOf(request.getDateOfBirth()));
			stmt.setLong(count++, request.getStreetCode());
			stmt.setString(count++, request.getBuilding());
			
			if (request.getExtension() != null) {
				stmt.setString(count++ , request.getExtension());
			}
			if (request.getAppartment() != null) {
				stmt.setString(count++ , request.getAppartment());
			}
			
			
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				response.setRegistered(true);
				response.setTemporal(resultSet.getBoolean("temporal"));
			}
			
		} catch (SQLException e) {
			throw new PersonCheckException(e);
		}
		
		
		return response;
	}
	

}
