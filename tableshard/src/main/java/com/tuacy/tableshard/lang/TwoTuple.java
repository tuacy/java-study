package com.tuacy.tableshard.lang;

public class TwoTuple<F,S> {

    private F first;
    private S second;

    public TwoTuple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
