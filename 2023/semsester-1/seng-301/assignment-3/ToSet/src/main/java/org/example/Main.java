package org.example;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Collection<Integer> collection = List.of(1, 2, 3);
        Set<Integer> setOfInts = (Set<Integer>) collection;
        System.out.println(setOfInts);
    }
}