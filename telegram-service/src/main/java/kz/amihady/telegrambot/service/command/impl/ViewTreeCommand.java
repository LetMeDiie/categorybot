package kz.amihady.telegrambot.service.command.impl;

import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryTreeStringResponse;
import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ViewTreeCommand implements Command {
    private CategoryTreeServiceClient categoryTreeService;

    public ViewTreeCommand(CategoryTreeServiceClient categoryTreeService) {
        this.categoryTreeService = categoryTreeService;
    }

    @Override
    public TelegramResponseSender process(Message message) {
        try {
            CategoryTreeStringResponse response =
                    categoryTreeService.getTreeAsString(message.getChatId());
            return new TelegramTextResponseSender(response.categoryTree());
        }
        catch (FeignClientException exception){
            return new ErrorTelegramResponseSender(exception.getMessage());
        }
        catch (RuntimeException exception){
            return new ErrorTelegramResponseSender(exception.getMessage());
        }

    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
