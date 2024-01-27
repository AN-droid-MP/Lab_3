package app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataDeclarationOperatorDescriptor extends OperatorDescriptor {
    private String dataIdentifier;
    private Integer operand;
}

