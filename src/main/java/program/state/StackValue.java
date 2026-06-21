package main.java.program.state;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

record StackValue(Deque<Integer> stack) implements Argument {
    StackValue() {
        this(new LinkedList<>());
    }

    @Override
    public int getAsInt() {
        return pop();
    }

    public int pop() {
        return stack.pop();
    }

    public void push(final int value) {
        stack.push(value);
    }

    public void clear() {
        stack.clear();
    }

    @Override
    public String prettyPrint() {
        return "STK";
    }

    @Override
    public String toString() {
        return stack.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}
