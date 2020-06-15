package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.dao.impl.UserDaoImpl;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserResource;
import be.pxl.auctions.util.EmailValidator;
import be.pxl.auctions.util.EntityManagerUtil;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserService {
	private UserDao userDao;

	public UserService() {
		EntityManager entityManager = EntityManagerUtil.createEntityManager();
		userDao = new UserDaoImpl(entityManager);
	}

	public List<UserResource> getAllUsers() {

		List<User> users = userDao.findAllUsers();
		List<UserResource> usersResource = new ArrayList<>();

		for (User user : users) {
			usersResource.add(mapToUserResource(user));
		}
		return usersResource;
	}
	public UserResource getUserById(long userId) {
		User user = userDao.findUserById(userId);
		if(user != null){
			return (mapToUserResource(user));
		}
		return null;
	}

	public UserResource createUser(UserCreateResource userCreateResource) throws RequiredFieldException, InvalidEmailException, DuplicateEmailException, InvalidDateException {
		if (StringUtils.isBlank(userCreateResource.getFirstName())) {
			throw new RequiredFieldException("FirstName");
		}
		if (StringUtils.isBlank(userCreateResource.getLastName())) {
			throw new RequiredFieldException("LastName");
		}
		if (StringUtils.isBlank(userCreateResource.getEmail())) {
			throw new RequiredFieldException("Email");
		}
		if (!EmailValidator.isValid(userCreateResource.getEmail())) {
			throw new InvalidEmailException("Email is invalid");
		}
		if (StringUtils.isBlank(userCreateResource.getDateOfBirth())) {
			throw new RequiredFieldException("DateOfBirth");
		}
		if (LocalDate.parse(userCreateResource.getDateOfBirth(),
				DateTimeFormatter.ofPattern("dd/MM/uuuu")).isAfter(LocalDate.now())) {
			throw new InvalidDateException("DateOfBirth cannot be in the future.");
		}
		User existingUser = userDao.findUserByEmail(userCreateResource.getEmail());
		if (existingUser != null) {
			throw new DuplicateEmailException("Email already exists");
		}

		User user = mapToUser(userCreateResource);
		userDao.saveUser(user);

		return mapToUserResource(user);
	}

	private User mapToUser(UserCreateResource userCreateResource){

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dateOfBirth = LocalDate.parse(userCreateResource.getDateOfBirth(), formatter);

		User user = new User();
		user.setFirstName(userCreateResource.getFirstName());
		user.setLastName(userCreateResource.getLastName());
		user.setDateOfBirth(dateOfBirth);
		user.setEmail(userCreateResource.getEmail());

		return user;

	}

	private UserResource mapToUserResource(User user){

		UserResource userResource = new UserResource();
		userResource.setFirstName(user.getFirstName());
		userResource.setLastName(user.getLastName());
		userResource.setEmail(user.getEmail());
		userResource.setAge(user.getAge());
		userResource.setDateOfBirth(user.getDateOfBirth());
		userResource.setId(user.getId());

		return userResource;
	}
}
