package com.jabaddon.tutorials.embabel.basic_embabel_agent;

import com.embabel.agent.config.annotation.EnableAgentShell;
import com.embabel.agent.config.annotation.EnableAgents;
import com.embabel.agent.config.annotation.LocalModels;
import com.embabel.agent.config.annotation.LoggingThemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAgents(
		loggingTheme = LoggingThemes.STAR_WARS,
		localModels = {LocalModels.OLLAMA}
)
@EnableAgentShell // Enable the agent shell
public class BasicEmbabelAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicEmbabelAgentApplication.class, args);
	}

}
