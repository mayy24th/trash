package christmas.util;

public class MyPair<A, B> {
    private A name;
    private B count;

    public MyPair(A name, B count) {
        this.name = name;
        this.count = count;
    }

    public A getName() {
        return name;
    }

    public B getCount() {
        return count;
    }
}