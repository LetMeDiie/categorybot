package kz.amihady.telegrambot.service.command.step;

import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.StepResult;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class Step {
    protected StepContext stepContext;

    public Step(StepContext stepContext) {
        this.stepContext = stepContext;
    }

    public abstract StepResponse process(Message message);

    public StepResponse cancel(String cancelMessage){
        return new StepResponse(
                StepResult.ENDED,
                new TelegramTextResponseSender(cancelMessage));
    }

}
