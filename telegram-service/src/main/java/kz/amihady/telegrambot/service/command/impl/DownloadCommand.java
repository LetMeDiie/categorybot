package kz.amihady.telegrambot.service.command.impl;

import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryTreeExelResponse;
import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramFileResponseSender;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DownloadCommand implements Command {
    private CategoryTreeServiceClient categoryTreeService;

    public DownloadCommand(CategoryTreeServiceClient categoryTreeService) {
        this.categoryTreeService = categoryTreeService;
    }

    @Override
    public TelegramResponseSender process(Message message) {
        try {
            CategoryTreeExelResponse response =
                    categoryTreeService.getTreeAsFile(message.getChatId());
            return new TelegramFileResponseSender(response.file(), "category.xlsx");
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
