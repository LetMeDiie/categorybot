package kz.amihady.telegrambot.service.command.step.remove;

import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.CategoryServiceClient;
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
import org.telegram.telegrambots.meta.api.objects.Message;

public class RemoveSecondStep extends Step {
    private CategoryServiceClient categoryServiceClient;

    public RemoveSecondStep(StepContext stepContext, CategoryServiceClient categoryServiceClient) {
        super(stepContext);
        this.categoryServiceClient = categoryServiceClient;
    }

    @Override
    public StepResponse process(Message message) {
        if(!message.hasText()) { //если не строка вернем сообщение об ошибке и проис повторить ввод
            String errorResponse = "Имя категорий которую нужно удалить должна быть в виде строки.";
            errorResponse+="\nПовторите ввести имя категорию еще раз"+ Constants.CANCEL_TEXT;
            return new StepResponse(
                    StepResult.RETRY,
                    new ErrorTelegramResponseSender(errorResponse));
        }
        String categoryName = message.getText(); //получаем имя категорий

        if (CommandType.CANCEL == CommandType.getCommand(categoryName)) {
            String cancelMessage = "Команда /removeelement была успешно отменена. Данные не удалились";
            return cancel(cancelMessage);
        }

        return removeCategory(message.getChatId(),categoryName);
    }

    private StepResponse removeCategory(Long userId,String categoryName){
        StepResult stepResult ;
        TelegramResponseSender responseSender;
        try{
            CategoryResponse categoryResponse =
                    categoryServiceClient.deleteCategory(userId,categoryName);
            responseSender = new TelegramTextResponseSender(
                    categoryResponse.message());
            stepResult=StepResult.ENDED;
        }
        catch (FeignClientException exception){ //это может вызывать тогда когда не может найти категорию с таким названием можем повторить попытку
            responseSender = new ErrorTelegramResponseSender(
                    exception.getMessage()+Constants.CANCEL_TEXT);
            stepResult=StepResult.RETRY;
        }
        catch (RuntimeException exception){
            responseSender =  new ErrorTelegramResponseSender(exception.getMessage());
            stepResult=StepResult.ENDED; // в этом случае возникает ошибна на сторное сервсиа (сервис не доступен) отменяем команду
        }
        return new StepResponse(stepResult,responseSender);
    }

}
