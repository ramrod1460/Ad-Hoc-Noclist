package com.noclist.solution;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
public class NoclistRunner implements CommandLineRunner {

	// Inject via application.properties
	private NoclistProperties properties;

	@Autowired
	public void setApp(NoclistProperties properties) {
	    this.properties = properties;
	}
	
	@Autowired
	private NoclistAPICalls apiCall;
	
	@Override
	public void run(final String... args) {

		// Set system exit status - assume failure
		int exitStatus = 1;

		// Retrieve a security token
		try {
			final String token = apiCall.getAuthToken();

			if ( ! token.isEmpty() ) {
				// Continue processing to retrieve user list
				System.err.println("Badsec-Authentication-Token security token is :"+token);

				// Create a SHA256 checksum token
				final String sha256hex = DigestUtils.sha256Hex(token+properties.getUsers());
				System.err.println("Checksum Token :"+sha256hex);

				// Make call to retrieve list of users - I assume an empty list is ok
				exitStatus = apiCall.userList(sha256hex);
			} 
		} catch ( InterruptedException e ) {
			System.err.println("Unexpected InterruptedException exception: "+e.getLocalizedMessage());
			exitStatus = 1;
		} catch ( Exception e ) {
			System.err.println("Unexpected exception: "+e.getLocalizedMessage());
			exitStatus = 2;
		}

		System.err.println("Exit status : "+exitStatus);
		System.exit(exitStatus);
	}

}
