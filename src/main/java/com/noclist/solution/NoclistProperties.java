package com.noclist.solution;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="noclist")
@Data
@SuppressWarnings({"PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
public class NoclistProperties {

	// Users part of URL for API calls
	private String users;

	// URL for API calls
	private String urlUsers;
	private String urlAuthToken;

	// Header tokens 
	private String badsecAuthenticationToken;
	private String xRequestToken;

	// Max attempts to API calls
	private int maxAttempts;

	// Sleep time on API retry ...can be 0 
	private Long sleepTime;

}
