package kz.amihady.telegrambot.service.command.step.add;

import kz.amihady.telegrambot.service.command.step.Step;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.Constants;
import kz.amihady.telegrambot.utils.StepResult;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class AddCommandFirstStep extends Step {

    public AddCommandFirstStep(StepContext stepContext) {
        super(stepContext);
    }

    @Override
    public StepResponse process(Message message) {
        log.info("Начат первый шаг команды добавления категории. ID пользователя: {}", message.getChatId());

        String responseMessage =
                "Введите имя категории, которую хотите добавить." + Constants.CANCEL_TEXT;
        TelegramResponseSender responseSender =
                new TelegramTextResponseSender(responseMessage);

        log.info("Первый шаг успешно завершен. Ожидание ввода имени категории от пользователя: {}", message.getChatId());

        return new StepResponse(StepResult.SUCCESS,responseSender);


    }
}
