// If no, Show contract modal
// Update contract for employee using put contract along with employee id, Update recent contract Finish date with current date, if null
// Props to be passed: Empid, recent contract (if existing)
// Validation: new contract date is greater than finish date of previous contract
// Result: New contract created, previous contract closed (with finish date), if existing


        //  {
        //         "contractType": "Temporary",
        //         "startDate": "2023-07-07",
        //         "finishDate": "2023-12-07",
        //         "workType": "FullTime",
        //         "hoursPerWeek": 36.8,
        //         "createdAt": "2025-07-30T09:25:57.493501",
        //         "updatedAt": "2024-07-30T09:25:57.4925"
        //     }

import React, { useContext, useState } from 'react';
import { ContractCreateDTO } from '../types/Contract';
import { EmployeeContext } from '../context/EmployeeContext';
import { contractSchemaWithPrevFinishDate } from './validators/ContractSchema';
import {
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
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

import {ZodError, ZodIssue} from 'zod';

// interface AddContractData {
//   contractType: string;
//   startDate: string;
//   finishDate: string;
//   workType: string;
//   hoursPerWeek: number;
//   createdAt: string;
//   updatedAt: string;
// }


type AddContractProps = {
  empid: number;
  previousContract?: ContractCreateDTO | null;   
  onContractAdded?: () => Promise<void>;   
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



  const AddContract: React.FC<AddContractProps> = ({empid, previousContract, onContractAdded }) => {



  console.log(previousContract?.finishDate);  

  const [formData, setFormData] = useState<ContractCreateDTO>(initialFormState);

  const [hoursInput, setHoursInput] = useState(formData.hoursPerWeek?.toString());

  const toast = useToast();
  const context = useContext(EmployeeContext);

  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});


  if (!context) throw new Error("AddContract must be used within an EmployeeProvider");
  const { createContract, refreshEmployees } = context;

  const handleChange = (field: keyof ContractCreateDTO, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleHoursChange = (valueAsString: string, valueAsNumber: number) => {
    setHoursInput(valueAsString);

    if (!isNaN(valueAsNumber) && valueAsNumber >= 10 && valueAsNumber <= 40) {
      setFormData(prev => ({ ...prev, hoursPerWeek: valueAsNumber }));
    }
    };

  // const handleSave = async (empid:number, formData: ContractCreateDTO) => {
  //   // Replace with your actual save logic (e.g. API call)
  //   try{
  //     await createContract(empid, formData);
  //     await refreshEmployees();
  //     toast({
  //         title: 'Saved!',
  //         description: 'Contract details saved successfully.',
  //         status: 'success',
  //         duration: 3000,
  //         isClosable: true,
  //       });
  //       console.log('Saved:', formData);
  //   }catch(error){
  //      console.log("Error creating contract", error);
  //   }
  //   setFormData(initialFormState);
  // };

const handleSave = async (empid: number, formData: ContractCreateDTO) => {
    const schema = contractSchemaWithPrevFinishDate(previousContract?.finishDate || null);
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
    await createContract(empid, formData);

    toast({
      title: "Saved!",
      description: "Contract details saved successfully.",
      status: "success",
      duration: 3000,
      isClosable: true,
    });


    //Notify EmployeeUpdateForm that a new contract has been added
    setFormData(initialFormState);
      if (onContractAdded) {
        await onContractAdded();  
    }


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
    <Accordion allowToggle bgColor="green.50">
      <AccordionItem border="1px solid" borderColor="gray.200" borderRadius="md">
        <h2>
          <AccordionButton _expanded={{ bg: "green.400" }}>
            <Box flex="1" textAlign="left">
              Add new Contract
            </Box>
            <AccordionIcon />
          </AccordionButton>
        </h2>
        <AccordionPanel pb={4}>
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
                <Text color="red.500" fontSize="sm">
                  {validationErrors["startDate"]}
                </Text>
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
                  <Text color="red.500" fontSize="sm">
                    {validationErrors.finishDate}
                  </Text>
             )}              
            </FormControl>

            <FormControl>
              <FormLabel>Hours per Week</FormLabel>
                <NumberInput
                  max={40}
                  min={0}
                  value={hoursInput}
                  onChange={handleHoursChange}
                >
                <NumberInputField />
              </NumberInput>

              {validationErrors.hoursPerWeek && (
                  <Text color="red.500" fontSize="sm">
                    {validationErrors.hoursPerWeek}
                  </Text>
              )}              
            </FormControl>

            <HStack justify="flex-end" pt={4}>
              <Button variant="solid" colorScheme="green" onClick={handleReset}>
                Reset
              </Button>
              <Button colorScheme="green" onClick={() => handleSave(empid, formData)}>
                Save
              </Button>
            </HStack>
          </VStack>
        </AccordionPanel>
      </AccordionItem>
    </Accordion>
  );
};

export default AddContract;