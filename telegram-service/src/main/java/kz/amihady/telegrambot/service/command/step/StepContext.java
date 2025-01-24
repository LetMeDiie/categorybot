package kz.amihady.telegrambot.service.command.step;

import java.util.HashMap;
import java.util.Map;

public class StepContext {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }
}