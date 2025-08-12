import React, { useState, useContext } from 'react';
import {
  Box,
  Button,
  Input,
  FormControl,
  FormLabel,
  VStack,
  Heading,
  Divider,
  useToast,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
} from '@chakra-ui/react';


import { DeleteIcon, RepeatIcon } from '@chakra-ui/icons';

import { EmployeeGetDTO } from "../types/Employee";
import { ContractViewDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";
import AddContract from './AddContract';
import EditRecentContract from './EditRecentContract';


type Props = {
  employee: EmployeeGetDTO;
  onUpdate: (updatedEmployee: EmployeeGetDTO) => void;
  setisModalOpen: (value: boolean) => void;
};

const EmployeeUpdateForm: React.FC<Props> = ({ employee, onUpdate, setisModalOpen }) => {
  const context = useContext(EmployeeContext);
  const toast = useToast();

  if (!context) throw new Error("ViewEmployees must be used within an EmployeeProvider");
  const { updateEmployee, getEmployeeById, 
          refreshEmployees, refreshRecentContract } = context;

  const [formData, setFormData] = useState<EmployeeGetDTO>(employee);

  const handleChange = (field: keyof EmployeeGetDTO, value: string) => {
    setFormData({ ...formData, [field]: value });
  };

  const handleContractChange = (
    index: number,
    updatedContract: ContractViewDTO
  ) => {
    const updatedContracts = [...(formData.contracts ?? [])];
    updatedContracts[index] = updatedContract;
    setFormData({ ...formData, contracts: updatedContracts });
  };


  //This is to update formdata when AddContract is used to create a new contract
  // In turn, this should help refreshing edit Recent contract section
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
      console.log("Sending form data: ", formData);
      await updateEmployee(formData.id, formData);
      toast({
        title: 'Success!',
        description: 'successfully update details for employee: ' + formData.firstName + " " + formData.lastName,
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

  const handleDeleteContract = (index: number) => {
    const updatedContracts = [...formData.contracts ?? []];
    updatedContracts.splice(index, 1);
    setFormData({ ...formData, contracts: updatedContracts });
  };



  return (
    <Box maxW="800px" mx="auto" p={6} bg="white" shadow="md" borderRadius="xl">
      <Heading mb={4}>Update Employee</Heading>
      <Divider mb={4} borderColor="blue.300" />

      <VStack spacing={4} align="stretch">

      <Accordion allowToggle bg="blue.50">
        <AccordionItem
        border="1px solid"
        borderColor="gray.300"
        borderRadius="md"
        overflow="hidden"
        // mb={2}
      >
      <h2>
      <AccordionButton _expanded={{ bg: "blue.400" }}>    
         <Box flex="1" textAlign="left">
            Update Employee Details
          </Box>
          <AccordionIcon />
        </AccordionButton>
      </h2>
      <AccordionPanel pb={4}>
        <VStack spacing={4} align="stretch">
          <FormControl>
            <FormLabel>First Name</FormLabel>
            <Input
              value={formData.firstName}
              onChange={(e) => handleChange("firstName", e.target.value)}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Middle Name</FormLabel>
            <Input
              value={formData.middleName ?? ""}
              onChange={(e) => handleChange("middleName", e.target.value)}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Last Name</FormLabel>
            <Input
              value={formData.lastName}
              onChange={(e) => handleChange("lastName", e.target.value)}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Photo URL</FormLabel>
            <Input
              value={formData.photoUrl}
              isDisabled={true}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Email</FormLabel>
            <Input
              value={formData.email}
              onChange={(e) => handleChange("email", e.target.value)}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Mobile Number</FormLabel>
            <Input
              value={formData.mobileNumber}
              onChange={(e) => handleChange("mobileNumber", e.target.value)}
            />
          </FormControl>

          <FormControl>
            <FormLabel>Residential Address</FormLabel>
            <Input
              value={formData.residentialAddress}
              onChange={(e) =>
                handleChange("residentialAddress", e.target.value)
              }
            />
          </FormControl>
        </VStack>
      </AccordionPanel>
    </AccordionItem>
  </Accordion>



   

        <EditRecentContract
          id={formData.id}
          contract={formData.contracts?.[0]}   
          index={0}                            
          onContractChange={(updatedContract) =>
            handleContractChange(0, updatedContract)
          }
          onDeleteContract={() => handleDeleteContract(0)}
        />

         <AddContract 
           empid={formData.id}
           previousContract={formData.contracts?.[0] ?? null}
           onContractAdded={handleContractAdded}
          />

      
      </VStack>

         <Button colorScheme="blue" mt={6} onClick={handleSubmit}>
              Save
            </Button>
            <Button colorScheme="red" mt={6}  ml={4} onClick={handleCancel}>
              Cancel
            </Button>        
 
    </Box>
  );
};

export default EmployeeUpdateForm;