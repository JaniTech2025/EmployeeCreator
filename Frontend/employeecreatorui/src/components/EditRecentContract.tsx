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

import { DeleteIcon, RepeatIcon } from '@chakra-ui/icons';

import { ContractViewDTO } from '../types/Contract';
import { EmployeeContext } from '../context/EmployeeContext';

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
  const toast = useToast();

  if (!context) throw new Error('EditRecentContract must be used within EmployeeProvider');
  const { refreshRecentContract } = context;

  const [loading, setLoading] = useState(false);

  const handleRefresh = async () => {
    setLoading(true);
    try {
      const latestContract = await refreshRecentContract(id);
      if (latestContract) {
        toast({
          title: 'Contract refreshed',
          status: 'success',
          duration: 2000,
          isClosable: true,
        });
        onContractChange(latestContract); 
      } else {
        toast({
          title: 'No contract found',
          status: 'info',
          duration: 2000,
          isClosable: true,
        });
      }
    } catch (error) {
      toast({
        title: 'Failed to refresh contract',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
      console.error('Failed to refresh recent contract', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFieldChange = (field: keyof ContractViewDTO, value: string | number) => {
    if (!contract) return;
    const updatedContract = { ...contract, [field]: value };
    onContractChange(updatedContract); // notify parent on every change
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
              <IconButton
                aria-label="Refresh recent contract"
                icon={<RepeatIcon />}
                size="sm"
                colorScheme="blue"
                onClick={(e) => {
                  e.stopPropagation();
                  handleRefresh();
                }}
                ml={2}
              />
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

            <FormControl mb={2}>
              <FormLabel>Contract Type</FormLabel>
              <Select
                value={contract.contractType}
                onChange={(e) => handleFieldChange('contractType', e.target.value)}
              >
                <option value="Permanent">Permanent</option>
                <option value="Temporary">Temporary</option>
              </Select>
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Start Date</FormLabel>
              <Input
                type="date"
                value={contract.startDate}
                onChange={(e) => handleFieldChange('startDate', e.target.value)}
              />
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Finish Date</FormLabel>
              <Input
                type="date"
                value={contract.finishDate ?? ''}
                onChange={(e) => handleFieldChange('finishDate', e.target.value)}
              />
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Work Type</FormLabel>
              <Select
                value={contract.workType}
                onChange={(e) => handleFieldChange('workType', e.target.value)}
              >
                <option value="FullTime">Full Time</option>
                <option value="PartTime">Part Time</option>
              </Select>
            </FormControl>

            <FormControl>
              <FormLabel>Hours/Week</FormLabel>
              <NumberInput
                precision={1}
                step={0.1}
                value={contract.hoursPerWeek}
                onChange={(_, value) => handleFieldChange('hoursPerWeek', value)}
              >
                <NumberInputField />
              </NumberInput>
            </FormControl>
          </AccordionPanel>
        </AccordionItem>
      </Accordion>
  );
};

export default EditRecentContract;