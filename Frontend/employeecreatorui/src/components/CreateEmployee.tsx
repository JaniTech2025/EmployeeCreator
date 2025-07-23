import React, { useContext, useState } from "react";
import { EmployeeCreateDTO } from "../types/Employee";
import { ContractCreateDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";


import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Stack,
  Text,
  Switch,
  NumberInput,
  NumberInputField,
  Select,
  Divider,
  useDisclosure,
  VStack
} from '@chakra-ui/react';


const defaultContract: ContractCreateDTO = {
  contractType: "",
  startDate: "",
  ongoing: false,
  workType: "FullTime",
};

interface CreateEmployeeProps {
  onClose: () => void;
}

export const CreateEmployee: React.FC<CreateEmployeeProps> = ({onClose}) => {

  const context = useContext(EmployeeContext);
    if (!context) {
    throw new Error('CreateEmployee must be used within an EmployeeProvider');
  }


  const { createNewEmployee } = context;


  const [employee, setEmployee] = useState<EmployeeCreateDTO>({
    firstName: "",
    middleName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    residentialAddress: "",
    employeeStatus: "Active",
    photoUrl: "",
    createdAt: new Date().toISOString().split("T")[0],
    updatedAt: new Date().toISOString().split("T")[0],
    contracts: [defaultContract]
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setEmployee((prev) => ({ ...prev, [name]: value }));
  };

  const handleContractChange = (
    index: number,
    field: keyof ContractCreateDTO,
    value: string | boolean | number | null
  ) => {
    const updatedContracts = employee.contracts?.map((c, i) =>
      i === index ? { ...c, [field]: value } : c
    );
    setEmployee((prev) => ({ ...prev, contracts: updatedContracts }));
  };

  // const addContract = () => {
  //   setEmployee((prev) => ({
  //     ...prev,
  //     contracts: [...(prev.contracts || []), defaultContract],
  //   }));
  // };

   const handleCancel = () => {
    console.log("Cancelling", employee);
    // Call your API here
    
  };


  const handleSubmit = async() => {
    try {
      console.log("Submitted:", employee);
      await createNewEmployee(employee);
      onClose();  
    } catch (err) {
      console.error("Failed to create employee", err);
    }
  };

  return (
    <Box maxW="800px" mx="auto" p={6}>
      <Text fontSize="2xl" mb={4}>Create Employee</Text>
      <Divider mb={4} borderColor="gray.500" />

      <VStack spacing={4} align="stretch">
        <FormControl isRequired>
          <FormLabel>First Name</FormLabel>
          <Input name="firstName" value={employee.firstName} onChange={handleChange} />
        </FormControl>

        <FormControl>
          <FormLabel>Middle Name</FormLabel>
          <Input name="middleName" value={employee.middleName} onChange={handleChange} />
        </FormControl>

        <FormControl isRequired>
          <FormLabel>Last Name</FormLabel>
          <Input name="lastName" value={employee.lastName} onChange={handleChange} />
        </FormControl>

        <FormControl>
          <FormLabel>Email</FormLabel>
          <Input name="email" type="email" value={employee.email} onChange={handleChange} />
        </FormControl>

        <FormControl isRequired>
          <FormLabel>Mobile Number</FormLabel>
          <Input name="mobileNumber" value={employee.mobileNumber} onChange={handleChange} />
        </FormControl>

        <FormControl>
          <FormLabel>Residential Address</FormLabel>
          <Input name="residentialAddress" value={employee.residentialAddress} onChange={handleChange} />
        </FormControl>

        <FormControl>
          <FormLabel>Employee Status</FormLabel>
          <Select name="employeeStatus" value={employee.employeeStatus} onChange={handleChange}>
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </Select>
        </FormControl>

        <FormControl>
          <FormLabel>Photo URL</FormLabel>
          <Input name="photoUrl" value={employee.photoUrl} onChange={handleChange} />
        </FormControl>

        {employee.contracts?.map((contract, index) => (
          <Box key={index} p={4} border="1px solid #ccc" borderRadius="md">
            <Text fontWeight="bold" mb={5} >Contract Details</Text>
            <Divider mb={4} borderColor="gray.500" />

            <Stack spacing={3}>
              <FormControl isRequired>
                <FormLabel>Contract Type</FormLabel>
                <Input
                  value={contract.contractType}
                  onChange={(e) => handleContractChange(index, "contractType", e.target.value)}
                />
              </FormControl>

              <FormControl>
                <FormLabel>Contract Term</FormLabel>
                <Input
                  value={contract.contractTerm || ""}
                  onChange={(e) => handleContractChange(index, "contractTerm", e.target.value)}
                />
              </FormControl>

              <FormControl isRequired>
                <FormLabel>Start Date</FormLabel>
                <Input
                  type="date"
                  value={contract.startDate}
                  onChange={(e) => handleContractChange(index, "startDate", e.target.value)}
                />
              </FormControl>

              <FormControl>
                <FormLabel>Finish Date</FormLabel>
                <Input
                  type="date"
                  value={contract.finishDate || ""}
                  onChange={(e) => handleContractChange(index, "finishDate", e.target.value || null)}
                />
              </FormControl>

              <FormControl display="flex" alignItems="center">
                <FormLabel mb="0">Ongoing</FormLabel>
                <Switch
                  isChecked={contract.ongoing}
                  onChange={(e) => handleContractChange(index, "ongoing", e.target.checked)}
                />
              </FormControl>

              <FormControl isRequired>
                <FormLabel>Work Type</FormLabel>
                <Select
                  value={contract.workType}
                  onChange={(e) => handleContractChange(index, "workType", e.target.value)}
                >
                  <option value="FullTime">FullTime</option>
                  <option value="PartTime">PartTime</option>
                </Select>
              </FormControl>

              <FormControl>
                <FormLabel>Hours Per Week</FormLabel>
                <NumberInput
                  min={0}
                  max={100}
                  value={contract.hoursPerWeek ?? ""}
                  onChange={(_, num) => handleContractChange(index, "hoursPerWeek", num)}
                >
                  <NumberInputField />
                </NumberInput>
              </FormControl>
            </Stack>
          </Box>
        ))}

        <Button onClick={handleSubmit} colorScheme="green">
          Submit
        </Button>


        <Button onClick={handleCancel} colorScheme="blue" mt={2}>
          Cancel
        </Button>
        
      </VStack>
    </Box>
  );
};
