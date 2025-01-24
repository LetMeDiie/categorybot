package kz.amihady.telegrambot.service.command.step.stepresponse;

import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.utils.StepResult;

public record StepResponse(
        StepResult stepResult,
        TelegramResponseSender responseSender) {
}
