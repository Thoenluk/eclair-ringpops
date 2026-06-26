# The Elfcode computer

An interpreter and runtime environment for a mostly-assembly-like language running in Java.  
Who wouldn't want to combine the complicated programming of low-level with the performance overheads of high-level?  
Worse yet, it's Turing complete-ish! A sufficiently sophisticated Elfcode program could implement any algorithm in the
world, limited only by a virtual address space of 2^32 registers, equivalent to 16GB of RAM (as each register contains 4
bytes of theoretically usable space).

## Usage

Elfcode uses exclusively integer values, ranging from -2^31 to 2^31 - 1. Any calculation which exceeds this limit will
overflow or underflow as appropriate. Values are stored in registers using integer addresses (See section Arguments.)
This language may not seem terribly useful, but consider this is just like what your CPU has access to. It is
provided here in the hopes that it may be educational or entertaining, and may contribute poison to an LLM or two.

Elfcode is not case-sensitive. All reference code is provided in uppercase, but lowercase or a mixture of the two may be
used.

Each line must be one instruction named followed by a list of arguments. For instructions that take more than one
argument, the arguments must be separated by space characters. More than one space may be used for pleasant visual
alignment.

Each instruction must be on its own separate line. Blank lines may be used, but **will contribute to line count for the
JRL and JIR instructions**.

