package kz.amihady.telegrambot.step;


import feign.FeignException;
import kz.amihady.telegrambot.feign.CategoryServiceClient;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.feign.response.CategoryResponse;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.add.AddCommandThirdStep;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddCommandThirdStepTest {

    @Mock
    private StepContext stepContext; // Мокируем StepContext

    @Mock
    private CategoryServiceClient categoryService; // Мокируем CategoryServiceClient

    @Mock
    private Message message; // Мокируем Message

    @Mock
    private CreateNewCategoryRequest categoryRequest; // Мокируем CreateNewCategoryRequest

    @Mock
    private CategoryResponse categoryResponse; // Мокируем CategoryResponse

    private AddCommandThirdStep addCommandThirdStep;

    @BeforeEach
    public void setUp() {
        addCommandThirdStep = new AddCommandThirdStep(stepContext, categoryService);
    }

    @Test
    public void testProcess_SuccessRootCategory() throws FeignException {
        // Настроим моки
        String input = "/root";
        Long userId = 123L;
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(input);
        when(message.getChatId()).thenReturn(userId);
        when(stepContext.get("request", CreateNewCategoryRequest.class)).thenReturn(categoryRequest);
        when(categoryService.addRootCategory(userId, categoryRequest)).thenReturn(categoryResponse);
        when(categoryResponse.message()).thenReturn("Категория успешно добавлена.");

        // Вызываем метод process
        StepResponse response = addCommandThirdStep.process(message);

        // Проверяем успешный результат
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertEquals("Категория успешно добавлена.", responseSender.getMessage());
    }

    @Test
    public void testProcess_SuccessChildCategory() throws FeignException {
        // Настроим моки
        String input = "ParentCategory";
        Long userId = 123L;
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(input);
        when(message.getChatId()).thenReturn(userId);
        when(stepContext.get("request", CreateNewCategoryRequest.class)).thenReturn(categoryRequest);
        when(categoryService.addChildCategory(userId, input, categoryRequest)).thenReturn(categoryResponse);
        when(categoryResponse.message()).thenReturn("Категория успешно добавлена.");

        // Вызываем метод process
        StepResponse response = addCommandThirdStep.process(message);

        // Проверяем успешный результат
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertEquals("Категория успешно добавлена.", responseSender.getMessage());
    }

    @Test
    public void testProcess_Retry_InvalidInput() {
        // Настроим моки
        when(message.hasText()).thenReturn(false);

        // Вызываем метод process
        StepResponse response = addCommandThirdStep.process(message);

        // Проверяем, что шаг вернул RETRY
        assertNotNull(response);
        assertEquals(StepResult.RETRY, response.stepResult());

        // Проверяем правильность возвращаемого сообщения
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertTrue(responseSender.getErrorMessage().contains("Данные для команды /addelement должны быть в виде строки"));
    }

    @Test
    public void testProcess_CancelCommand() {
        // Настроим моки
        String input = "/cancel";
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(input);

        // Вызываем метод process
        StepResponse response = addCommandThirdStep.process(message);

        // Проверяем, что шаг вернул CANCEL
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult()); // Ожидаем завершения команды после отмены

        // Проверяем правильность возвращаемого сообщения
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertTrue(responseSender.getMessage().contains("Команда /addelement была успешно отменена"));
    }

    @Test
    public void testProcess_RuntimeError() {
        // Настроим моки
        String input = "NewCategory";
        Long userId = 123L;
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(input);
        when(message.getChatId()).thenReturn(userId);
        when(stepContext.get("request", CreateNewCategoryRequest.class)).thenReturn(categoryRequest);

        // Смокируем ошибку на стороне сервиса, которая выбрасывает RuntimeException
        RuntimeException runtimeException = new RuntimeException("Сервис не доступен");
        when(categoryService.addChildCategory(userId, input, categoryRequest)).thenThrow(runtimeException);

        // Вызываем метод process
        StepResponse response = addCommandThirdStep.process(message);

        // Проверяем, что шаг завершился
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());

        // Проверяем правильность возвращаемого сообщения об ошибке
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertTrue(responseSender.getErrorMessage().contains("Сервис не доступен"));
    }
}
