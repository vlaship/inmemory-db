package inmemory.db;

import java.util.Locale;

public class InputHandler {

    private final Store db = new Store();
    private final Transaction transaction = new Transaction();

    public void run(String line) {
        final var strings = line.split("\\s+");
        final var command = parseLine(strings);
        if (command != null) {
            execute(command);
        }
    }

    private Command parseLine(String[] strings) {
        Command command = null;
        switch (Commands.valueOf(strings[0].toUpperCase(Locale.ROOT))) {
            case SET -> {
                if (checkCommand(strings, 3)) {
                    command = new Command(Commands.SET, strings[1], strings[2]);
                }
            }
            case GET -> {
                if (checkCommand(strings, 2)) {
                    command = new Command(Commands.GET, strings[1], null);
                }
            }
            case DELETE -> {
                if (checkCommand(strings, 2)) {
                    command = new Command(Commands.DELETE, strings[1], null);
                }
            }
            case COUNT -> {
                if (checkCommand(strings, 2)) {
                    command = new Command(Commands.COUNT, null, strings[2]);
                }
            }
            case BEGIN -> command = new Command(Commands.BEGIN, null, null);
            case COMMIT -> command = new Command(Commands.COMMIT, null, null);
            case ROLLBACK -> command = new Command(Commands.ROLLBACK, null, null);
        }

        return command;
    }

    private boolean checkCommand(String[] strings, int count) {
        if (strings.length < count) {
            System.err.println("Wrong command format");
            return false;
        }
        return true;
    }

    private void execute(Command command) {
        Result result = null;
        switch (command.command()) {
            case SET, DELETE -> {
                if (transaction.isBegin()) {
                    transaction.add(prepareRollbackCommand(command));
                }
                db.execute(command);
            }
            case GET, COUNT -> result = db.execute(command);
            case BEGIN -> result = transaction.begin(command);
            case COMMIT -> result = transaction.commit();
            case ROLLBACK -> result = transaction.rollback();
            default -> throw new IllegalArgumentException();
        }
        if (result != null && result.getResult() != null) {
            System.out.println(result.getResult());
        }
    }

    private Command prepareRollbackCommand(Command command) {
        final var result = db.execute(new Command(Commands.GET, command.key(), null));
        if (result.getResult() == null) {
            return new Command(Commands.DELETE, command.key(), null);
        }
        return new Command(Commands.SET, command.key(), result.getResult());
    }
}
