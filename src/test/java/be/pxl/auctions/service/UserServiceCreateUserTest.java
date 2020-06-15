package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserResource;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceCreateUserTest {

	// TODO add unit tests for all possible scenario's of the createUser method
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;
    private UserCreateResource user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        user = new UserCreateResource();
        user.setEmail("mark@facebook.com");
        user.setDateOfBirth("27/04/1977");
        user.setFirstName("Mark");
        user.setLastName("Zuckerberg");

    }

    @Test
    public void ValidBidIsSaved() throws InvalidDateException, RequiredFieldException, InvalidEmailException, DuplicateEmailException {
        when(userDao.findUserByEmail(anyString())).thenReturn(null);

        userService.createUser(user);

        verify(userDao).saveUser(any(User.class));
    }

    @Test
    public void EmptyFirstNameThrowsRequiredFieldException(){
        user.setFirstName(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void EmptyLastNameThrowsRequiredFieldException(){
        user.setLastName(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void EmptyEmailThrowsRequiredFieldException(){
        user.setEmail(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void InvalidEmailThrowsInValidEmailException(){
        user.setEmail("ThisIsNotAValidEmail");

        assertThrows(InvalidEmailException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void EmptyDateOfBirthThrowsRequiredFieldException(){
        user.setDateOfBirth("");

        assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void FutureDateOfBirthThrowsInvalidDateException(){
        user.setDateOfBirth("10/10/2030");

        assertThrows(InvalidDateException.class, () -> userService.createUser(user));
        verifyNoInteractions(userDao);
    }

    @Test
    public void ExistingEmailThrowsDuplicateEmailException(){
        var tempUser = new User();
        tempUser.setEmail(user.getEmail());
        when(userDao.findUserByEmail(user.getEmail())).thenReturn(tempUser);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
        verify(userDao, never()).saveUser(any(User.class));
    }
}
