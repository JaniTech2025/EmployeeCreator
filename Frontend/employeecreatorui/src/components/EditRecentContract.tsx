import React, { useState, useContext } from 'react';
import {
  Box,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
  IconButton,
  HStack,
  FormControl,
  FormLabel,
  Select,
  Input,
  NumberInput,
  NumberInputField,
  Text,
  useToast,
} from '@chakra-ui/react';

import { DeleteIcon } from '@chakra-ui/icons';

import { ContractViewDTO } from '../types/Contract';
import { EmployeeContext } from '../context/EmployeeContext';
import { getContractSchema } from './validators/ContractSchema';


type EditRecentContractProps = {
  id: number;
  contract?: ContractViewDTO;
  index: number;
  onContractChange: (updatedContract: ContractViewDTO) => void;
  onDeleteContract: () => void;
};

const EditRecentContract: React.FC<EditRecentContractProps> = ({
  id,
  contract,
  onContractChange,
  onDeleteContract,
}) => {
  const context = useContext(EmployeeContext);

  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});


  if (!context) throw new Error('EditRecentContract must be used within EmployeeProvider');

  const [loading, setLoading] = useState(false);


const schema = getContractSchema();

const handleFieldChange = (field: keyof ContractViewDTO, value: string | number) => {
  if (!contract) return;

  const updatedContract = { ...contract, [field]: value };

  const result = schema.safeParse(updatedContract);

  if (!result.success) {
    const fieldErrors: Record<string, string> = {};

    result.error.issues.forEach(issue => {
      const errorPath = issue.path[0];
      if (errorPath === field) {
        fieldErrors[field] = issue.message;
      }
    });

    setValidationErrors(prev => ({ ...prev, ...fieldErrors }));
  } else {
    setValidationErrors(prev => {
      const newErrors = { ...prev };
      delete newErrors[field];
      return newErrors;
    });

    onContractChange(updatedContract);
  }
};


  if (loading) return <Text>Loading recent contract...</Text>;

  if (!contract) return <Text color="gray.500">No contracts available.</Text>;

  return (
      <Accordion allowToggle bgColor="blue.50">
      <AccordionItem border="1px solid" borderColor="gray.300" borderRadius="md">
          <h2>
            <AccordionButton _expanded={{ bg: 'blue.400' }}>
              <Box flex="1" textAlign="left">
                Edit Recent Contract
              </Box>
              <AccordionIcon />
            </AccordionButton>
          </h2>
          <AccordionPanel pb={4}>
            <HStack justify="space-between" mb={2} justifyContent="right">
              <IconButton
                aria-label="Delete contract"
                icon={<DeleteIcon />}
                colorScheme="red"
                size="sm"
                onClick={onDeleteContract}
              />
            </HStack>

            <FormControl mb={2} isDisabled>
              <FormLabel>Contract Type</FormLabel>
              <Select
                value={contract.contractType}
                onChange={(e) => handleFieldChange('contractType', e.target.value)}
              >
                <option value="Permanent">Permanent</option>
                <option value="Temporary">Temporary</option>
              </Select>
            </FormControl>

            <FormControl mb={2} isDisabled>
              <FormLabel>Start Date</FormLabel>
              <Input
                type="date"
                value={contract.startDate}
                onChange={(e) => handleFieldChange('startDate', e.target.value)}
              />
            {validationErrors["startDate"] && (
                <Text color="red.500" fontSize="sm">
                {validationErrors["startDate"]}
                </Text>    
            )}                  
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Finish Date</FormLabel>
              <Input
                type="date"
                value={contract.finishDate ?? ''}
                onChange={(e) => handleFieldChange('finishDate', e.target.value)}
              />
            {validationErrors["finishDate"] && (
                <Text color="red.500" fontSize="sm">
                {validationErrors["finishDate"]}
                </Text>    
            )}  
            </FormControl>

            <FormControl mb={2} isDisabled>
              <FormLabel>Work Type</FormLabel>
              <Select
                value={contract.workType}
                onChange={(e) => handleFieldChange('workType', e.target.value)}
              >
                <option value="FullTime">Full Time</option>
                <option value="PartTime">Part Time</option>
              </Select>
            </FormControl>

            <FormControl isDisabled>
              <FormLabel>Hours/Week</FormLabel>
              <NumberInput
                precision={1}
                step={0.1}
                value={contract.hoursPerWeek}
                onChange={(_, value) => handleFieldChange('hoursPerWeek', value)}
              >
                <NumberInputField />
              </NumberInput>
            {validationErrors["hoursPerWeek"] && (
                <Text color="red.500" fontSize="sm">
                {validationErrors["hoursPerWeek"]}
                </Text>    
            )}                
            </FormControl>
          </AccordionPanel>
        </AccordionItem>
      </Accordion>
  );
};

export default EditRecentContract;