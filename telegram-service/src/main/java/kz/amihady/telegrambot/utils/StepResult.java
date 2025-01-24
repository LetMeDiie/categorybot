package kz.amihady.telegrambot.utils;


public enum StepResult {
    SUCCESS(1),     // Успешно -> следующий шаг
    RETRY(0),       // повторяем шаг
    BACK(-1),       // назад
    ENDED(0);//завершен (например когда степ закончился или когда ее отменили)


    private final int value;

    StepResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
