package net.najiboulhouch.redditcloneapp;

import net.najiboulhouch.redditcloneapp.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RedditCloneAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneAppApplication.class, args);
	}

}
