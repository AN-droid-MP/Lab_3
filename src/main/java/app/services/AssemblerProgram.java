package app.services;

import app.models.AssemblerInstruction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AssemblerProgram {
    private final List<AssemblerInstruction> instructions = new ArrayList<>();

    public void addInstruction(AssemblerInstruction instruction) {
        int address;

        if (instruction.getAddress() % 8 == 0) {
            address = instruction.getAddress();
        }
        else {
            address = instruction.getAddress() - instruction.getAddress()%8;

            if(instruction.getAddress()%8 > 4) {
                address+=8;
            }
        }

        if (address / 8 > instructions.size()) {
            instructions.add(instruction);
        } else {
            instructions.add(address / 8, instruction);
        }

        for (int i = 0; i < instructions.size(); i++) {
            AssemblerInstruction currentInstruction = instructions.get(i);
            int expectedAddress = i * 8;

            if (currentInstruction.getAddress() != expectedAddress) {
                if (currentInstruction.getAddress() % 8 == 0 && currentInstruction.getAddress() < i * 8
                        && currentInstruction.getAddress() / 8 <= instructions.size()) {
                    currentInstruction.setAddress(expectedAddress);
                }
            }
        }
        instructions.sort(Comparator.comparingInt(AssemblerInstruction::getAddress));
    }

    public void editInstruction(int originalAddress, AssemblerInstruction instruction) {
        deleteInstructionByAddress(originalAddress);

        addInstruction(instruction);
    }

    public String deleteInstructionByAddress(int address) {
        if(findInstructionByAddress(address) != null) {
            instructions.removeIf(instr -> instr.getAddress() == address);

            for (int i = 0; i < instructions.size(); i++) {
                AssemblerInstruction currentInstruction = instructions.get(i);
                int expectedAddress = i * 8;

                if (currentInstruction.getAddress() != expectedAddress) {
                    if (currentInstruction.getAddress() % 8 == 0 && currentInstruction.getAddress() > i * 8
                            && currentInstruction.getAddress() / 8 <= instructions.size()) {
                        currentInstruction.setAddress(expectedAddress);
                    }
                }
            }

            return "Instruction was deleted successfully on address " + address;
        }
        else return "Non existent instruction on address " + address;
    }

    public AssemblerInstruction findInstructionByAddress(int address) {
        return instructions.stream()
                .filter(instr -> instr.getAddress() == address)
                .findFirst()
                .orElse(null);
    }

    public List<AssemblerInstruction> getAllInstructions() {
        return new ArrayList<>(instructions);
    }

    public String getOperatorInfo(int address) {
        AssemblerInstruction instruction = findInstructionByAddress(address);
        if (instruction != null) {
            return instruction.getDescriptor().getInfo();
        }
        return "Instruction not found at address: " + address;
    }


    public List<Integer> checkIdentifiers() {
        return instructions.stream()
                .filter(assemblerInstruction -> assemblerInstruction.getAddress() % 8 != 0 ||
                        assemblerInstruction.getAddress()/8 > instructions.size()).map(AssemblerInstruction::getAddress)
                .toList();
    }
}
