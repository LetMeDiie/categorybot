package kz.amihady.telegrambot.step;

import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryResponse;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.remove.RemoveSecondStep;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.Constants;
import kz.amihady.telegrambot.utils.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemoveSecondStepTest {

    @Mock
    private StepContext stepContext;

    @Mock
    private CategoryServiceClient categoryServiceClient;

    @Mock
    private Message message;

    private RemoveSecondStep removeSecondStep;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        removeSecondStep = new RemoveSecondStep(stepContext, categoryServiceClient);
    }

    @Test
    public void testProcess_NoTextMessage() {
        // Мокаем поведение сообщения
        when(message.hasText()).thenReturn(false);

        // Выполняем шаг
        StepResponse response = removeSecondStep.process(message);

        // Проверяем, что результат обработки не null
        assertNotNull(response);

        // Проверяем, что результат обработки был StepResult.RETRY
        assertEquals(StepResult.RETRY, response.stepResult());

        // Проверяем, что отправлено сообщение с ошибкой
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertTrue(responseSender.getErrorMessage().contains("Имя категорий которую нужно удалить должна быть в виде строки."));
    }

    @Test
    public void testProcess_CancelCommand() {
        // Мокаем поведение сообщения с командой /cancel
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/cancel");

        // Выполняем шаг
        StepResponse response = removeSecondStep.process(message);

        // Проверяем, что результат был отменен
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());

        // Проверяем, что отправлен текст отмены
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertTrue(responseSender.getMessage().contains("Команда /removeelement была успешно отменена. Данные не удалились"));
    }

    @Test
    public void testProcess_Success() {
        // Мокаем поведение сообщения с правильным названием категории
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Test Category");
        when(message.getChatId()).thenReturn(12345L);  // Мокаем chatId для пользователя

        CategoryResponse categoryResponse = new CategoryResponse("Категория успешно удалена");
        when(categoryServiceClient.deleteCategory(anyLong(), anyString())).thenReturn(categoryResponse);

        // Выполняем шаг
        StepResponse response = removeSecondStep.process(message);

        // Проверяем, что результат был успешным
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());

        // Проверяем, что отправлен правильный ответ
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertEquals("Категория успешно удалена", responseSender.getMessage());
    }

    @Test
    public void testProcess_FeignClientException() {
        // Мокаем поведение сообщения с правильным названием категории
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Test Category");

        FeignClientException feignClientException = mock(FeignClientException.class);
        when(categoryServiceClient.deleteCategory(anyLong(), anyString())).thenThrow(feignClientException);

        // Выполняем шаг
        StepResponse response = removeSecondStep.process(message);

        // Проверяем, что результат был StepResult.RETRY
        assertNotNull(response);
        assertEquals(StepResult.RETRY, response.stepResult());
    }

    @Test
    public void testProcess_RuntimeException() {
        // Мокаем поведение сообщения с правильным названием категории
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Test Category");

        RuntimeException runtimeException = new RuntimeException("Сервис не доступен");
        when(categoryServiceClient.deleteCategory(anyLong(), anyString())).thenThrow(runtimeException);

        // Выполняем шаг
        StepResponse response = removeSecondStep.process(message);

        // Проверяем, что результат был StepResult.ENDED
        assertNotNull(response);
        assertEquals(StepResult.ENDED, response.stepResult());

        // Проверяем, что отправлено сообщение с ошибкой
        ErrorTelegramResponseSender responseSender = (ErrorTelegramResponseSender) response.responseSender();
        assertEquals("Сервис не доступен", responseSender.getErrorMessage());
    }
}