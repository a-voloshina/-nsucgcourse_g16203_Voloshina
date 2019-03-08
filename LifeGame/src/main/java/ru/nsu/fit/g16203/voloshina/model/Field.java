package ru.nsu.fit.g16203.voloshina.model;

import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;

import java.util.ArrayList;

public class Field<T> {

    ArrayList<ArrayList<T>> field = new ArrayList<>();
    int width;
    int height;
    private T initialValue;

    private int defaultFieldSize = 10;

    public int getWidth(int curHeight) {
        return checkWidth(width, curHeight);
    }

    public int getHeight() {
        return height;
    }

    Field(int width, int height, T initialValue) {
        setWidth(width);
        setHeight(height);
        for (int i = 0; i < height; i++) {
            ArrayList<T> inner = new ArrayList<>();
            int end = checkWidth(width, i);
            for (int j = 0; j < end; j++) {
                inner.add(initialValue);
            }
            field.add(inner);
        }

        this.initialValue = initialValue;
    }

    void setWidth(Integer width) {
        if (width == null || width < 1) {
            this.width = defaultFieldSize;
        } else {
            this.width = width;
        }
    }

    void setHeight(Integer height) {
        if (height == null || height < 1) {
            this.height = defaultFieldSize;
        } else {
            this.height = height;
        }
    }

    public T getItem(int coordinateX, int coordinateY) throws OutOfFieldRangeException {
        int width = checkWidth(this.width, coordinateY);
        if (coordinateX < width && coordinateX >= 0 && coordinateY < height && coordinateY >= 0) {
            return field.get(coordinateY).get(coordinateX);
        } else {
            throw new OutOfFieldRangeException(coordinateX, coordinateY, width, height);
        }
    }

    public void updateField(IMyFunc<T> fn) throws OutOfFieldRangeException {
        for (int i = 0; i < height; i++) {
            int end = checkWidth(width, i);
            for (int j = 0; j < end; j++) {
                field.get(i).set(j, fn.updateFieldItem(j, i));
            }
        }
    }

    public void setItem(int coordinateX, int coordinateY, T item) throws OutOfFieldRangeException {
        int width = checkWidth(this.width, coordinateY);
        if (coordinateX < width && coordinateX >= 0 && coordinateY < height && coordinateY >= 0) {
            field.get(coordinateY).set(coordinateX, item);
        } else {
            throw new OutOfFieldRangeException(coordinateX, coordinateY, width, height);
        }
    }

    public void _printField(IUpdate fn) {
        for (int i = 0; i < height; i++) {
            int curWidth = checkWidth(width, i);
            for (int j = 0; j < curWidth; j++) {
                fn.printFieldItem(j, i);
            }
        }
    }

    protected int checkWidth(int width, int height) {
        return (height % 2 == 0) ? width : width - 1;
    }

    public interface IMyFunc<T> {
        T updateFieldItem(int x, int y) throws OutOfFieldRangeException;
    }

    public void printField() {
        for (int i = 0; i < height; i++) {
            int curWidth = checkWidth(width, i);
            for (int j = 0; j < curWidth; j++) {
                System.out.print(field.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public interface IUpdate {
        void printFieldItem(int x, int y);
    }

    public void swap(Field<T> other) {
        ArrayList<ArrayList<T>> tmp = other.field;
        other.field = field;
        field = tmp;
    }

    public void resizeField(int newWidth, int newHeight) {
        if (newWidth > width) {
            increaseWidth(newWidth);
        } else if (newWidth < width) {
            decreaseWidth(newWidth, newHeight);
        }

        if (newHeight > height) {
            increaseHeight(newWidth, newHeight);
        } else if (newHeight < height) {
            decreaseHeight(newHeight);
        }

        setWidth(newWidth);
        setHeight(newHeight);
    }

    private void increaseWidth(int newWidth) {
        for (int i = 0; i < height; i++) {
            int start = checkWidth(width, i);
            int end = checkWidth(newWidth, i);
            for (int j = start; j < end; j++) {
                field.get(i).add(initialValue);
            }
        }
    }

    private void increaseHeight(int newWidth, int newHeight) {
        for (int i = height; i < newHeight; i++) {
            ArrayList<T> inner = new ArrayList<>();
            int end = checkWidth(newWidth, i);
            for (int j = 0; j < end; j++) {
                inner.add(initialValue);
            }
            field.add(inner);
        }
    }

    private void decreaseWidth(int newWidth, int newHeight) {
        for (int i = 0; i < newHeight; i++) {
            int start = checkWidth(width, i) - 1;
            int end = checkWidth(newWidth, i) - 1;
            if (start >= end + 1) {
                field.get(i).subList(end + 1, start + 1).clear();
            }
        }
    }

    private void decreaseHeight(int newHeight) {
        int end = newHeight - 1;
        if (height > end + 1) {
            field.subList(end + 1, height).clear();
        }
    }

    public void clearField() {
        for (int i = 0; i < height; i++) {
            int end = checkWidth(width, i);
            for (int j = 0; j < end; j++) {
                field.get(i).set(j, initialValue);
            }
        }
    }

}
