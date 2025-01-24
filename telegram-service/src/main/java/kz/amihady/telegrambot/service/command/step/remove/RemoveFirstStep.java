package kz.amihady.telegrambot.service.command.step.remove;

import kz.amihady.telegrambot.service.command.step.Step;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.Constants;
import kz.amihady.telegrambot.utils.StepResult;
import org.telegram.telegrambots.meta.api.objects.Message;

public class RemoveFirstStep extends Step {

    public RemoveFirstStep(StepContext stepContext) {
        super(stepContext);
    }

    @Override
    public StepResponse process(Message message) {
        //первый степ шаг когда пользователь только выбрал команду
        String response = "Введите имя категорий которую хотите удалить."+ Constants.CANCEL_TEXT;
        TelegramResponseSender responseSender =
                new TelegramTextResponseSender(response);
        return new StepResponse(StepResult.SUCCESS,responseSender);
    }


}
