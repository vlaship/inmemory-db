package inmemory.db;

class Result {
    private String result;

    Result() {
    }

    Result(String result) {
        this.result = result;
    }

    Result(long count) {
        this.result = String.valueOf(count);
    }

    String getResult() {
        return result;
    }
}
