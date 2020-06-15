package be.pxl.auctions.util;

import be.pxl.auctions.model.User;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

public class EmailValidatorIsValidTest {

	private EmailValidator emailValidator;
	@BeforeEach
	public void init() {
		emailValidator = new EmailValidator();
	}
	@Test
	public void ReturnsTrueWhenValidEmail() {
		var email = "valid@email.be";
		Assertions.assertTrue(EmailValidator.isValid(email));
	}

	@Test
	public void ReturnsFalseWhenAtSignMissing() {
		var email = "invalidEmail.be";
		Assertions.assertFalse(EmailValidator.isValid(email));
	}

}
