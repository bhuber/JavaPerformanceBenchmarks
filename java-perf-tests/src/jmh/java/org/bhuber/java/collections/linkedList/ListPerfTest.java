package org.bhuber.java.collections.linkedList;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListPerfTest {

    interface ListPerfTestBase {
        default <T> List<T> createList() {
            return createList(null);
        }
        <T> List<T> createList(Integer capacity);
    }

    public interface LinkedListPerfTestBase extends ListPerfTestBase {

        default <T> List<T> createList(Integer capacity) {
            return new LinkedList<T>();
        };
    }

    public interface ArrayListPerfTestBase extends ListPerfTestBase {

        default <T> List<T> createList(Integer capacity) {
            return capacity == null ? new ArrayList<>() : new ArrayList<>(capacity);
        };
    }

    public static abstract class SizeIndependentPerfTest implements ListPerfTestBase {



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
        public void zeroElementCreationTime(Blackhole blackhole) {
            final List<Integer> underTest = createList();
            blackhole.consume(underTest);
        }

        @Benchmark
        public void singleElementCreationTime(Blackhole blackhole) {
            final List<Integer> underTest = createList();
            underTest.add(1);
            blackhole.consume(underTest);
        }
    }

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class LinkedListSizeIndependentPerfTest extends SizeIndependentPerfTest implements LinkedListPerfTestBase {

    }

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class ArrayListSizeIndependentPerfTest extends SizeIndependentPerfTest implements ArrayListPerfTestBase {

    }
}
