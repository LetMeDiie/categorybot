package kz.amihady.telegrambot.factory;

import com.sun.nio.sctp.IllegalReceiveException;
import jakarta.annotation.PostConstruct;
import kz.amihady.telegrambot.feign.CategoryServiceClient;
import kz.amihady.telegrambot.service.command.step.Step;
import kz.amihady.telegrambot.service.command.step.StepContext;
import kz.amihady.telegrambot.service.command.step.add.AddCommandFirstStep;
import kz.amihady.telegrambot.service.command.step.add.AddCommandSecondStep;
import kz.amihady.telegrambot.service.command.step.add.AddCommandThirdStep;
import kz.amihady.telegrambot.service.command.step.remove.RemoveFirstStep;
import kz.amihady.telegrambot.service.command.step.remove.RemoveSecondStep;
import kz.amihady.telegrambot.utils.CommandType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class CommandStepFactory {
    @Autowired
    private  CategoryServiceClient categoryServiceClient;

    @Autowired
    private  CategoryRequestFactory categoryRequestFactory;
    private Map<CommandType, Supplier<Step[]>> stepCreators;


    @PostConstruct
    private void initStepCreators() {
        stepCreators = new HashMap<>();
        stepCreators.put(CommandType.ADDELEMENT, this::createAddElementSteps);
        stepCreators.put(CommandType.REMOVEELEMENT, this::createRemoveElementSteps);
    }

    public Step[] createStepsForCommand(CommandType commandType) {
        return stepCreators.getOrDefault(commandType, () -> {
            throw new IllegalReceiveException("Ошибка в коде: неизвестная команда " + commandType);
        }).get();
    }

    private Step[] createAddElementSteps() {
        StepContext stepContext = new StepContext();
        return new Step[]{
                new AddCommandFirstStep(stepContext),
                new AddCommandSecondStep(stepContext, categoryRequestFactory),
                new AddCommandThirdStep(stepContext, categoryServiceClient)
        };
    }

    private Step[] createRemoveElementSteps() {
        StepContext stepContext = new StepContext();
        return new Step[]{
                new RemoveFirstStep(stepContext),
                new RemoveSecondStep(stepContext, categoryServiceClient)
        };
    }
}