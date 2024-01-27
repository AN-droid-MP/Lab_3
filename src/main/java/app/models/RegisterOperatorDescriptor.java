package app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOperatorDescriptor extends OperatorDescriptor {
    private int[] registers;

    @Override
    public String getInfo() {
        return super.getInfo() +
                ", Registers: " + Arrays.toString(registers);
    }

    public int getRegisterValue(int registerIndex) {
        if (registerIndex < 0 || registerIndex >= registers.length) {
            throw new IllegalArgumentException("Invalid register index");
        }
        return registers[registerIndex];
    }

    public void setRegisterValue(int registerIndex, int value) {
        if (registerIndex < 0 || registerIndex >= registers.length) {
            throw new IllegalArgumentException("Invalid register index");
        }
        registers[registerIndex] = value;
    }
}