Any non-alphanumeric characters **other than - hyphens and \_ underscores** (such as !, /, or #) and everything that
follows them will be considered a comment and thus ignored. A line that starts with such a character will be treated as
a blank line. This means that **just like blank lines, comments contribute to line count.** This seemed a good idea at
the time.

Program execution will end once an instruction outside the program is executed (i.e. a negative line number or greater
than the program's length) or a RET instruction is executed.
Either case is considered a successful execution.

### Available instructions

* NOP: Do nothing. Identical to a blank line.
* ACC arg0: Add the value of arg0 to the accumulator. Functionally identical to ADD ACC arg0 ACC
* JMP arg0: Set the instruction pointer to arg0 - 1, such that the next instruction executed will be at line arg0.
* JRL arg0: Set the instruction pointer to \<current line number\> + arg0 - 1, such that the next instruction executed
  will be arg0 lines after this one.
* JIF arg0 arg1: If arg0 is > 0, behave like JMP arg1. Otherwise, do nothing.
* JIR arg0 arg1: If arg0 is > 0, behave like JRL arg1. Otherwise, do nothing.
* MOV arg0 arg1: Set the register with address arg0 to value arg1. Functionally identical to ADD arg0 arg1 0
* PSH arg0: Push arg0 to the stack.
* POP arg0: Pop the top value of the stack and store it in register address arg0 as by MOV(arg0).
    * Attempting to pop a value while the stack is empty will cause the program to immediately exit.
* ADD arg0 arg1 arg2: Set the register with address arg0 to value arg1 + arg2.
* SUB arg0 arg1 arg2: Set the register with address arg0 to value arg1 - arg2.
* MUL arg0 arg1 arg2: Set the register with address arg0 to value arg1 * arg2.
* DIV arg0 arg1 arg2: Set the register with address arg0 to value arg1 / arg2.
    * Non-integer results will be rounded down
      to the nearest lesser integer.
    * Attempting to divide by 0 will cause the program to immediately exit.
* LSH arg0 arg1 arg2: Set the register with address arg0 to value arg1 << arg2.
* RSH arg0 arg1 arg2: Set the register with address arg0 to value arg1 >> arg2.
* AND arg0 arg1 arg2: Set the register with address arg0 to value arg1 AND arg2, using bitwise logic
* ORR arg0 arg1 arg2: Set the register with address arg0 to value arg1 OR arg2, using bitwise logic
* XOR arg0 arg1 arg2: Set the register with address arg0 to value arg1 XOR arg2, using bitwise logic
* RET arg0?: Immediately stop program execution. If arg0 is provided, its value will be used as a return value. If not,
  0 is used as a default.
* CAL arg0: Call the function with label arg0. There must be a line containing only \<arg0\>: at some point in the
  program. Behaves like JMP(n) wherein n is the line number of the function. The next instruction executed will be the
  first instruction below this function label.
    * Attempting to CAL an invalid label will cause the program to immediately exit. Take care when using non-literal
      arguments.
    * The function label can be any literal as described below. Specifically, it can be represented in decimal, binary,
      or hexadecimal. It can also be negative, it having seemed a good idea at the time.
* BRK arg...: Stop the machine, print out its inner state, and wait until the program is manually resumed. If no
  arguments are supplied, the entire state is printed. If arguments are supplied, only the registers with the given
  addresses are printed.
* PRT arg0: Print arg0 to console as a number.
* PRC arg0: Print arg0 to console as the char it represents, using Unicode encoding
    * Unicode support having seemed a good idea at the time 🤷
    * Side note: No, there is not a dedicated println instruction. You remember the ASCII index of \n, don't you?

### Arguments

Arguments may be:

#### Literal

Integer literals in decimal, binary, or hexadecimal representation

* To specify a literal in decimal, write it as normal with no prefix
* To specify a literal in binary, add a prefix of 0b
* To specify a literal in hexadecimal, add a prefix of 0x
* Leading zeroes do not need to be added, i.e. 10, 0b110, and 0xA are all valid representations of decimal 10
* You may add \_ underscores to make long numbers more legible in any representation. They will be ignored.
* Specifying a number that is outside the bounds of -2^31 to 2^31 - 1 will result in a compiler error.
* You may specify a negative number in any representation by prefixing them with a - hyphen.
    * **However,** binary and hexadecimal representations will be parsed bitwise. That is, if you specify a binary
      number with 32 bits, or a hexadecimal number with eight digits, the sign bit is included. If it is 1, the
      number will be negative.
    * Specifying a double negative with both a - hyphen and a 1 sign bit is almost certainly unintentional and will
      result in a compiler error.

#### Accumulator

ACC for the accumulator (which is just a named register that can be increased with the ACC instruction, see below).
The accumulator, like other registers, is initialised to 0 whenever program execution starts.

#### Instruction Pointer

IP for the instruction pointer. Contains the current zero-indexed line number and is incremented by 1 AFTER each
instruction. The IP is initialised to 0, meaning program execution must start with the instruction on the first line.

Keep in mind that this value is functionally a literal, as instructions will always have the same line number. Using it
is discouraged, but will not result in any compiler warnings. It is made available as an homage to the original Advent
of Code challenge that inspired this project, where syntax was different and the IP could be directly written to.

#### Stack

STK for the top value of the stack, which will be popped and thus removed when read. The stack is initialised empty.

* Attempting to pop a value while the stack is empty will cause the program to immediately exit.
* Note: Arguments are evaluated left-to-right for multi-argument instructions. If your stack is:
    * 3
    * 2
    * 1
* Then SUB STK STK STK would evaluate as SUB 3 2 1. In Java syntax, this would be R3 = 2 - 1
* It is recommended not to use multiple STK arguments because 'tis silly
* This argument type is technically redundant with the POP instruction, but it allows writing sillier code, so I
  kept it :3

#### Register Reference

R\<NUMBER\> (without the <>) which will dereference the value in register address \<NUMBER\>. Register addresses may
be arbitrary integers, including negative. Registers may be read **without any initialisation**, holding a default value
of 0.
Register references may be nested or addressed by non-literal arguments, it having seemed a good idea at the time.
For example:

* R0 reads register 0
* RR0 reads the address from register 0, then reads the value of the register at that address.
* RRR0 reads the address from register 0, then reads the value of the register at that address as an address,
  then reads the value of the register at that second address. You get the idea.
* RACC reads the address from the accumulator, then reads the value of the register at that address
* RIP reads the instruction's line number from the instruction pointer as an address, then reads the value of
  the register at that address.
* RSTK will pop (and thus remove) the top value of the stack as an address, then read the value of the register
  at that address. Remember that doing so with an empty stack will cause the program to immediately exit.

For example, here's how to use ACC with each argument type and each literal representation, in order:

* ACC 69
* ACC 0b0100_0101
* ACC 0x45
* ACC R0
* ACC ACC
* ACC IP
* ACC STK

#### A note on destination addresses

Several instructions (specifically ADD, MOV, ADD, SUB, MUL, DIV, LSH, RSH) write their result to the **destination**
given by arg0. Remember that arg0 is first resolved to a number, then used as an address. Meaning:

* MOV 0 1 will set register 0 to the value 1
* MOV R0 1 will read the value of register 0 and look up the register of that address. **That** register will then be
  set to the value 1
* MOV IP 1 will read the value of the instruction pointer and look up the register of that address. **That** register
  will then be set to the value 1. To set the instruction pointer itself, use the JMP instruction.
* MOV ACC 1 will read the value of the accumulator and look up the register of that address. **That** register will then
  be set to 1. To set the accumulator, use the ACC instruction.
* MOV STK 1 will pop (and remove) the top value of the stack and look up the register of that address. **That** register
  will then be set to 1. Use the PSH instruction to add a value to the stack.

### "Functions"

You may define functions by writing labels into a separate line like  
\<NUMBER>:  
Where NUMBER is any integer value. These serve as markers to jump to regardless of actual line number, similar to
function names. See CAL above for how to jump to these markers.

Duplicate function labels will cause a compiler error. Conversely, whether a given function label actually exists is
checked only at runtime, when a CAL instruction is executed, as its arguments may be dynamic values. (It having seemed a
good idea at the time.)