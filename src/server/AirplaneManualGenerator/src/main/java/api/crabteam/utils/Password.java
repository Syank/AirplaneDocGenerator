package api.crabteam.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * A class to represent a password for an user, providing methods to handle it with it's encryption by the jBCrypt library
 * <p>
 * This class did not store the real/readable password value, in other words, after the password be set with the method
 * <i>setValue</i>, the class immediately encrypts it using the jBCrypt, so, when the method <i>getValue</i> be called, it
 * will return the encrypted version of the password
 * 
 * @author Rafael Furtado
 */
public class Password {
	private String value;
	
	private static final int PASSWORD_SALT = 5;  // A higher salt value results in a more safe password, but demands more CPU process for it

	public Password() {
		
	}
	
	public Password(String hashedPassword) {
		this.value = hashedPassword;
		
	}
	
	/**
	 * Checks if the given password is the same as the encrypted one for this class instance
	 * 
	 * @param recievedPassword - The password to check
	 * @return If the given password is the same as the encrypted one, returns <b>true</b>, if it is not <b><i>OR</i></b>
	 *         if the class instance did not have a password value set, returns <b>false</b>
	 * @author Rafael Furtado
	 */
	public boolean checkPassword(String recievedPassword) {
		if(this.value == null) {
			return false;
		}
		
		return BCrypt.checkpw(recievedPassword, this.value);
	}
	

	
	/**
	 * Gets the password value for this object
	 * <p>
	 * The returned value will be the encrypted version of the value passed to the method <i>setValue</i> of the class
	 * 
	 * @return Returns the encrypted version of the value passed to the method <i>setValue</i> of the object.
	 *         If the password is not set yet, this method will return <b>null</b>
	 * @author Rafael Furtado
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Encrypts the given value and sets it's as the password value
	 * 
	 * @param value - The password value to set for the object
	 * @author Rafael Furtado
	 */
	public void setValue(String value) {
		String salt = BCrypt.gensalt(PASSWORD_SALT);
		
		String hashedPassword = BCrypt.hashpw(value, salt);
		
		this.value = hashedPassword;
		
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}
	
}
