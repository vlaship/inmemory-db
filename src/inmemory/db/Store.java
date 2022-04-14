package inmemory.db;

import java.util.HashMap;
import java.util.Map;

class Store {

    private final Map<String, String> db = new HashMap<>();

    Map<String, Integer> valueAndCount;

    Result execute(Command command) {
        return switch (command.command()) {
            case GET -> get(command);
            case SET -> set(command);
            case DELETE -> delete(command);
            case COUNT -> count(command);
            default -> throw new IllegalArgumentException();
        };
    }

    private Result set(Command command) {
        db.put(command.key(), command.value());
        final var count = valueAndCount.getOrDefault(command.value(), 0);
        valueAndCount.put(command.value(), count + 1);
        return new Result();
    }

    private Result get(Command command) {
        return new Result(db.getOrDefault(command.key(), "NULL"));
    }

    private Result delete(Command command) {
        db.remove(command.key());

        return new Result();
    }

    private Result count(Command command) {
        return new Result(valueAndCount.getOrDefault(command.value(), 0));
    }
}


