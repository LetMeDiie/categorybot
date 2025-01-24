package kz.amihady.telegrambot.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.add.AddCommandFirstStep;
import kz.amihady.telegrambot.service.command.step.stepresponse.StepResponse;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AddCommandFirstStepTest {

    @Mock
    private StepContext stepContext;

    @Mock
    private Message message;

    private AddCommandFirstStep addCommandFirstStep;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализируем моки
        addCommandFirstStep = new AddCommandFirstStep(stepContext);
    }

    @Test
    public void testProcess_Success() {
        when(message.getChatId()).thenReturn(12345L);

        // Вызываем метод process
        StepResponse response = addCommandFirstStep.process(message);

        // Проверяем, что шаг завершился успешно
        assertNotNull(response);
        assertEquals(StepResult.SUCCESS, response.stepResult());

        // Проверяем, что responseSender вернул правильное сообщение
        TelegramTextResponseSender responseSender = (TelegramTextResponseSender) response.responseSender();
        assertTrue(responseSender.getMessage().contains("Введите имя категории"));
    }
}