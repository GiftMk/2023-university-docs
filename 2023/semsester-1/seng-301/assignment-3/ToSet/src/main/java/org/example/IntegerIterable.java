package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IntegerIterable implements Iterable<Integer> {
    private final List<Integer> integers;
    
    public IntegerIterable(List<Integer> integers) {
        this.integers = new ArrayList<>(integers);
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;
            public boolean hasNext() {
                return currentIndex < integers.size();
            }
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return integers.get(currentIndex++);
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
