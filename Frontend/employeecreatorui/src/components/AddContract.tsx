import React, { useContext, useState } from 'react';
import { ContractCreateDTO, ContractViewDTO } from '../types/Contract';
import { EmployeeContext } from '../context/EmployeeContext';
import { contractSchemaWithPrevFinishDate } from './validators/ContractSchema';
import {
  Box,
  FormControl,
  FormLabel,
  Input,
  Select,
  NumberInput,
  NumberInputField,
  Button,
  HStack,
  VStack,
  useToast,
  Text,
} from '@chakra-ui/react';
import { ZodIssue } from 'zod';

type AddContractProps = {
  empid: number;
  previousContract?: ContractCreateDTO | null;   
  onContractAdded?: (addedContract: ContractViewDTO) => Promise<void>;   
}

const initialFormState: ContractCreateDTO = {
  contractType: 'Temporary',
  startDate: new Date().toISOString().split('T')[0],
  finishDate: null,
  workType: 'FullTime',
  hoursPerWeek: 40,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString(),
  ongoing: true,
};


/* Send back changes to updateEmployee (in order to reset Edit contract)*/
/* Check on add - Edit recent contract Tab displays new contract*/
/* Seperate issue: Fetch ongoing field to hightlight active contract in employee details view page (Refer GetContracts api)*/


const AddContract: React.FC<AddContractProps> = ({ empid, previousContract, onContractAdded }) => {
  const [formData, setFormData] = useState<ContractCreateDTO>(initialFormState);
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});

  const toast = useToast();
  const context = useContext(EmployeeContext);

  if (!context) throw new Error("AddContract must be used within an EmployeeProvider");
  const { createContract, refreshEmployees } = context;

  const handleChange = (field: keyof ContractCreateDTO, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSave = async (empid: number, formData: ContractCreateDTO) => {
    const schema = contractSchemaWithPrevFinishDate(previousContract);
    const result = schema.safeParse(formData);

    if (!result.success) {
      const errors: Record<string, string> = {};
      result.error.issues.forEach((issue: ZodIssue) => {
        const path = issue.path.join(".");
        errors[path] = issue.message;
      });
      setValidationErrors(errors);
      return;
    }
    setValidationErrors({});

    try {
      const addedContract = await createContract(empid, formData);
      await refreshEmployees();      
      toast({
        title: "Saved!",
        description: "Contract details saved successfully.",
        status: "success",
        duration: 3000,
        isClosable: true,
      });

      setFormData(initialFormState);
      if (onContractAdded && addedContract) await onContractAdded(addedContract);
    } catch (err) {
      console.error("Error creating contract", err);
      toast({
        title: "Error",
        description: "Failed to save contract.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const handleReset = () => {
    setFormData(initialFormState);
    toast({
      title: 'Changes Discarded',
      description: 'Form reset to initial values.',
      status: 'info',
      duration: 3000,
      isClosable: true,
    });
  };

  return (
    <Box p={4} border="1px solid" borderColor="gray.200" borderRadius="md" bgColor="green.50">
      <VStack spacing={4} align="stretch">
        <FormControl>
          <FormLabel>Contract Type</FormLabel>
          <Select
            value={formData.contractType}
            onChange={(e) => handleChange('contractType', e.target.value)}
          >
            <option value="Temporary">Temporary</option>
            <option value="Permanent">Permanent</option>
          </Select>
        </FormControl>

        <FormControl>
          <FormLabel>Work Type</FormLabel>
          <Select
            value={formData.workType}
            onChange={(e) => handleChange('workType', e.target.value)}
          >
            <option value="FullTime">Full Time</option>
            <option value="PartTime">Part Time</option>
          </Select>
        </FormControl>

        <FormControl>
          <FormLabel>Start Date</FormLabel>
          <Input
            type="date"
            value={formData.startDate}
            onChange={(e) => handleChange('startDate', e.target.value)}
          />
          {validationErrors["startDate"] && (
            <Text color="red.500" fontSize="sm">{validationErrors["startDate"]}</Text>
          )}
        </FormControl>

        <FormControl>
          <FormLabel>Finish Date</FormLabel>
          <Input
            type="date"
            value={formData.finishDate || ""}
            onChange={(e) => handleChange('finishDate', e.target.value)}
          />
          {validationErrors.finishDate && (
            <Text color="red.500" fontSize="sm">{validationErrors.finishDate}</Text>
          )}
        </FormControl>

        <FormControl>
          <FormLabel>Hours per Week</FormLabel>
          <NumberInput
            max={40}
            min={0}
            value={formData.hoursPerWeek ?? ""}
            onChange={(_, valueAsNumber) => handleChange("hoursPerWeek", valueAsNumber)}
          >
            <NumberInputField />
          </NumberInput>

          {validationErrors.hoursPerWeek && (
            <Text color="red.500" fontSize="sm">{validationErrors.hoursPerWeek}</Text>
          )}
        </FormControl>

        <HStack justify="flex-start" pt={4}>
          <Button colorScheme="green" onClick={() => handleSave(empid, formData)}>
            Save
          </Button>          
          <Button variant="solid" colorScheme="green" onClick={handleReset}>
            Reset
          </Button>
        </HStack>
      </VStack>
    </Box>
  );
};

export default AddContract;

