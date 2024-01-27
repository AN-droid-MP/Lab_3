package app.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegisterOperatorDescriptor.class, name = "register"),
        @JsonSubTypes.Type(value = MemoryOperatorDescriptor.class, name = "memory"),
        @JsonSubTypes.Type(value = JumpOperatorDescriptor.class, name = "jump"),
        @JsonSubTypes.Type(value = DataDeclarationOperatorDescriptor.class, name = "data")
})
public class OperatorDescriptor {
    private String label;
    private String operationCode;

    public String getInfo() {
        return "Type: " + this.getClass().getSimpleName() +
                ", Label: " + label +
                ", Operation Code: " + operationCode;
    }
}
