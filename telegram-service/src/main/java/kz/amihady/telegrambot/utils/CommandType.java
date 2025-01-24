package kz.amihady.telegrambot.utils;


public enum CommandType {
    ADDELEMENT("/addelement", "Добавить новую категорию."),
    REMOVEELEMENT("/removeelement", "Удалить категорию."),
    HELP("/help","Инструкцию."),
    VIEWTREE("/viewtree","Посмотреть категорию"),
    DOWNLOAD("/download","Скачать категорию"),
    ROOT("/root","Вспомогательная команда для команды /addelement"),
    CANCEL("/cancel","Отменить команду");

    private final String commandName;
    private final String description;

    CommandType(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    public static CommandType getCommand(String input){
        for(CommandType commandType : CommandType.values()) {
            if (input.equalsIgnoreCase(commandType.getCommandName()))
                return commandType;
        }
        return null;
    }
}