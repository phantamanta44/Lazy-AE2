package io.github.phantamanta44.threng.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class SlotType {

    public enum BasicIO {

        INPUT(true, false), OUTPUT(false, true), OMNI(true, true), NONE(false, false);

        public static final List<BasicIO> VALUES = ImmutableList.copyOf(values());

        public static BasicIO get(int index) {
            return (index >= 0 && index < VALUES.size()) ? VALUES.get(index) : NONE;
        }

        public boolean allowsInput, allowsOutput;

        BasicIO(boolean allowsInput, boolean allowsOutput) {
            this.allowsInput = allowsInput;
            this.allowsOutput = allowsOutput;
        }

        public BasicIO next() {
            return VALUES.get((ordinal() + 1) % VALUES.size());
        }

        public BasicIO prev() {
            int valueCount = VALUES.size();
            return VALUES.get((ordinal() + valueCount - 1) % valueCount);
        }

    }

}
