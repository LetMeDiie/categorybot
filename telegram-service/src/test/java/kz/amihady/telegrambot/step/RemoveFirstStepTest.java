package kz.amihady.telegrambot.step;

import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.remove.RemoveFirstStep;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.Constants;
import kz.amihady.telegrambot.utils.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class RemoveFirstStepTest {

    @Mock
    private StepContext stepContext;

    @Mock
    private Message message;

    private RemoveFirstStep removeFirstStep;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        removeFirstStep = new RemoveFirstStep(stepContext);
    }

    @Test
    public void testProcess_Success() {
        // Мокаем поведение сообщения
        String inputMessage = "Введите имя категорий которую хотите удалить.";
        when(message.hasText()).thenReturn(true);  // Мы не проверяем содержание текста, так как это первый шаг
        when(message.getText()).thenReturn(inputMessage);  // Текст не важен, в первом шаге мы просто отвечаем

        // Выполняем шаг
        StepResponse response = removeFirstStep.process(message);

        // Проверяем, что ответ не null
        assertNotNull(response);

        // Проверяем, что результат выполнения был SUCCESS
        assertEquals(StepResult.SUCCESS, response.stepResult());

        // Проверяем, что ответный текст соответствует ожиданиям
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertEquals("Введите имя категорий которую хотите удалить." + Constants.CANCEL_TEXT, responseSender.getMessage());
    }
}