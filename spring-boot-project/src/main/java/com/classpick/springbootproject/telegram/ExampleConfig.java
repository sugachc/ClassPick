package com.classpick.springbootproject.telegram;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TelegramAPIClient telegramAPIClient;

    public ExampleConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, TelegramAPIClient telegramAPIClient) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.telegramAPIClient = telegramAPIClient;
    }

    @Bean
    public Job tutorialJob() {
        return jobBuilderFactory.get("exampleJob")
                .start(tutorialStep())  // Step 설정
                .build();
    }

    @Bean
    public Step tutorialStep() {
        return stepBuilderFactory.get("exampleStep")
                .tasklet(new ExampleTasklet(telegramAPIClient))
                .build();
    }
}