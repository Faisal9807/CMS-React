package com.hcl.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CmsApplication {

	private static final Logger logger = LoggerFactory.getLogger(CmsApplication.class);

	public static void main(String[] args) {
		logger.info("Starting CMS Application...");
		SpringApplication.run(CmsApplication.class, args);
		logger.info("CMS Application started successfully.");
	}

}
