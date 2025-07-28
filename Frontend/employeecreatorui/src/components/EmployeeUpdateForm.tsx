import React, { useState, useContext } from 'react';
import {
  Box,
  Button,
  Input,
  FormControl,
  FormLabel,
  Select,
  VStack,
  Heading,
  Divider,
  NumberInput,
  NumberInputField,
  HStack,
  IconButton,
  Text,
  useToast,
} from '@chakra-ui/react';
import { DeleteIcon } from '@chakra-ui/icons';

import { EmployeeGetDTO } from "../types/Employee";
import { ContractViewDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";


type Props = {
  employee: EmployeeGetDTO;
  onUpdate: (updatedEmployee: EmployeeGetDTO) => void;
  setisModalOpen: (value: boolean) => void;
};

const EmployeeUpdateForm: React.FC<Props> = ({ employee, onUpdate, setisModalOpen }) => {
  const context = useContext(EmployeeContext);
  const toast = useToast();

  if (!context) throw new Error("ViewEmployees must be used within an EmployeeProvider");
  const { updateEmployee } = context;

  const [formData, setFormData] = useState<EmployeeGetDTO>(employee);

  const handleChange = (field: keyof EmployeeGetDTO, value: string) => {
    setFormData({ ...formData, [field]: value });
  };

  const handleContractChange = (
    index: number,
    field: keyof ContractViewDTO,
    value: string | number
  ) => {
    const updatedContracts = [...(formData.contracts ?? [])];
    updatedContracts[index] = { ...updatedContracts[index], [field]: value };
    setFormData({ ...formData, contracts: updatedContracts });
  };

  const handleDeleteContract = (index: number) => {
    const updatedContracts = [...formData.contracts ?? []];
    updatedContracts.splice(index, 1);
    setFormData({ ...formData, contracts: updatedContracts });
  };

  const handleSubmit = async () => {
    try {
      console.log("Sending form data: ", formData);
      await updateEmployee(formData.id, formData);
      toast({
        title: 'Success!',
        description: 'successfully update employee with id: ' + formData.id,
        status: 'success',
        duration: 3500,
        isClosable: true,
      });      
      setisModalOpen(false);
    } catch (error) {
      console.error("Error submitting form:", error);
    }
  };

  const handleCancel = () => {
    setisModalOpen(false);
  };

  return (
    <Box maxW="800px" mx="auto" p={6} bg="white" shadow="md" borderRadius="md">
      <Heading mb={4}>Update Employee</Heading>
      <VStack spacing={4} align="stretch">
        <FormControl>
          <FormLabel>First Name</FormLabel>
          <Input
            value={formData.firstName}
            onChange={(e) => handleChange('firstName', e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Middle Name</FormLabel>
          <Input
            value={formData.middleName ?? ""}
            onChange={(e) => handleChange('middleName', e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Last Name</FormLabel>
          <Input
            value={formData.lastName}
            onChange={(e) => handleChange('lastName', e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Email</FormLabel>
          <Input
            value={formData.email}
            onChange={(e) => handleChange('email', e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Mobile Number</FormLabel>
          <Input
            value={formData.mobileNumber}
            onChange={(e) => handleChange('mobileNumber', e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Residential Address</FormLabel>
          <Input
            value={formData.residentialAddress}
            onChange={(e) => handleChange('residentialAddress', e.target.value)}
          />
        </FormControl>

        {/* <FormControl>
          <FormLabel>Status</FormLabel>
          <Select
            value={formData.employeeStatus}
            onChange={(e) => handleChange('employeeStatus', e.target.value)}
          >
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </Select>
        </FormControl> */}

        <Divider my={4} />
        <Heading size="md">Contracts</Heading>

        {formData.contracts?.map((contract, index) => (
          <Box
            key={contract.id}
            p={4}
            border="1px solid #ccc"
            borderRadius="md"
            position="relative"
          >
            <HStack justify="space-between" mb={2}>
              <Text fontWeight="bold">Contract #{contract.id}</Text>
              <IconButton
                aria-label="Delete contract"
                icon={<DeleteIcon />}
                colorScheme="red"
                size="sm"
                onClick={() => handleDeleteContract(index)}
              />
            </HStack>

            <FormControl mb={2}>
              <FormLabel>Contract Type</FormLabel>
              {/* <Input
                value={contract.contract_type}
                onChange={(e) =>
                  handleContractChange(index, 'contract_type', e.target.value)
                }
              /> */}
              <Select
                value={contract.contractType}
                onChange={(e) =>
                  handleContractChange(index, 'contractType', e.target.value)
                }
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
                onChange={(e) =>
                  handleContractChange(index, 'startDate', e.target.value)
                }
              />
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Finish Date</FormLabel>
              <Input
                type="date"
                value={contract.finishDate ?? ''}
                onChange={(e) =>
                  handleContractChange(index, 'finishDate', e.target.value)
                }
              />
            </FormControl>

            <FormControl mb={2}>
              <FormLabel>Work Type</FormLabel>
              <Select
                value={contract.workType}
                onChange={(e) =>
                  handleContractChange(index, 'workType', e.target.value)
                }
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
                onChange={(_, value) =>
                  handleContractChange(index, 'hoursPerWeek', value)
                }
              >
                <NumberInputField />
              </NumberInput>
            </FormControl>
          </Box>
        ))}

        {formData.contracts?.length === 0 && (
          <Text color="gray.500">No contracts available.</Text>
        )}

        <Button colorScheme="blue" mt={1} onClick={handleSubmit}>
          Update
        </Button>
        <Button colorScheme="green" mt={1} onClick={handleCancel}>
          Cancel
        </Button>        
      </VStack>
    </Box>
  );
};

export default EmployeeUpdateForm;
