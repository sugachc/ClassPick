package com.classpick.springbootproject.telegram;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class ExampleTasklet implements Tasklet {


    private final TelegramAPIClient telegramAPIClient;

    public ExampleTasklet(TelegramAPIClient telegramAPIClient) {
        this.telegramAPIClient = telegramAPIClient;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("send message start");
        telegramAPIClient.sendMessage("6288354229:AAG_WmdY1Xnz-j_YkgTMUrI7-RZpT0y1bxw","hello bot", "5823214610");
        log.info("send message finished");
        return RepeatStatus.FINISHED;
    }

}
