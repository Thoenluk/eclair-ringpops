package main.java.program.state;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class State {
    private final Map<Integer, Register> registers = new HashMap<>();
    private final Register acc = new NamedRegister("ACC");
    private final Register ip = new NamedRegister("IP");
    private final StackValue stack = new StackValue();
    private final Map<Integer, Integer> functionLabels = new HashMap<>();
    private int returnValue = 0;

    private Register getRegister(final int address) {
        return registers.computeIfAbsent(address, NumberedRegister::new);
    }

    public int getRegisterValue(final int address) {
        return getRegister(address).getValue();
    }

    public void setRegisterValue(final int address, final int value) {
        getRegister(address).setValue(value);
    }

    public void setAccumulatorValue(final int value) {
        acc.setValue(value);
    }

    public void setNextLineToExecute(final int value) {
        ip.setValue(value - 1);
    }

    public void setNextLineToExecuteByOffset(final int offset) {
        setNextLineToExecute(ip.getAsInt() + offset);
    }

    public int pop() {
        return stack.pop();
    }

    public void push(final int value) {
        stack.push(value);
    }

    public void call(final int functionLabel) {
        ip.setValue(functionLabels.get(functionLabel) - 1);
    }

    public void returnWith(final int returnValue) {
        this.returnValue = returnValue;
        ip.setValue(Integer.MIN_VALUE);
    }

    public void breakPrintingEntireMachine() {
        print(toString());
        // break()?
    }

    public void breakPrinting(final int[] values) {
        final String output = Arrays.stream(values).mapToObj(Integer::toString).collect(Collectors.joining(" "));
        print(output);
        // break()?
    }

    public void print(final int output) {
        print(Integer.toString(output));
    }

    public void print(final String output) {
        System.out.print(output);
        // Definitely replace this with a more sensible in-GUI solution, but I can't use what I ain't built 3:
    }

    public void addFunctionLabel(final int label, final int lineNumber) {
        functionLabels.put(label, lineNumber);
    }

    public int getIpValue() {
        return ip.getValue();
    }

    public void advance() {
        ip.setValue(getIpValue() + 1);
    }

    public int getReturnValue() {
        return returnValue;
    }

    public void reset() {
        acc.clear();
        ip.clear();
        stack.clear();
        for (final Register register : registers.values()) {
            register.clear();
        }
    }

    // Readonly view by type safety with zero overhead. How's that?
    Argument getRegisterArgument(final int address) {
        return getRegister(address);
    }

    Argument getAcc() {
        return acc;
    }

    Argument getIp() {
        return ip;
    }

    Argument getStack() {
        return stack;
    }

    @Override
    public String toString() {
        final String accumulator = "ACC: " + acc.getValue();
        final String instructionPointer = "IP: " + ip.getValue();
        final String returnVal = "Return value: " + returnValue;
        final String fns = "Function labels:\n" + functionLabels.entrySet().stream()
                .map(e -> e.getKey() + " at line " + e.getValue())
                .collect(Collectors.joining("\n"));
        final String regs = "Registers:\n" + registers.entrySet().stream()
                .map(e -> e.getKey() + " -> " + e.getValue())
                .collect(Collectors.joining("\n"));
        final String stk = "Stack:\n" + stack;
        return String.join("\n", accumulator, instructionPointer, returnVal, fns, regs, stk);
    }
}
