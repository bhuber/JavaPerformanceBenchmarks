package org.bhuber.java.collections.linkedList;

import lombok.Data;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

    @State(Scope.Benchmark)
    @Data
    public static class SizedPerfTestState {
        public SizedPerfTestState() {}

        /*
        public SizedPerfTestState(int listSize, Supplier<Deque<Integer>> listCreator) {
            this.listSize = listSize;
            this.listCreator = listCreator;
        }
         */

        @Param({"2", "8", "16", "64", "256", "1024", "4096", "16192", "65536", "1048576", "16777216", "268435456" })
        int listSize;
        Supplier<List<Integer>> listCreator;
        List<Integer> underTest;

        public void resetUnderTest() {
            underTest = listCreator.get();
        }
    }

    public static abstract class SizedPerfTest implements ListPerfTestBase {
        @Setup
        public void setup(SizedPerfTestState state) {
            state.listCreator = this::createList;
            state.resetUnderTest();
        }

        @Benchmark
        public void append(SizedPerfTestState state) {
            final var listSize = state.listSize;
            final var underTest = state.underTest;

            for (int i = 0; i < listSize; i++) {
                underTest.add(i);
            }
        }

        @Benchmark
        public void prepend(SizedPerfTestState state) {
            final var listSize = state.listSize;
            final var underTest = state.underTest;

            for (int i = 0; i < listSize; i++) {
                underTest.add(0, i);
            }
        }

    }

    @Fork(0)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class LinkedListSizeIndependentPerfTest extends SizeIndependentPerfTest implements LinkedListPerfTestBase {

    }

    @Fork(0)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class ArrayListSizeIndependentPerfTest extends SizeIndependentPerfTest implements ArrayListPerfTestBase {

    }

    @Fork(0)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class LinkedListSizedPerfTest extends SizedPerfTest implements LinkedListPerfTestBase {

    }

    @Fork(0)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @State(Scope.Benchmark)
    public static class ArrayListSizedPerfTest extends SizedPerfTest implements ArrayListPerfTestBase {

    }
}
