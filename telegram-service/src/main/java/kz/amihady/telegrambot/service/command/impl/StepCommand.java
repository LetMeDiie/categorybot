package kz.amihady.telegrambot.service.command.impl;

import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.command.step.Step;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.utils.StepResult;
import org.telegram.telegrambots.meta.api.objects.Message;

public class StepCommand implements Command {
    private Step [] steps;
    private int stepIndex;
    private boolean isCompleted;

    public StepCommand(Step[] steps) {
        this.steps = steps;
        this.stepIndex=0;
        this.isCompleted=false;
    }

    @Override
    public TelegramResponseSender process(Message message) {
        Step step = steps[stepIndex];
        StepResponse stepResponse = step.process(message);
        TelegramResponseSender responseSender = stepResponse.responseSender();
        StepResult stepResult = stepResponse.stepResult();
        if(stepResult == StepResult.ENDED) {
            this.isCompleted=true;
        }
        stepIndex+=stepResult.getValue();
        return responseSender;
    }

    @Override
    public boolean isCompleted() {
        return this.isCompleted;
    }
}
