package com.noclist.solution;

import java.util.List;
import java.util.Optional;

import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Main driver class for retrieving the user list
 * 
 * Relies on separate method calls for deriving a security token and the actual
 * list of users
 * 
 * Relies on entries in the applicatino.properties file to direct to the appropriate
 * noclist server address as well as for the specification of the appropriate http
 * header token names/values ....this is specified in properties file for flexibility.
 * 
 * @author User
 *
 */
@Service
@SuppressWarnings({"PMD.BeanMembersShouldSerialize","PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.CommentSize","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
public class NoclistRunner implements CommandLineRunner {

	@Autowired
	private ValidProperties validProperties;

	@Autowired
	private NoclistAPICalls noclistAPICalls;

	@Autowired
	private ChecksumGenerator checksumGenerator;

	@Override
	public void run(final String... args) {

		// Set system exit status - assume failure
		int exitStatus = 1;

		// Validate requisite app properties exist
		if (validProperties.validateProperties() ) {
			try {

				// Retrieve a security token
				final Optional<String> token = noclistAPICalls.getAuthToken();

				if (token.isPresent()) {
					// Continue processing to retrieve user list

					// Create a SHA256 checksum token
					final String sha256hex = checksumGenerator.generateChecksum(token.get());

					// Make call to retrieve list of users - I *assume* an empty list is ok
					final Optional<List<String>> userList = noclistAPICalls.userList(sha256hex);
					if (userList.isPresent()) {
						// to JSON
						final JSONArray jsArray = new JSONArray();
						jsArray.addAll(userList.get());
						System.out.println(jsArray.toString());
						exitStatus = 0;
					}
				} else {
					System.err.println("Cannot get the auth token within the max number of attempts");
				}

			} catch ( InterruptedException e ) {
				System.err.println("Unexpected InterruptedException exception: "+e.getLocalizedMessage());
				exitStatus = 1;
			} catch ( Exception e ) {
				System.err.println("Unexpected exception: "+e.getLocalizedMessage());
				exitStatus = 2;
			}
		}

		System.err.println("Exit status : "+exitStatus);
		System.exit(exitStatus);
	}

}
