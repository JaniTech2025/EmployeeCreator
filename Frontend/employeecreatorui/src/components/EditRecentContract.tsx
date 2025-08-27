import React, { useState, useContext } from 'react';
import {
  Box,
  HStack,
  FormControl,
  FormLabel,
  Select,
  Input,
  NumberInput,
  NumberInputField,
  Text,
  IconButton,
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
  contract,
  onContractChange,
  onDeleteContract,
}) => {
  const context = useContext(EmployeeContext);
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);




  const [localFinishDate, setLocalFinishDate] = useState(contract?.finishDate || "");

const handleFinishDateChange = (value: string) => {
  setLocalFinishDate(value); // local input state
  console.log("Got finish date as" + value);

  if (!contract){ 
    console.log("Got stuck at invalid contract data");
    return;
  };

  console.log("proceeding");

  const updatedContract = { ...contract, finishDate: value };

  onContractChange(updatedContract); 


  const result = schema.safeParse(updatedContract);
  if (!result.success) {

    const fieldErrors: Record<string, string> = {};
    result.error.issues.forEach(issue => {
        const field = issue.path[0] as string;
        fieldErrors[field] = issue.message;
      });
    setValidationErrors(prev => ({ ...prev, ...fieldErrors }));

  } else {
    setValidationErrors(prev => {
      const newErrors = { ...prev };
      delete newErrors.finishDate;
      return newErrors;
    });

  }
};




  if (!context) throw new Error('EditRecentContract must be used within EmployeeProvider');

  const schema = getContractSchema();


  // const handleFieldChange = (field: keyof ContractViewDTO, value: string | number) => {
  //   if (!contract) return;

  //   const updatedContract = { ...contract, [field]: value };

  //   const result = schema.safeParse(updatedContract);
  //   if (!result.success) {
  //     const fieldErrors: Record<string, string> = {};
  //     result.error.issues.forEach(issue => {
  //       if (issue.path[0] === field) fieldErrors[field] = issue.message;
  //     });
  //     setValidationErrors(prev => ({ ...prev, ...fieldErrors }));
  //   } else {
  //     setValidationErrors(prev => {
  //       const newErrors = { ...prev };
  //       delete newErrors[field];
  //       return newErrors;
  //     });

  //     onContractChange(updatedContract); 
  //   }
  // };

  


  if (loading) return <Text>Loading recent contract...</Text>;
  if (!contract) return <Text color="gray.500">No contracts available.</Text>;

  return (
    <Box p={4} bg="blue.50" borderRadius="md" border="1px solid" borderColor="gray.300">
      <HStack justify="flex-end" mb={2}>
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
          value={contract.contractType} isReadOnly>
          <option value="Permanent">Permanent</option>
          <option value="Temporary">Temporary</option>
        </Select>
      </FormControl>

      <FormControl mb={2} isDisabled>
        <FormLabel>Start Date</FormLabel>
        <Input
          type="date"
          value={contract.startDate}
          isReadOnly
        />
        {validationErrors.startDate && (
          <Text color="red.500" fontSize="sm">{validationErrors.startDate}</Text>
        )}
      </FormControl>

    <FormControl mb={2}>
      <FormLabel>Finish Date</FormLabel>
      <Input
        type="date"
        value={localFinishDate} // use local state here
        onChange={(e) => handleFinishDateChange(e.target.value)}
      />
      {validationErrors.finishDate && (
        <Text color="red.500" fontSize="sm">{validationErrors.finishDate}</Text>
      )}
    </FormControl>


      <FormControl mb={2} isDisabled>
        <FormLabel>Work Type</FormLabel>
        <Select
          value={contract.workType}
          isReadOnly
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
          isReadOnly
        >
          <NumberInputField />
        </NumberInput>
        {validationErrors.hoursPerWeek && (
          <Text color="red.500" fontSize="sm">{validationErrors.hoursPerWeek}</Text>
        )}
      </FormControl>
    </Box>
  );
};

export default EditRecentContract;
