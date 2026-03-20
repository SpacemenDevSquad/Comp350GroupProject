package edu.gcc.prij.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TupleTest {
    @Test
    public void testTupleStoresCorrectValues() {
        Tuple<Integer, Integer> t = new Tuple<>(1, 2);
        assertEquals(1, t.x);
        assertEquals(2, t.y);
    }
}
