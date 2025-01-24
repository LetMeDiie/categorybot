package kz.amihady.telegrambot.command;


import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryTreeStringResponse;
import kz.amihady.telegrambot.service.command.impl.ViewTreeCommand;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewTreeCommandTest {

    @Mock
    private CategoryTreeServiceClient categoryTreeService;

    private ViewTreeCommand viewTreeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        viewTreeCommand = new ViewTreeCommand(categoryTreeService);
    }

    @Test
    void testProcess_Success() {
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(123L);

        CategoryTreeStringResponse response = new CategoryTreeStringResponse("Category Tree Data");
        when(categoryTreeService.getTreeAsString(123L)).thenReturn(response);

        TelegramResponseSender result = viewTreeCommand.process(message);

        assertInstanceOf(TelegramTextResponseSender.class, result);
        assertEquals("Category Tree Data", ((TelegramTextResponseSender) result).getMessage());
    }



    @Test
    void testProcess_FailsWithRuntimeException() {
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(123L);
        when(categoryTreeService.getTreeAsString(123L)).thenThrow(new RuntimeException("Unexpected error"));

        TelegramResponseSender result = viewTreeCommand.process(message);

        assertInstanceOf(ErrorTelegramResponseSender.class, result);
        assertEquals("Unexpected error", ((ErrorTelegramResponseSender) result).getErrorMessage());
    }

    @Test
    void testIsCompleted() {
        assertTrue(viewTreeCommand.isCompleted());
    }
}
