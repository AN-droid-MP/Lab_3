package app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoryOperatorDescriptor extends OperatorDescriptor {
    private int registerNumber;
    private String memoryIdentifier;
}
