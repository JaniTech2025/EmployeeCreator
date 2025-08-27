import React, { useState, useContext } from 'react';
import {
  Box,
  Button,
  Input,
  FormControl,
  FormLabel,
  VStack,
  Text,
  Heading,
  Divider,
  useToast,
  HStack,
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
} from '@chakra-ui/react';

import { EmployeeGetDTO } from "../types/Employee";
import { ContractCreateDTO, ContractViewDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";
import AddContract from './AddContract';
import EditRecentContract from './EditRecentContract';
import { employeeUpdateSchema } from './validators/EmployeeSchema';
import EmpModal from './EmpModal';
import { ZodError } from 'zod';

type Props = {
  employee: EmployeeGetDTO;
  setisModalOpen: (value: boolean) => void;
};

const EmployeeUpdateForm: React.FC<Props> = ({ employee, setisModalOpen }) => {
  const context = useContext(EmployeeContext);
  const toast = useToast();
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState<number | null>(null);
  const [hasUnsavedChanges, setHasUnsavedChanges] = useState(false);

  if (!context) throw new Error("ViewEmployees must be used within an EmployeeProvider");
  const { updateEmployee, refreshEmployees, refreshRecentContract } = context;

  const [formData, setFormData] = useState<EmployeeGetDTO>(employee);

  const handleChange = (field: keyof EmployeeGetDTO, value: string) => {
    setFormData({ ...formData, [field]: value });
    setHasUnsavedChanges(true);
  };

  const handleContractChange = (index: number, updatedContract: ContractViewDTO) => {
    const updatedContracts = [...(formData.contracts ?? [])];
    updatedContracts[index] = updatedContract;
    setFormData({ ...formData, contracts: updatedContracts });
    setHasUnsavedChanges(true);
  };

  const handleContractAdded = async () => {
    try {
      await refreshEmployees();
      setFormData(employee);
    } catch (error) {
      console.error("Failed to refresh employees:", error);
    }
  };

  const handleSubmit = async () => {
    try {
      setHasUnsavedChanges(false);

      const parsedData = employeeUpdateSchema.parse(formData);
      setValidationErrors({});

      const dataToSend = { ...formData, ...parsedData, id: formData.id };
      await updateEmployee(dataToSend.id, dataToSend);

       setHasUnsavedChanges(false);

      toast({
        title: 'Success!',
        description: `Successfully updated details for ${formData.firstName} ${formData.lastName}`,
        status: 'success',
        duration: 3500,
        isClosable: true,
      });
    } catch (err) {
      if (err instanceof ZodError) {
        const errors: Record<string, string> = {};
        err.issues.forEach((e) => {
          const key = e.path.length > 0 ? e.path.join(".") : "_error";
          errors[key] = e.message;
        });
        setValidationErrors(errors);

        toast({
          title: 'Validation Error',
          description: 'Please fix the errors in the form.',
          status: 'error',
          duration: 3500,
          isClosable: true,
        });
      } else {
        console.error("Error submitting form", err);
      }
    }
  };

  const handleCancel = () => {
    setisModalOpen(false);
  };

  const handleDeleteContract = (index: number) => {
    setDeleteIndex(index);
    setIsDeleteModalOpen(true);
  };

  const confirmDelete = async () => {
    if (deleteIndex === null) return;

    const updatedContracts = [...(formData.contracts ?? [])];
    updatedContracts.splice(deleteIndex, 1);
    setFormData({ ...formData, contracts: updatedContracts });
    setHasUnsavedChanges(true);

    try {
      await refreshEmployees();
      await refreshRecentContract(formData.id);
    } catch (err) {
      console.error("Error refreshing contracts:", err);
    }

    toast({
      title: "Contract removed!",
      description: "Confirm deletion by clicking Save.",
      status: "success",
      duration: 3500,
      isClosable: true,
    });

    setDeleteIndex(null);
    setIsDeleteModalOpen(false);
  };

  return (
    <Box maxW="800px" mx="auto" p={6} bg="white" shadow="md" borderRadius="xl">
      <Heading mb={4}>Employee Management</Heading>
      <Divider mb={4} borderColor="black.300" />

      <Tabs variant="enclosed" colorScheme="blue">
        <TabList>
          <Tab>Update Employee</Tab>
          <Tab>Edit latest Contract</Tab>
          <Tab>Add Contract</Tab>
        </TabList>

        <TabPanels>
          {/* Update Employee Tab */}
          <TabPanel>
            <VStack spacing={4} align="stretch">
              {/* Employee Form */}
              {["firstName", "middleName", "lastName", "email", "mobileNumber", "residentialAddress"].map((field) => (
                <FormControl key={field}>
                  <FormLabel>{field.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}</FormLabel>
                  <Input
                    value={(formData as any)[field] ?? ""}
                    onChange={(e) => handleChange(field as keyof EmployeeGetDTO, e.target.value)}
                    isDisabled={field === "photoUrl"}
                  />
                  {validationErrors[field] && (
                    <Text color="red.500" fontSize="sm">{validationErrors[field]}</Text>
                  )}
                </FormControl>
              ))}

              {hasUnsavedChanges && (
                <Box bg="yellow.100" border="1px solid" borderColor="yellow.300" p={3} borderRadius="md" mb={4}>
                  <Text fontSize="sm" color="yellow.800">
                    You have unsaved changes. Click <b>Save</b> to update the employee record.
                  </Text>
                </Box>
              )}

              <HStack spacing={2} mt={2}>
                <Button colorScheme="blue" onClick={handleSubmit}>Save</Button>
                <Button colorScheme="red" onClick={handleCancel}>Close</Button>
              </HStack>
            </VStack>
          </TabPanel>

          {/* Edit Recent Contract Tab */}
          <TabPanel>
            {formData.contracts && formData.contracts.length > 0 ? (
              <EditRecentContract
                id={formData.id}  
                contract={formData.contracts[0]}
                index={0}
                onContractChange={(updatedContract) => handleContractChange(0, updatedContract)}
                onDeleteContract={() => handleDeleteContract(0)}
              />

            ) : (
              <Text color="gray.500" fontStyle="italic">No contracts available.</Text>
            )}

              {hasUnsavedChanges && (
                  <Box
                    bg="yellow.100"
                    border="1px solid"
                    borderColor="yellow.300"
                    p={3}
                    borderRadius="md"
                    mb={4}
                  >
                    <Text fontSize="sm" color="yellow.800">
                      You have unsaved changes. Click <b>Save</b> to update the record.
                    </Text>
                  </Box>
                )}


            <HStack spacing={2} mt={2}>
              <Button colorScheme="blue" onClick={handleSubmit}>Save</Button>
              <Button colorScheme="red" onClick={handleCancel}>Close</Button>
            </HStack>            
          </TabPanel>

          {/* Add Contract Tab */}
          <TabPanel>
            <AddContract
              empid={formData.id}
              previousContract={formData.contracts?.[0] ?? null}
              onContractAdded={handleContractAdded}
            />
          </TabPanel>
        </TabPanels>
      </Tabs>

      <EmpModal isOpen={isDeleteModalOpen} onClose={() => setIsDeleteModalOpen(false)}>
        <VStack spacing={4} align="stretch">
          <Text fontSize="lg" fontWeight="bold">Delete Contract</Text>
          <Text>Are you sure you want to delete this contract?</Text>
          <Box display="flex" justifyContent="flex-end" gap={3}>
            <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Cancel</Button>
            <Button colorScheme="red" onClick={confirmDelete}>Delete</Button>
          </Box>
        </VStack>
      </EmpModal>
    </Box>
  );
};

export default EmployeeUpdateForm;
