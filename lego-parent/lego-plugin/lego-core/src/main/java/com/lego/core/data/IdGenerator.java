package com.lego.core.data;

public abstract class IdGenerator {
    private static IdGenerator current;

    public abstract Long nextId();
    public abstract Long nextId(String type);

    public static IdGenerator getCurrent() {
        return current;
    }

    public static void setIdGenerator(IdGenerator current) {
        IdGenerator.current = current;
    }
}
