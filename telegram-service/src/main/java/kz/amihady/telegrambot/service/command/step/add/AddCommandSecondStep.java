package kz.amihady.telegrambot.service.command.step.add;

import kz.amihady.telegrambot.exception.ValidationException;
import kz.amihady.telegrambot.factory.CategoryRequestFactory;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.service.command.step.Step;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.CommandType;
import kz.amihady.telegrambot.utils.Constants;
import kz.amihady.telegrambot.utils.StepResult;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;


//после того как получаем сообщение от клиента попытаем создать категорию
//предполагается что пользователь передаст имя в виде текста иначе вернем ошибку.

@Slf4j
public class AddCommandSecondStep extends Step {

    private CategoryRequestFactory categoryRequestFactory;

    public AddCommandSecondStep(StepContext stepContext, CategoryRequestFactory categoryRequestFactory) {
        super(stepContext);
        this.categoryRequestFactory = categoryRequestFactory;
    }

    @Override
    public StepResponse process(Message message) {
        log.info("Обработка второго шага команды добавления категории. ID пользователя: {}", message.getChatId());

        if (!message.hasText()) { // Проверяем сообщение, полученное после первого шага
            String errorMessage = "Имя категории должно представлять собой строку. " +
                    "Попробуйте еще раз ввести имя категории." + Constants.CANCEL_TEXT;
            log.warn("Сообщение не содержит текст. Отправлен ошибочный ответ.");
            TelegramResponseSender response =
                    new ErrorTelegramResponseSender(errorMessage);
            return new StepResponse(StepResult.RETRY,response);
        }

        String input = message.getText();
        if (CommandType.CANCEL == CommandType.getCommand(input)) {
            log.info("Команда /cancel была использована пользователем. Команда отменена.");
            String cancelMessage = ("Команда /addelement была успешно отменена.\nДанные не сохранились.");
            return cancel(cancelMessage);
        }

        try {
            // Может вернуть исключение валидации
            CreateNewCategoryRequest request = categoryRequestFactory.createNewCategoryRequest(input);
            stepContext.put("request",request);
            log.info("Категория '{}' успешно создана. Переход к следующему шагу.", input);
            TelegramResponseSender responseSender =
                    new TelegramTextResponseSender(
                            "Что бы добавить новую категорию как корневую, введите /root. " +
                            "Что бы добавить категорию как дочернюю, введите имя родительской категории. " +
                            Constants.CANCEL_TEXT);
            return new StepResponse(StepResult.SUCCESS,responseSender);

        } catch (ValidationException exception) {
            log.error("Ошибка валидации при создании категории: {}", exception.getMessage());
            TelegramResponseSender responseSender =
                    new ErrorTelegramResponseSender(
                            "Попробуйте еще раз ввести имя категории правильно. \n" +
                            exception.getMessage() + Constants.CANCEL_TEXT);
            return new StepResponse(StepResult.RETRY,responseSender);
        }
    }
}
