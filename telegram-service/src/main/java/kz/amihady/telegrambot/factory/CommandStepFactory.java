package kz.amihady.telegrambot.factory;

import com.sun.nio.sctp.IllegalReceiveException;
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
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class CommandStepFactory {
    CategoryServiceClient categoryServiceClient;
    CategoryRequestFactory categoryRequestFactory;

    public  Step [] createStepsForCommand(CommandType commandType){
        //будем создавать для команды лишь первые степы
        if(commandType == CommandType.ADDELEMENT) {
            return  addCommandStepFactor();
        }
        if(commandType == CommandType.REMOVEELEMENT){
            return removeCommandStepFactor();
        }
        throw new IllegalReceiveException("Ошибка в в коде");
    }

    private Step[] addCommandStepFactor(){
        Step [] steps = new Step[3];
        StepContext stepContext = new StepContext();
        steps[0]= new AddCommandFirstStep(stepContext);
        steps[1]= new AddCommandSecondStep(stepContext,categoryRequestFactory);
        steps[2]= new AddCommandThirdStep(stepContext,categoryServiceClient);
        return steps;
    }

    private Step [] removeCommandStepFactor(){
        Step [] steps = new Step[2];
        StepContext stepContext = new StepContext();
        steps[0] = new RemoveFirstStep(stepContext);
        steps[1] = new RemoveSecondStep(stepContext,categoryServiceClient);
        return steps;
    }
}
