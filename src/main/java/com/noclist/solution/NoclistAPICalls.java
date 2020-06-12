package com.noclist.solution;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;


/**
 * Support for Noclist interaction with Noclist server
 * 
 * @author User
 *
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize","PMD.AvoidFinalLocalVariable","PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.CommentSize","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
@Service
public class NoclistAPICalls {

	// Inject via application.properties
	private NoclistProperties properties;

	@Autowired
	public void setApp(final NoclistProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * Retrieve a AUTH token or empty string
	 * 
	 * @return String Token
	 */
	public String getAuthToken() throws InterruptedException {

		final RestTemplate template = new RestTemplate(); 
		int attempts = properties.getMaxAttempts();

		// Retrieve a security token
		final ResponseEntity<String> response = null; 
		String authToken = ""; 
		HttpHeaders headers = null; 

		// Alternatively use org.springframework.web.client.ResponseExtractor on HttpResponse to drop
		// body while still being able to view HttpResponse
		
		do {
			attempts--;

			try {
				// Per instructions ...avoid the -large amount of useless data-  associated with the response body
				// by pulling in only the response headers.
				headers = template.headForHeaders(properties.getBase() + properties.getAuth());
				
				if ( headers != null && headers.containsKey(properties.getBadsecAuthenticationToken()) && ! headers.getFirst(properties.getBadsecAuthenticationToken()).isEmpty() ) {
					authToken = headers.getFirst(properties.getBadsecAuthenticationToken());
					System.err.println("Badsec-Authentication-Token security token is :"+authToken);
					break;
				} else {
					// Give server a moment to -potentially- recover
					Thread.sleep(properties.getSleepTime());
					System.err.println(properties.getBadsecAuthenticationToken()+" unavailable ...status code: "+response.getStatusCode());
				}
			} catch(ResourceAccessException e) {
				System.err.println(properties.getBadsecAuthenticationToken()+" unexpected exception: "+e.getLocalizedMessage());
				Thread.sleep(properties.getSleepTime());
			} 

		} while (response == null && attempts > 0);

		return authToken;

	}

	/**
	 * Call API to retrieve user list
	 * 
	 * @param securityChecksum from getAuthToken
	 * @return void
	 */
	public int userList(final String securityChecksum) throws InterruptedException {

		int attempts = properties.getMaxAttempts();
		int errorCode = 1;
		
		// Set the headers you need send
		final HttpHeaders userHeaders = new HttpHeaders();
		userHeaders.set(properties.getXRequestToken(), securityChecksum);

		// JSON rendering
		final JSONArray jsArray = new JSONArray();
		
		// Set security checksum in header
		final HttpEntity<String> entity = new HttpEntity<>(userHeaders); 
		final RestTemplate template = new RestTemplate(); 
		ResponseEntity<String> response = null; 
		
		// Attempt to retrieve a valid response from API
		do {
			attempts--;
			
			try {
				// call users API
				response = template.exchange(properties.getBase()+properties.getUsers(), HttpMethod.GET, entity, String.class); 
			} catch(ResourceAccessException e) {
				System.err.println("User API unexpected exception : "+e.getLocalizedMessage());
				continue;
			}
			
			// Ensure we received a proper response
			if ( response.getStatusCode().equals(HttpStatus.OK) ) {
				errorCode = 0;
				break;
			} 
			
			System.err.println(" User API call failed ...status code: "+response.getStatusCode());
			Thread.sleep(properties.getSleepTime());

		} while ( response == null && attempts > 0 );

		if ( errorCode == 0)  {

			// Render JSON output

			Arrays
			.stream(response.getBody().split("\n"))
			.parallel()
			.forEach(e -> jsArray.add(e));
			
			System.out.println(jsArray.toString());
		} 

		return errorCode;
	}
}
