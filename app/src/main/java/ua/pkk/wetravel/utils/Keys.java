package ua.pkk.wetravel.utils;

public enum Keys {
    VIDEO_FROM_MAP(1),VIDEO_FROM_ADAPTER(2);

    private final int value;

    Keys(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
