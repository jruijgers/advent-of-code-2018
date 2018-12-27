package org.salandur.advent_of_code.day16;

import java.util.ArrayList;
import java.util.List;

public abstract class OpCode {
    public static List<OpCode> OP_CODES = List.of(
            new AdrR(), new AddI(), new MulR(), new MulI(),
            new BanR(), new BanI(), new BorR(), new BorI(),
            new SetR(), new SetI(),
            new GtIR(), new GtRI(), new GtRR(),
            new EqIR(), new EqRI(), new EqRR()
    );

    public void perform(Registers registers, int inputA, int inputB, int output) {
        registers.set(output, performInternal(registers, inputA, inputB));
    }

    protected abstract int performInternal(Registers registers, int inputA, int inputB);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    private static class AdrR extends OpCode {
        @Override
        public int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) + registers.get(inputB);
        }
    }

    private static class AddI extends OpCode {
        @Override
        public int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) + inputB;
        }
    }

    private static class MulR extends OpCode {
        @Override
        public int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) * registers.get(inputB);
        }
    }

    private static class MulI extends OpCode {
        @Override
        public int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) * inputB;
        }
    }

    private static class BanR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) & registers.get(inputB);
        }
    }

    private static class BanI extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) & inputB;
        }
    }

    private static class BorR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) | registers.get(inputB);
        }
    }

    private static class BorI extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) | inputB;
        }
    }

    private static class SetR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA);
        }
    }

    private static class SetI extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return inputA;
        }
    }

    private static class GtIR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return inputA > registers.get(inputB) ? 1 : 0;
        }
    }

    private static class GtRI extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) > inputB ? 1 : 0;
        }
    }

    private static class GtRR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) > registers.get(inputB) ? 1 : 0;
        }
    }

    private static class EqIR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return inputA == registers.get(inputB) ? 1 : 0;
        }
    }

    private static class EqRI extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) == inputB ? 1 : 0;
        }
    }

    private static class EqRR extends OpCode {
        @Override
        protected int performInternal(Registers registers, int inputA, int inputB) {
            return registers.get(inputA) == registers.get(inputB) ? 1 : 0;
        }
    }
}
