package be.pxl.auctions.model;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserGetAgeTest {


	private static final int YEARS = 15;
	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS));

		int currentAge = user.getAge();

		assertEquals(YEARS, currentAge);
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).plusDays(1));

		int currentAge = user.getAge();

		assertEquals(YEARS - 1, currentAge);
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).minusDays(1));

		int currentAge = user.getAge();

		assertEquals(YEARS, currentAge);
	}

}
