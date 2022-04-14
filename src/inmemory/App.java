package inmemory;

import inmemory.db.InputHandler;

public class App {
    public static void main(String[] args) {
        final var inputHandler = new InputHandler();

        inputHandler.run("get a");
        inputHandler.run("set a");
        inputHandler.run("set a 10");
        inputHandler.run("get a");
    }
}
