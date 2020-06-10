package com.noclist.solution;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 
/**
 * Spring Boot root class
 * 
 * @author User
 *
 */
@SuppressWarnings("PMD.UseUtilityClass")
@SpringBootApplication
public class NoclistDriver {
	
	/**
	 * Spring Boot CommandLineRunner app
	 * 
	 * @param args
	 */
    public static void main(final String[] args) {
 
    	// Banner disabled in properties
    	SpringApplication.run(NoclistDriver.class, args);
    	
    }

}