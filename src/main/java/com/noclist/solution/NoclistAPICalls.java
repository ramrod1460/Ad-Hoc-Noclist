package com.noclist.solution;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	private final RestTemplate template = new RestTemplate();

	@Autowired
	public void setApp(final NoclistProperties properties) {
		this.properties = properties;
	}

	/**
	 * Retrieve an AUTH token or empty string
	 *
	 * @return String Token
	 */
	@SuppressWarnings({"PMD.LocalVariableCouldBeFinal"})
	public Optional<String> getAuthToken() throws InterruptedException {

        final Retrier retrier = new Retrier(properties.getMaxAttempts(), properties.getSleepTime());

        // Provide a Supplier ( body of the Lambda ) written as a java 8 lambda to the retry method.
		// In case of any error, our supplier returns an empty optional which indicates that another try is required
		return retrier.retry(() -> {
			// Per instructions ...avoid the -large amount of useless data-  associated with the response body
			// by pulling in only the response headers.
			// Void as a response body type indicates that body is not required and is going to be discarded
			final ResponseEntity<Void> response = template.exchange(properties.getBase() + properties.getAuth(),
					HttpMethod.GET, null, Void.class);
			HttpHeaders headers = response.getHeaders();

			String token = headers.getFirst(properties.getBadsecAuthenticationToken());
			if (response.getStatusCode() == HttpStatus.OK && token != null) {
				System.err.println("Badsec-Authentication-Token security token is :" + token);
				return Optional.of(token);
			} else {
				System.err.println(properties.getBadsecAuthenticationToken() + " unavailable ...status code: " + response.getStatusCode());
				return Optional.empty();
			}
		});

	}

	/**
	 * Call API to retrieve user list
	 *
	 * @param securityChecksum from getAuthToken
	 * @return list of users
	 */
	@SuppressWarnings({"PMD.LocalVariableCouldBeFinal"})
	public Optional<List<String>> userList(final String securityChecksum) throws InterruptedException {

		// Set the headers you need send
		final HttpHeaders userHeaders = new HttpHeaders();
		userHeaders.set(properties.getXRequestToken(), securityChecksum);

		// Set security checksum in header
		final HttpEntity<String> entity = new HttpEntity<>(userHeaders);
        final Retrier retrier = new Retrier(properties.getMaxAttempts(), properties.getSleepTime());

        return retrier.retry(() -> {
            ResponseEntity<String> response = template.exchange(properties.getBase() + properties.getUsers(),
                    HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
                return Optional.of(Arrays.asList(response.getBody().split("\n")));
            } else {
				System.err.println(" User API call failed ...status code: "+response.getStatusCode());
                return Optional.empty();
            }

        });
	}
}
