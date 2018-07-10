package com.brainsocket.mainlibrary.Enums;

public enum SwipeDirection {
    all(0), left(1), right(2), none(3);
    private int value;

    SwipeDirection(int id) {
        this.value = id;
    }

    public int getValue() {
        return value;
    }

    public SwipeDirection setValue(int id) {
        this.value = id;
        switch (id) {
            case 0:
                return SwipeDirection.all;
            case 1:
                return SwipeDirection.left;
            case 2:
                return SwipeDirection.right;
            case 3:
                return SwipeDirection.none;
        }
        return SwipeDirection.all;
    }
}