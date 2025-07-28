import React, { ChangeEvent, useContext, useState } from "react";
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
  NumberInput,
  NumberInputField,
  Select,
  Divider,
  VStack,
  useToast
} from '@chakra-ui/react';


const defaultContract: ContractCreateDTO = {
  contractType: "",
  startDate: "",
  finishDate: null,
  workType: "FullTime",
  hoursPerWeek: 10,
  ongoing: false
};



interface CreateEmployeeProps {
  onClose: () => void;
}

export const CreateEmployee: React.FC<CreateEmployeeProps> = ({onClose}) => {

    const toast = useToast();



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

  const [address, setAddress] = useState({ streetAddress: "",
                                            suburb: "",
                                            state:"",
                                            postcode: ""});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setEmployee((prev) => ({ ...prev, [name]: value }));
  };

  const handleContractChange = (
    field: keyof ContractCreateDTO,
    value: string | boolean | number | null
    ) => {
     const currentContract = employee.contracts && employee.contracts.length > 0
    ? employee.contracts[0]
    : defaultContract;

    let updatedValue = value;

  
    // if (field === "contractType") {
    //   if (!value || value === "") {
    //     updatedValue = "Permanent";
    //   }
    //   console.log("Field value" + field +  value);
    // }

    const updatedContract = {
      ...currentContract,
      [field]: updatedValue,
    };

    if (field === "startDate" || field === "finishDate") {
      const startDate = field === "startDate" ? (value as string) : currentContract.startDate;
      const finishDate = field === "finishDate" ? (value as string) : currentContract.finishDate;

      updatedContract.contractType = currentContract.contractType || "Permanent";
    }

    setEmployee(prev => ({
      ...prev,
      contracts: [updatedContract],
    }));

    console.log(updatedContract)
  };

  // const addContract = () => {
  //   setEmployee((prev) => ({
  //     ...prev,
  //     contracts: [...(prev.contracts || []), defaultContract],
  //   }));
  // };

   const handleCancel = () => {
      onClose();
   };


  const handleSubmit = async() => {
    try {
      console.log("Submitted:", employee);
      await createNewEmployee(employee);
      toast({
        title: 'Success!',
        description: 'successfully created a new employee',
        status: 'success',
        duration: 3500,
        isClosable: true,
      });
      onClose();  

    } catch (err) {
      console.error("Failed to create employee", err);
    }

  };

  function handleAddressChange(event: ChangeEvent<HTMLInputElement | HTMLSelectElement>): void {
    const {name, value} = event.target;

    setAddress((prev) => {
      const updated = { ...prev, [name]: value };

      const fullAddress = `${updated.streetAddress}, ${updated.suburb} ${updated.state} ${updated.postcode}`;

      setEmployee((emp) => ({
        ...emp,
        residentialAddress: fullAddress,
      }));

      return updated;
    });
      
  }

  return (
        <Box maxW="800px" mx="auto" p={6}>
          <Text fontSize="2xl" mb={4}>Create Employee</Text>
          <Divider mb={4} borderColor="gray.500" />

          <VStack spacing={4} align="stretch">
            <FormControl isRequired>
              <FormLabel>First Name</FormLabel>
              <Input name="firstName" value={employee.firstName} onChange={handleChange} />
            </FormControl>

            <FormControl mb={4}>
              <FormLabel>Middle Name</FormLabel>
              <Input name="middleName" value={employee.middleName} onChange={handleChange} />
            </FormControl>

            <FormControl isRequired  mb={4}>
              <FormLabel>Last Name</FormLabel>
              <Input name="lastName" value={employee.lastName} onChange={handleChange} />
            </FormControl>

            <FormControl  mb={4}>
              <FormLabel>Email</FormLabel>
              <Input name="email" type="email" value={employee.email} onChange={handleChange} />
            </FormControl>

            <FormControl isRequired mb={4}>
              <FormLabel>Mobile Number</FormLabel>
              <Input name="mobileNumber" value={employee.mobileNumber} onChange={handleChange} />
            </FormControl>

            {/* <FormControl>
              <FormLabel>Residential Address</FormLabel>
              <Input name="residentialAddress" value={employee.residentialAddress} onChange={handleChange} />
            </FormControl> */}

            <FormControl mb={4}>
              <FormLabel>Street Address</FormLabel>
              <Input name="streetAddress" value={address.streetAddress} onChange={handleAddressChange}/>
            </FormControl>

            
            <FormControl mb={4}>
              <FormLabel>Suburb/Town</FormLabel>
              <Input name="suburb" value={address.suburb} onChange={handleAddressChange}/>
            </FormControl>

            <FormControl mb={4}>
              <FormLabel>State/Territory</FormLabel>
              {/* <Input name="state" value={address.state} onChange={handleAddressChange}/> */}
                  <Select name="state" placeholder="Select state or territory" onChange={handleAddressChange}>
                  <option value="NSW">New South Wales</option>
                  <option value="VIC">Victoria</option>
                  <option value="QLD">Queensland</option>
                  <option value="WA">Western Australia</option>
                  <option value="SA">South Australia</option>
                  <option value="TAS">Tasmania</option>
                  <option value="ACT">Australian Capital Territory</option>
                  <option value="NT">Northern Territory</option>
            </Select>
            </FormControl>

            <FormControl mb={4}>
            <FormLabel>Postcode</FormLabel>
              <Input type="text" placeholder="3000" name="postcode" maxLength={4} value={address.postcode} onChange={handleAddressChange}/>
            </FormControl>


            {/* <FormControl>
              <FormLabel>Employee Status</FormLabel>
              <Select name="employeeStatus" value={employee.employeeStatus} onChange={handleChange}>
                <option value="Active">Active</option>
                <option value="Inactive">Inactive</option>
              </Select>
            </FormControl> */}

            <FormControl>
              <FormLabel>Photo URL</FormLabel>
              <Input name="photoUrl" value={employee.photoUrl} onChange={handleChange} />
            </FormControl>

            {employee.contracts && employee.contracts.length > 0 && (
              <Box p={4} border="1px solid #ccc" borderRadius="md">
                <Text fontWeight="bold" mb={5}>Contract Details</Text>
                <Divider mb={4} borderColor="gray.500" />

                <Stack spacing={3}>
                <FormControl isRequired>
                    <FormLabel>Contract Type</FormLabel>
                    <Select
                      value={employee.contracts[0].contractType || "Permanent"}                      
                      onChange={(e) => handleContractChange("contractType", e.target.value)}
                      placeholder="Enter contract type"
                    >
                      <option value="Permanent">Permanent</option>
                      <option value="Temporary">Temporary</option>
                    </Select>
                  </FormControl>
{/* 
                  <FormControl>
                    <FormLabel>Contract Term</FormLabel>
                    <Input
                      value={employee.contracts[0].contractTerm || ""}
                      onChange={(e) => handleContractChange("contractTerm", e.target.value)}
                      placeholder="Enter approx duration in months/years"
                    />
                  </FormControl> */}

                  <FormControl isRequired>
                    <FormLabel>Start Date</FormLabel>
                    <Input
                      type="date"
                      value={employee.contracts[0].startDate}
                      onChange={(e) => handleContractChange("startDate", e.target.value)}
                    />
                  </FormControl>

                  <FormControl>
                    <FormLabel>Finish Date</FormLabel>
                    <Input
                      type="date"
                      value={employee.contracts[0].finishDate || ""}
                      onChange={(e) =>
                        handleContractChange("finishDate", e.target.value || null)
                      }
                    />
                  </FormControl>

                  {/* Optional: Uncomment if using 'ongoing' */}
                  {/* <FormControl display="flex" alignItems="center">
                    <FormLabel mb="0">Ongoing</FormLabel>
                    <Switch
                      isChecked={employee.contracts[0].ongoing}
                      onChange={(e) =>
                        handleContractChange("ongoing", e.target.checked)
                      }
                    />
                  </FormControl> */}

                  <FormControl isRequired>
                    <FormLabel>Work Type</FormLabel>
                    <Select
                      value={employee.contracts[0].workType}
                      onChange={(e) => handleContractChange("workType", e.target.value)}
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
                      value={employee.contracts[0].hoursPerWeek ?? ""}
                      onChange={(_, num) =>
                        handleContractChange("hoursPerWeek", num)
                      }
                    >
                      <NumberInputField />
                    </NumberInput>
                  </FormControl>
                </Stack>
              </Box>
    )}


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