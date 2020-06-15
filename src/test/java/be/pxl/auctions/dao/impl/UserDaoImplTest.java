package be.pxl.auctions.dao.impl;

import be.pxl.auctions.dao.impl.util.DaoTest;
import be.pxl.auctions.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest extends DaoTest {

	private UserDaoImpl userDao = new UserDaoImpl(em);

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		long newUserId = userDao.saveUser(user).getId();
		clear();

		User retrievedUser = userDao.findUserById(newUserId);
		assertNotNull(retrievedUser);
		assertEquals(user.getFirstName(), retrievedUser.getFirstName());
		assertEquals(user.getLastName(), retrievedUser.getLastName());
		assertEquals(user.getEmail(), retrievedUser.getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.getDateOfBirth());
	}

	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = new User();
		user.setFirstName("Vito");
		user.setLastName("Mastroianni");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("vito@facebook.com");
		var newUserEmail = userDao.saveUser(user).getEmail();
		clear();

		User retrievedUser = userDao.findUserByEmail(newUserEmail);
		assertNotNull(retrievedUser);
		assertEquals(user.getFirstName(), retrievedUser.getFirstName());
		assertEquals(user.getLastName(), retrievedUser.getLastName());
		assertEquals(user.getEmail(), retrievedUser.getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.getDateOfBirth());
	}

	@Test
	public void returnsNullWhenNoUserFoundWithGivenEmail() {
		User retrievedUser = userDao.findUserByEmail("NotAValid@email.com");
		assertNull(retrievedUser);
	}

	@Test
	public void allUsersCanBeRetrieved() {
		// TODO implement this test
		// create and save one user
		User user = new User();
		user.setFirstName("Elke");
		user.setLastName("Slegers");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("elke@facebook.com");
		userDao.saveUser(user);
		clear();

		// retrieve all users
		// make sure there is at least 1 user in the list
		// make sure the newly created user is in the list (e.g. test if a user with this email address is in the list)
		var userList = userDao.findAllUsers();

		assertNotNull(userList);
		assertTrue(userList.stream().anyMatch(e -> e.getEmail().equals(user.getEmail())));
	}


}
