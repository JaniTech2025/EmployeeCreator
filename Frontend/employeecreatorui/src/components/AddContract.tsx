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

import React, { useState } from 'react';
import { ContractCreateDTO } from '../types/Contract';
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
  useToast
} from '@chakra-ui/react';

// interface AddContractData {
//   contractType: string;
//   startDate: string;
//   finishDate: string;
//   workType: string;
//   hoursPerWeek: number;
//   createdAt: string;
//   updatedAt: string;
// }

const AddContract: React.FC = () => {
  const initialFormState: ContractCreateDTO = {
    contractType: 'Temporary',
    startDate: '2023-07-07',
    finishDate: '2023-12-07',
    workType: 'FullTime',
    hoursPerWeek: 36.8,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    ongoing: true
  };

  const [formData, setFormData] = useState<ContractCreateDTO>(initialFormState);
  const toast = useToast();

  const handleChange = (field: keyof ContractCreateDTO, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleSave = () => {
    toast({
      title: 'Saved!',
      description: 'Contract details saved successfully.',
      status: 'success',
      duration: 3000,
      isClosable: true,
    });

    // Replace with your actual save logic (e.g. API call)
    console.log('Saved:', formData);
  };

  const handleDiscard = () => {
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
    <Accordion allowToggle>
      <AccordionItem border="1px solid" borderColor="gray.200" borderRadius="md">
        <h2>
          <AccordionButton _expanded={{ bg: "blue.400" }}>
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
                <option value="Contract">Contract</option>
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
                <option value="Casual">Casual</option>
              </Select>
            </FormControl>

            <FormControl>
              <FormLabel>Start Date</FormLabel>
              <Input
                type="date"
                value={formData.startDate}
                onChange={(e) => handleChange('startDate', e.target.value)}
              />
            </FormControl>

            <FormControl>
              <FormLabel>Finish Date</FormLabel>
              <Input
                type="date"
                value={formData.finishDate || ""}
                onChange={(e) => handleChange('finishDate', e.target.value)}
              />
            </FormControl>

            <FormControl>
              <FormLabel>Hours per Week</FormLabel>
              <NumberInput
                value={formData.hoursPerWeek}
                onChange={(_, value) => handleChange('hoursPerWeek', value)}
              >
                <NumberInputField />
              </NumberInput>
            </FormControl>

            <HStack justify="flex-end" pt={4}>
              <Button variant="outline" colorScheme="gray" onClick={handleDiscard}>
                Discard
              </Button>
              <Button colorScheme="blue" onClick={handleSave}>
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