package com.noclist.solution;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="noclist")
@Data
@SuppressWarnings({"PMD.AvoidCatchingGenericException","PMD.AtLeastOneConstructor","PMD.SystemPrintln","PMD.LongVariable","PMD.LawOfDemeter","PMD.CommentRequired","PMD.DataflowAnomalyAnalysis","PMD.DoNotCallSystemExit"})
public class NoclistProperties {

	// base of URI for API calls
	private String base;
	// users call part of base URI 
	private String users;
	// auth call part of base URI 
	private String auth;

	// Header tokens 
	private String badsecAuthenticationToken;
	private String xRequestToken;

	// Max attempts to API calls
	private int maxAttempts;

	// Sleep time on API retry ...can be 0 
	private Long sleepTime;

}
