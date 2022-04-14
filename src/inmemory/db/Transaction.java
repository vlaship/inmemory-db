package inmemory.db;

import java.util.Deque;
import java.util.LinkedList;

class Transaction {
    private final Deque<Command> transactions = new LinkedList<>();
    private boolean isBegin = false;

    boolean isBegin() {
        return isBegin;
    }

    Result begin(Command command) {
        isBegin = true;
        transactions.push(command);
        return new Result();
    }

    Result commit() {
        isBegin = false;
        transactions.clear();
        return new Result();
    }

    Result rollback() {
        if (!isBegin) {
            transactions.clear();
            return new Result("NO TRANSACTION");
        }
        if (transactions.isEmpty()) {
            isBegin = false;
            return new Result("NO TRANSACTION");
        }

        while (transactions.pop().command() != Commands.BEGIN) {
            // TODO ...
        }

        return new Result();
    }

    void add(Command command) {
        transactions.push(command);
    }
}
