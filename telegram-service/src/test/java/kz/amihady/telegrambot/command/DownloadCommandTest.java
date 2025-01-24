package kz.amihady.telegrambot.command;


import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryTreeExelResponse;
import kz.amihady.telegrambot.service.command.impl.DownloadCommand;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramFileResponseSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DownloadCommandTest {

    @Mock
    private CategoryTreeServiceClient categoryTreeService;

    @Mock
    private Message message;

    @Mock
    private CategoryTreeExelResponse categoryTreeExelResponse;

    private DownloadCommand downloadCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        downloadCommand = new DownloadCommand(categoryTreeService);
    }

    @Test
    public void testProcess_Success() {
        // Мокаем поведение метода getTreeAsFile
        byte [] file = new byte[0];
        when(message.getChatId()).thenReturn(12345L);  // Мокаем chatId для пользователя
        when(categoryTreeService.getTreeAsFile(anyLong())).thenReturn(categoryTreeExelResponse);
        when(categoryTreeExelResponse.file()).thenReturn(file);

        // Выполняем команду
        TelegramResponseSender response = downloadCommand.process(message);

        // Проверяем, что ответ был TelegramFileResponseSender
        assertNotNull(response);
        assertTrue(response instanceof TelegramFileResponseSender);

        // Проверяем, что имя файла в ответе правильное
        TelegramFileResponseSender fileResponseSender = (TelegramFileResponseSender) response;
        assertEquals("category.xlsx", fileResponseSender.getFileName());
        assertEquals(file,fileResponseSender.getFileData());
    }

    @Test
    public void testProcess_FeignClientException() {
        // Мокаем поведение метода getTreeAsFile, чтобы он выбросил FeignClientException
        when(message.getChatId()).thenReturn(12345L);
        FeignClientException feignClientException = mock(FeignClientException.class);
        when(categoryTreeService.getTreeAsFile(anyLong())).thenThrow(feignClientException);

        // Выполняем команду
        ErrorTelegramResponseSender response =(ErrorTelegramResponseSender) downloadCommand.process(message);

        // Проверяем, что возвращается ErrorTelegramResponseSender
        assertNotNull(response);
        assertTrue(response instanceof ErrorTelegramResponseSender);
    }

    @Test
    public void testProcess_RuntimeException() {
        // Мокаем поведение метода getTreeAsFile, чтобы он выбросил RuntimeException
        when(message.getChatId()).thenReturn(12345L);
        RuntimeException runtimeException = new RuntimeException("Сервис недоступен");
        when(categoryTreeService.getTreeAsFile(anyLong())).thenThrow(runtimeException);

        // Выполняем команду
        TelegramResponseSender response = downloadCommand.process(message);

        // Проверяем, что возвращается ErrorTelegramResponseSender
        assertNotNull(response);
        assertTrue(response instanceof ErrorTelegramResponseSender);

        // Проверяем, что в сообщении ошибки содержится информация о RuntimeException
        ErrorTelegramResponseSender errorResponseSender = (ErrorTelegramResponseSender) response;
        assertTrue(errorResponseSender.getErrorMessage().contains("Сервис недоступен"));
    }

    @Test
    public void testIsCompleted() {
        // Проверяем, что метод isCompleted возвращает true
        assertTrue(downloadCommand.isCompleted());
    }
}
