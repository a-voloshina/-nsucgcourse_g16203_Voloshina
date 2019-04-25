package ru.nsu.fit.g16203.voloshina.general;

public class Triple<T> {
    private T first;
    private T second;
    private T third;

    private Triple() {
    }

    public Triple(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode() + third.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Triple another;
        try {
            another = (Triple) obj;
        } catch (ClassCastException ex) {
            return false;
        }
        return another.first == this.first && another.second == this.second && another.third == this.third;
    }

    @Override
    public String toString() {
        String string;
        string = first.toString() + "," + second.toString() + "," + third.toString();
        return string;
    }
}
