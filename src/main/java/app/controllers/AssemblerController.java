package app.controllers;

import app.models.AssemblerInstruction;
import app.models.ResponseModel;
import app.services.AssemblerProgram;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/assembler")
public class AssemblerController {

    private final AssemblerProgram assemblerProgram;

    @GetMapping("/instructions")
    public ResponseEntity<List<AssemblerInstruction>> getAllInstructions() {
        List<AssemblerInstruction> instructions = assemblerProgram.getAllInstructions();
        return ResponseEntity.ok(instructions);
    }

    @PostMapping("/instructions")
    public ResponseEntity<Void> addInstruction(@RequestBody AssemblerInstruction instruction) {
        assemblerProgram.addInstruction(instruction);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/instructions/edit/{originalAddress}")
    public ResponseEntity<Void> editInstruction(@PathVariable Integer originalAddress, @RequestBody AssemblerInstruction instruction) {
        assemblerProgram.editInstruction(originalAddress, instruction);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/instructions/{address}")
    public ResponseEntity<?> getInstruction(@PathVariable int address) {
        AssemblerInstruction instruction = assemblerProgram.findInstructionByAddress(address);
        return ResponseEntity.ok((instruction == null)? new ResponseModel("No instruction with such address " + address)
                : instruction);
    }

    @DeleteMapping("/instructions/{address}")
    public ResponseEntity<ResponseModel> deleteInstruction(@PathVariable int address) {
        String response = assemblerProgram.deleteInstructionByAddress(address);
        return ResponseEntity.ok(new ResponseModel(response));
    }
    @GetMapping("/operator-info/{address}")
    public ResponseEntity<ResponseModel> getOperatorInfo(@PathVariable int address) {
        String info = assemblerProgram.getOperatorInfo(address);
        return ResponseEntity.ok(new ResponseModel(info));
    }
    @GetMapping("/check-identifiers")
    public ResponseEntity<List<Integer>> checkIdentifiers() {
        List<Integer> invalidAddresses = assemblerProgram.checkIdentifiers();
        return ResponseEntity.ok(invalidAddresses);
    }
}