document.addEventListener("DOMContentLoaded", function () {
    window.onload = getProgram
    document.getElementById('operatorType').addEventListener('change', showFields);
    document.getElementById('addInstruction').addEventListener('click', addInstruction);
    document.getElementById('checkIdentifiersButton').addEventListener('click', checkIdentifiers);

    function showFields() {
        const selectedType = this.value;
        const extraFieldsContainer = document.getElementById('extraFields');
        extraFieldsContainer.innerHTML = '';

        switch (selectedType) {
            case 'register':
                extraFieldsContainer.innerHTML += '<input type="text" id="registers" placeholder="Registers (comma-separated)" required/><br/>';
                break;
            case 'memory':
                extraFieldsContainer.innerHTML += '<input type="number" id="registerNumber" placeholder="Register Number" required/><br/>';
                extraFieldsContainer.innerHTML += '<input type="text" id="memoryIdentifier" placeholder="Memory Identifier" required/><br/>';
                break;
            case 'jump':
                extraFieldsContainer.innerHTML += '<input type="text" id="jumpLabel" placeholder="Jump Label" required/><br/>';
                break;
            case 'data':
                extraFieldsContainer.innerHTML += '<input type="text" id="dataIdentifier" placeholder="Data Identifier" required/><br/>';
                extraFieldsContainer.innerHTML += '<input type="number" id="operand" placeholder="Operand" required/><br/>';
                break;
        }
    }


    async function addInstruction() {
        const operatorType = document.getElementById('operatorType').value;
        const address = document.getElementById('address').value;
        const label = document.getElementById('label').value;
        const operationCode = document.getElementById('operationCode').value;

        const descriptor = {
            label: label,
            operationCode: operationCode
        };

        switch (operatorType) {
            case 'register':
                descriptor.type = 'register'
                descriptor.registers = document.getElementById('registers').value.split(',').map(Number);
                break;
            case 'memory':
                descriptor.type = 'memory'
                descriptor.registerNumber = parseInt(document.getElementById('registerNumber').value, 10);
                descriptor.memoryIdentifier = document.getElementById('memoryIdentifier').value;
                break;
            case 'jump':
                descriptor.type = 'jump'
                descriptor.jumpLabel = document.getElementById('jumpLabel').value;
                break;
            case 'data':
                descriptor.type = 'data'
                descriptor.dataIdentifier = document.getElementById('dataIdentifier').value;
                descriptor.operand = document.getElementById('operand').value;
                break;
        }

        try {
            await axios.post('http://localhost:8080/api/assembler/instructions', {
                address: address,
                descriptor: descriptor
            });
            await getProgram()
        } catch (error) {
            console.error('Error adding instruction:', error);
            alert('Failed to add instruction');
        }
    }

    async function deleteInstruction(address) {

        try {
            await axios.delete(`http://localhost:8080/api/assembler/instructions/${address}`);
            await getProgram()
        } catch (error) {
            console.error('Error deleting instruction:', error);
            alert('Failed to delete instruction');
        }
    }

    async function getInstruction(address) {

        try {
            const response = await axios.get(`http://localhost:8080/api/assembler/instructions/${address}`);
            document.getElementById('response').innerText = 'Instruction: ' + JSON.stringify(response.data, null, 2);
        } catch (error) {
            console.error('Error getting instruction:', error);
            alert('Failed to get instruction');
        }
    }

    async function getProgram() {
        try {
            const response = await axios.get('http://localhost:8080/api/assembler/instructions');
            const instructions = response.data;

            document.getElementById("programList").innerHTML = "";

            for(let i = 0; i < instructions.length; i++) {
                document.getElementById("programList").appendChild(createBlock(instructions[i]));
            }

        } catch (error) {
            console.error('Error getting instructions:', error);
            alert('Failed to get instructions');
        }
    }

    function createBlock(instruction) {
        const wrapper = document.createElement("tr");
        const address = document.createElement("th");
        address.innerText = instruction.address
        address.onclick = function (){getInstruction(instruction.address)}
        const label = document.createElement("th");
        label.innerText = instruction.descriptor.label
        const operationCode = document.createElement("th");
        operationCode.innerText = instruction.descriptor.operationCode

        const otherInfo = document.createElement('th');
        otherInfo.appendChild(getAdditionalInfo(instruction.descriptor));

        const operations = document.createElement("th");
        const delBtn = document.createElement("button");
        delBtn.innerText = "Delete"
        delBtn.onclick = function () {
            deleteInstruction(instruction.address)
        }
        const infoBtn = document.createElement("button");
        infoBtn.innerText = "Info"
        infoBtn.onclick = function () {
            getOperatorInfo(instruction.address)
        }

        operations.appendChild(infoBtn)
        operations.appendChild(delBtn)

        wrapper.appendChild(address)
        wrapper.appendChild(label)
        wrapper.appendChild(operationCode)
        wrapper.appendChild(otherInfo)
        wrapper.appendChild(operations)

        return wrapper;
    }

    function getAdditionalInfo(instruction) {
        const excludedFields = ["address", "label", "operationCode", "type", "info"];

        const div = document.createElement("p");

        for (let key in instruction) {
            if (!excludedFields.includes(key)) {
                const keyValue = `${key}: ${instruction[key]}`;
                const lineBreak = document.createElement("br");

                div.appendChild(document.createTextNode(keyValue));
                div.appendChild(lineBreak);
            }
        }

        return div;
    }

    async function getOperatorInfo(address) {

        const response = await axios.get(`http://localhost:8080/api/assembler/operator-info/${address}`);
        const info = response.data;
        document.getElementById('response').innerText = info.message
    }

    async function checkIdentifiers() {
        try {
            const response = await axios.get('http://localhost:8080/api/assembler/check-identifiers');
            const invalidAddresses = response.data;

            document.getElementById('response').innerText = "Invalid addresses: " + invalidAddresses;
        } catch (error) {
            console.error('Error checking identifiers:', error);
            alert('Failed to check identifiers');
        }
    }
})