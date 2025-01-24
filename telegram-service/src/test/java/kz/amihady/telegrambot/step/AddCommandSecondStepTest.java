package kz.amihady.telegrambot.step;


import kz.amihady.telegrambot.exception.ValidationException;
import kz.amihady.telegrambot.factory.CategoryRequestFactory;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.add.AddCommandSecondStep;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddCommandSecondStepTest {

    @Mock
    private StepContext stepContext;

    @Mock
    private CategoryRequestFactory categoryRequestFactory;

    @Mock
    private Message message;

    @Mock
    private CreateNewCategoryRequest categoryRequest;

    private AddCommandSecondStep addCommandSecondStep;

    @BeforeEach
    public void setUp() {
        addCommandSecondStep = new AddCommandSecondStep(stepContext, categoryRequestFactory);
    }

    @Test
    public void testProcess_Success() throws ValidationException {
        // Настроим моки
        String categoryName = "New Category";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(categoryName);
        when(categoryRequestFactory.createNewCategoryRequest(categoryName)).thenReturn(categoryRequest);

        // Вызываем метод process
        StepResponse response = addCommandSecondStep.process(message);

        // Проверяем, что шаг завершился успешно
        assertNotNull(response);
        assertEquals(StepResult.SUCCESS, response.stepResult());

        // Проверяем правильность возвращаемого ответа
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertTrue(responseSender.getMessage().contains("Что бы добавить новую категорию как корневую"));
    }

    @Test
    public void testProcess_Retry_InvalidInput() {
        when(message.hasText()).thenReturn(false);

        // Вызываем метод process
        StepResponse response = addCommandSecondStep.process(message);

        // Проверяем, что шаг вернул RETRY
        assertNotNull(response);
        assertEquals(StepResult.RETRY, response.stepResult());

        // Проверяем правильность возвращаемого сообщения
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertTrue(responseSender.getErrorMessage().contains("Имя категории должно представлять собой строку"));
    }

    @Test
    public void testProcess_CancelCommand() {
        // Настроим моки
        String input = "/cancel";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(input);

        // Вызываем метод process
        StepResponse response = addCommandSecondStep.process(message);

        // Проверяем, что шаг вернул CANCEL
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());  // Отмена возвращает назад

        // Проверяем правильность возвращаемого сообщения
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertTrue(responseSender.getMessage().contains("Команда /addelement была успешно отменена"));
    }

    @Test
    public void testProcess_ValidationException() throws ValidationException {
        // Настроим моки
        String categoryName = "InvalidCategory";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(categoryName);
        when(categoryRequestFactory.createNewCategoryRequest(categoryName)).thenThrow(new ValidationException("Невалидное имя категории"));

        // Вызываем метод process
        StepResponse response = addCommandSecondStep.process(message);

        // Проверяем, что шаг вернул RETRY из-за ошибки валидации
        assertNotNull(response);
        assertEquals(StepResult.RETRY, response.stepResult());

        // Проверяем правильность возвращаемого сообщения об ошибке
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertTrue(responseSender.getErrorMessage().contains("Попробуйте еще раз ввести имя категории правильно"));
    }
}