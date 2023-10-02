package org.bhuber.java.collections.linkedList;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.LinkedList;
import java.util.List;

public class LinkedListPerfTest {

    @FunctionalInterface
    interface ListCreator {
        default <T> List<T> apply() {
            return apply(null);
        }
        <T> List<T> apply(Integer capacity);
    }

    @State(Scope.Benchmark)
    public static class CollectionPerfTest {

        final int size = 32;

        final ListCreator listCreator = new ListCreator() {
            @Override
            public <T> List<T> apply(Integer capacity) {
                return new LinkedList<T>();
            }
        };
        
        

        /*
         * Things to test
         * Front insertion
         * Middle insertion
         * Rear insertion
         * Forward search
         * Backward search
         * Filter out half the elements
         *
         * Zero element creation time
         * Single element creation time
         *
         */

        @Benchmark
        public void zeroElementCreationTime() {
            final List<Integer>  underTest = listCreator.apply();
        }

        @Benchmark
        public void singleElementCreationTime() {
            final List<Integer> underTest = listCreator.apply();
            underTest.add(1);
        }
    }
}
