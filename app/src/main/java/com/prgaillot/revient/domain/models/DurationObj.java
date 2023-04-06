package com.prgaillot.revient.domain.models;

public class DurationObj {
    String name;
    long value ;

    public DurationObj(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public long getValue() {
        return value;
    }

}
