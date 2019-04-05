package ru.nsu.fit.g16203.voloshina.general;

public class Pair<K, V> {
    private K key;
    private V value;

    private Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String string;
        string = key.toString() + "=" + value.toString();
        return string;
    }

    public int hashCode() {
        return (key.hashCode() + value.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        try {
            Pair another = (Pair) obj;
        } catch (ClassCastException ex) {
            return false;
        }
        return ((Pair) obj).key == this.key && ((Pair) obj).getValue() == this.getKey();
    }
}
