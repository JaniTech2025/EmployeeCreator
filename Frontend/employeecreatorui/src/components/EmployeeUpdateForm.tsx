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
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
} from '@chakra-ui/react';

import { EmployeeGetDTO } from "../types/Employee";
import { ContractViewDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";
import AddContract from './AddContract';
import EditRecentContract from './EditRecentContract';
import { employeeUpdateSchema } from './validators/EmployeeSchema';
import EmpModal from './EmpModal';

import { ZodError } from 'zod';



type Props = {
  employee: EmployeeGetDTO;
  onUpdate: (updatedEmployee: EmployeeGetDTO) => void;
  setisModalOpen: (value: boolean) => void;
};

const EmployeeUpdateForm: React.FC<Props> = ({ employee, onUpdate, setisModalOpen }) => {
  const context = useContext(EmployeeContext);
  const toast = useToast();
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});    

  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState<number | null>(null);
  const [hasUnsavedChanges, setHasUnsavedChanges] = useState(false);



  if (!context) throw new Error("ViewEmployees must be used within an EmployeeProvider");
  const { updateEmployee, getEmployeeById, 
          refreshEmployees, refreshRecentContract } = context;

  const [formData, setFormData] = useState<EmployeeGetDTO>(employee);

  const handleChange = (field: keyof EmployeeGetDTO, value: string) => {
    setFormData({ ...formData, [field]: value });
    setHasUnsavedChanges(true);     
  };

  const handleContractChange = (
    index: number,
    updatedContract: ContractViewDTO
  ) => {
    const updatedContracts = [...(formData.contracts ?? [])];
    updatedContracts[index] = updatedContract;
    setFormData({ ...formData, contracts: updatedContracts });
    setHasUnsavedChanges(true);
  };


  //This is to update formdata when AddContract is used to create a new contract
  // In turn, this should help refreshing edit Recent contract section
const handleContractAdded = async () => {
  try {
    await refreshEmployees();
    setFormData(employee);
    setHasUnsavedChanges(true)    
  } catch (error) {
    console.error("Failed to refresh employees:", error);
  }
};

  const handleSubmit = async () => {
    try {
      const parsedData = employeeUpdateSchema.parse(formData);

      setValidationErrors({});


      // Spreading parsed data after validation over formData, makes
      const dataToSend = {
        ...formData,          
        ...parsedData,        
        id: formData.id   
      };
      console.log("Sending form data: ", dataToSend);
      // await updateEmployee(formData.id, formData);

      // To allow transformations
      await updateEmployee(dataToSend.id, dataToSend);

      toast({
        title: 'Success!',
        description: 'successfully update details for employee: ' + formData.firstName + " " + formData.lastName,
        status: 'success',
        duration: 3500,
        isClosable: true,
      });      
      setisModalOpen(false);
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
          console.log("Validation errors:", errors);
      } else {
        console.error("Error submitting form", err);
      }
    }
  };

  const handleCancel = () => {
    setisModalOpen(false);
  };

  
// const handleDeleteContract = (index: number) => {
//   const confirmed = window.confirm("Are you sure you want to delete this contract?");
//   if (!confirmed) return;

//   const updatedContracts = [...(formData.contracts ?? [])];
//   updatedContracts.splice(index, 1);

//   setFormData({ ...formData, contracts: updatedContracts });

//   toast({
//     title: "Contract removed!",
//     description: "Confirm deletion by clicking Save.",
//     status: "success",
//     duration: 3500,
//     isClosable: true,
//   });
// };

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
          {validationErrors["firstName"] && (
            <Text color="red.500" fontSize="sm">
              {validationErrors["firstName"]}
            </Text>    
          )}        
          </FormControl>

          <FormControl>
            <FormLabel>Middle Name</FormLabel>
            <Input
              value={formData.middleName ?? ""}
              onChange={(e) => handleChange("middleName", e.target.value)}
            />
            {validationErrors.middleName && (
              <Text color="red.500" fontSize="sm">
                {validationErrors.middleName}
              </Text>
            )}
          </FormControl>

          <FormControl>
            <FormLabel>Last Name</FormLabel>
            <Input
              value={formData.lastName}
              onChange={(e) => handleChange("lastName", e.target.value)}
            />
          {validationErrors["lastName"] && (
            <Text color="red.500" fontSize="sm">
              {validationErrors["lastName"]}
            </Text>    
          )}             
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
            {validationErrors["email"] && (
              <Text color="red.500" fontSize="sm">
                {validationErrors["email"]}
              </Text>  
            )}
          </FormControl>

          <FormControl>
            <FormLabel>Mobile Number</FormLabel>
            <Input
              value={formData.mobileNumber}
              onChange={(e) => handleChange("mobileNumber", e.target.value)}
            />
            {validationErrors["mobileNumber"] && (
              <Text color="red.500" fontSize="sm">
                {validationErrors["mobileNumber"]}
              </Text>  
            )}            
          </FormControl>

          <FormControl>
            <FormLabel>Residential Address</FormLabel>
            <Input
              value={formData.residentialAddress}
              onChange={(e) =>
                handleChange("residentialAddress", e.target.value)
              }
            />
            {validationErrors["residentialAddress"] && (
              <Text color="red.500" fontSize="sm">
                {validationErrors["residentialAddress"]}
              </Text>
            )}            
          </FormControl>
        </VStack>
      </AccordionPanel>
    </AccordionItem>
  </Accordion>



   
          {formData.contracts && formData.contracts.length > 0 ? (
            <EditRecentContract
              id={formData.id}
              contract={formData.contracts[0]}   // always the most recent
              index={0}
              onContractChange={(updatedContract) =>
                handleContractChange(0, updatedContract)
              }
              onDeleteContract={() => handleDeleteContract(0)}
            />
          ) : (
            <Text color="gray.500" fontStyle="italic">
              No contracts available. Add a new contract below.
            </Text>
          )}

         <AddContract 
           empid={formData.id}
           previousContract={formData.contracts?.[0] ?? null}
           onContractAdded={handleContractAdded}
          />

      
      </VStack>
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
                  You have unsaved changes. Click <b>Save</b> to update the employee record.
                </Text>
              </Box>
            )}

            <Button colorScheme="blue" mt={6}   ml={4} onClick={handleSubmit}>
              Save
            </Button>

            <Button colorScheme="red" mt={6}  ml={4} onClick={handleCancel}>
              Cancel
            </Button>     

            <EmpModal isOpen={isDeleteModalOpen} onClose={() => setIsDeleteModalOpen(false)}>
              <VStack spacing={4} align="stretch">
                <Text fontSize="lg" fontWeight="bold">
                  Delete Contract
                </Text>
                <Text>
                  Are you sure you want to delete this contract?
                </Text>
                <Box display="flex" justifyContent="flex-end" gap={3}>
                  <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>
                    Cancel
                  </Button>
                  <Button colorScheme="red" onClick={confirmDelete}>
                    Delete
                  </Button>
                </Box>
              </VStack>
            </EmpModal>
   
 
    </Box>
  );
};

export default EmployeeUpdateForm;