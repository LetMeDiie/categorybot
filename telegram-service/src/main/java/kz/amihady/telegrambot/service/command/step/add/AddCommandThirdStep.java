package kz.amihady.telegrambot.service.command.step.add;

import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryServiceClient;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.feign.response.CategoryResponse;
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


@Slf4j
public class AddCommandThirdStep extends Step {

    private CategoryServiceClient categoryService;

    public AddCommandThirdStep(StepContext stepContext, CategoryServiceClient categoryService) {
        super(stepContext);
        this.categoryService = categoryService;
    }

    @Override
    public StepResponse process(Message message) {
        log.info("Обработка третьего шага команды добавления категории. ID пользователя: {}", message.getChatId());

        if (!message.hasText()) { // Проверяем, если не строка, возвращаем ошибку
            String errorMessage =
                    "Данные для команды /addelement должны быть в виде строки.\n" +
                    "Попробуйте ввести имя категорию еще раз." + Constants.CANCEL_TEXT;
            log.warn("Сообщение не содержит текст. Отправлен ошибочный ответ.");
            TelegramResponseSender responseSender =
                    new ErrorTelegramResponseSender(errorMessage);
            return new StepResponse(StepResult.RETRY,responseSender);
        }

        String input = message.getText(); // Получаем строку от клиента
        if (CommandType.CANCEL == CommandType.getCommand(input)) { // Если данные равно /cancel, отменяем команду
            log.info("Команда /cancel была использована пользователем. Команда отменена.");
            String cancelMessage = "Команда /addelement была успешно отменена.\nДанные не изменились.";
            return cancel(cancelMessage);
        }

        return addCategory(input, message.getChatId());
    }

    private StepResponse addCategory(String input, Long userId) {
        log.info("Попытка добавления категории. Имя: {}, ID пользователя: {}", input, userId);
        CreateNewCategoryRequest request =
                stepContext.get("request",CreateNewCategoryRequest.class); //получаем из контекста переданный реквест из 2 шага
        TelegramResponseSender responseSender ;
        StepResult stepResult ;
        try {
            CategoryResponse categoryResponse =
                    CommandType.ROOT == CommandType.getCommand(input) ?
                            categoryService.addRootCategory(userId, request) : // Если пользователь ввел /root, отправляем запрос сюда
                            categoryService.addChildCategory(userId, input, request); // Иначе это имя родительской категории

            log.info("Категория успешно добавлена. Сообщение: {}", categoryResponse.message());
            responseSender = new TelegramTextResponseSender(categoryResponse.message());
            stepResult = StepResult.ENDED;

        } catch (FeignClientException exception) { //ошибка на стороне сервера с кодом not found or conflict , скорее всегда это исключение которое возникло в логике приложения и мы можем ввести другое имя для категорий
            log.error("Ошибка при добавлении категории: {}", exception.getMessage());
            responseSender =  new ErrorTelegramResponseSender(
                    exception.getMessage() + "\nПовторите еще раз." + Constants.CANCEL_TEXT);
            stepResult = StepResult.BACK; //возвращаемся на 2 шаг
        } catch (RuntimeException exception) { //это значит что категория сервис не доступен и просто отменяем команду
            stepResult=StepResult.ENDED;
            log.error("ошибка из сервиса: {}", exception.getMessage());
            responseSender = new ErrorTelegramResponseSender(exception.getMessage());
        }
        return new StepResponse(stepResult,responseSender);
    }
}