package com.noclist.solution;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Validate that requisite app properties exist & are valid
 * 
 * @author User
 *
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize","PMD.AtLeastOneConstructor","PMD.AvoidFinalLocalVariable","PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.CommentSize","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
@Service
public class ValidProperties {

	// Inject via application.properties
	private NoclistProperties properties;

	@Autowired
	public void setApp(final NoclistProperties properties) {
		this.properties = properties;
	}

	/**
	 * Ensure the application.properties are valid
	 * 
	 * @return
	 */
	@SuppressWarnings({"PMD.NPathComplexity","PMD.CyclomaticComplexity"})
	public boolean validateProperties() {

		boolean validProperties = true;

		// Validate noclist URLs
		if ( properties.getBase() == null || ! isUrlValid(properties.getBase())) {
			System.err.println("The application.properties element noclist.base is invaild.");
			validProperties = false;
		}
		if ( properties.getUsers() == null || ! isUrlValid(properties.getBase()+properties.getUsers())) {
			System.err.println("The application.properties element noclist.users is invaild.");
			validProperties = false;
		}
		if ( properties.getAuth() == null || ! isUrlValid(properties.getBase()+properties.getAuth())) {
			System.err.println("The application.properties element noclist.auth is invaild.");
			validProperties = false;
		}

		// Validate header elements exist
		if ( properties.getBadsecAuthenticationToken() == null || 
			 properties.getBadsecAuthenticationToken().trim().length() < 1 ) {
			System.err.println("The application.properties element noclist.BadsecAuthenticationToken is invaild.");
			validProperties = false;
		}
		
		if ( properties.getXRequestToken() == null || 
			 properties.getXRequestToken().trim().length() < 1 ) {
             System.err.println("The application.properties element noclist.getXRequestToken is invaild.");
             validProperties = false;
		}	
		
		// Validate control elements exist
		if ( properties.getMaxAttempts() < 0 ) {
	         System.err.println("The application.properties element noclist.getMaxAttempts is invaild... must be >= 0");
	         validProperties = false;
		}	
		if ( properties.getSleepTime() < 0 ) {
             System.err.println("The application.properties element noclist.getSleepTime is invaild... must be >= 0");
             validProperties = false;
		}			

		return validProperties;
	}

	/**
	 * Validate that a URL is valid
	 * 
	 * @param url
	 * @return boolean true if URL is ok
	 */
	public static boolean isUrlValid(final String url) {
		
		boolean isValid = false;
		
		try {
			final URL obj = new URL(url);
			obj.toURI();
			isValid = true;
		} catch (MalformedURLException | URISyntaxException e) {
			System.err.println("URL validation check exception : "+e.getLocalizedMessage());
		}
		
		return isValid;
	}
	

}
