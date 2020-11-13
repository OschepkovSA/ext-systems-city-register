package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PersonCheckDaoTest {
	
	@Test
	public void checkPerson() throws PersonCheckException {
		PersonRequest pr = new PersonRequest();
		pr.setSurName("Андреев");
		pr.setGivenName("Алексей");
		pr.setPatronomic("Васильевич");
		pr.setDateOfBirth(LocalDate.of(1990,10,10));
		pr.setStreetCode(1L);
		pr.setBuilding("10");
		pr.setExtension("2");
		pr.setAppartment("141");
		
		PersonCheckDao dao = new PersonCheckDao();
		dao.setConnectionBuilder(new DirectConnectionBuilder());
		PersonResponse ps = dao.checkPerson(pr);
		Assert.assertTrue(ps.isRegistered());
		Assert.assertFalse(ps.isTemporal());
	}
	
	@Test
	public void checkPerson2() throws PersonCheckException {
		PersonRequest pr = new PersonRequest();
		pr.setSurName("Андреева");
		pr.setGivenName("Ирина");
		pr.setPatronomic("Петровна");
		pr.setDateOfBirth(LocalDate.of(1990,10,10));
		pr.setStreetCode(1L);
		pr.setBuilding("10");
		pr.setExtension("2");
		pr.setAppartment("141");

		PersonCheckDao dao = new PersonCheckDao();
		dao.setConnectionBuilder(new DirectConnectionBuilder());
		PersonResponse ps = dao.checkPerson(pr);
		Assert.assertTrue(ps.isRegistered());
		Assert.assertFalse(ps.isTemporal());
	}
}