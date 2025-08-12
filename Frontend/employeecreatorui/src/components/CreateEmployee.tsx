import React, { ChangeEvent, useContext, useState } from "react";
import { EmployeeCreateDTO } from "../types/Employee";
import { ContractCreateDTO } from "../types/Contract";
import { EmployeeContext } from "../context/EmployeeContext";
import { ImagePicker }  from "./ImagePicker";
import { ZodError } from 'zod';
import { employeeSchema, addressSchema } from "./validators/EmployeeSchema";

import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Image,
  Stack,
  Text,
  NumberInput,
  NumberInputField,
  Select,
  Divider,
  VStack,
  useToast,
  Accordion,
  AccordionPanel,
  AccordionItem,
  AccordionButton,
  AccordionIcon,
  HStack,
  Tooltip,
  IconButton
} from '@chakra-ui/react';

import { RepeatIcon } from '@chakra-ui/icons'; 


const defaultContract: ContractCreateDTO = {
  contractType: "Permanent",
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
    const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});    



  const context = useContext(EmployeeContext);
    if (!context) {
    throw new Error('CreateEmployee must be used within an EmployeeProvider');
  }


  const { createNewEmployee } = context;


    const resetContractDetails = () => {
    setEmployee(prev => ({
      ...prev,
      contracts: [defaultContract],
    }));
  };


  const [employee, setEmployee] = useState<EmployeeCreateDTO>({
    firstName: "",
    middleName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    // residentialAddress: "",
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
      updatedContract.contractType = currentContract.contractType || "Permanent";
    }

    if(field === "startDate"){
      console.log(value);
    }


    setEmployee(prev => ({
      ...prev,
      contracts: [updatedContract],
      // contracts: (updatedContract.startDate === "")? [updatedContract] : [],
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

    employeeSchema.omit({ residentialAddress: true }).parse(employee);

    addressSchema.parse(address);


    setValidationErrors({});

    const fullAddress = `${address.streetAddress}, ${address.suburb}, ${address.state} ${address.postcode}`;


      const employeeToSend = {
        ...employee,
        residentialAddress: fullAddress,
        contracts: employee.contracts && employee.contracts.length > 0 && employee.contracts[0].startDate.trim() !== ""
          ? employee.contracts
          : []
      };      
      await createNewEmployee(employeeToSend);
      console.log("Submitted:", employeeToSend);

      toast({
        title: 'Success!',
        description: 'successfully created a new employee',
        status: 'success',
        duration: 3500,
        isClosable: true,
      });
      onClose();  

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
      console.error("Failed to create employee", err);
    }
    }

  };

function handleAddressChange(event: ChangeEvent<HTMLInputElement | HTMLSelectElement>): void {
  const { name, value } = event.target;

  setAddress((prev) => {
    const updated = { ...prev, [name]: value };

    const fullAddress = `${updated.streetAddress}, ${updated.suburb}, ${updated.state} ${updated.postcode}`;

    setEmployee((emp) => ({
      ...emp,
      residentialAddress: fullAddress,
    }));

    return updated;
  });
}


  const employeeDetailsComplete =  Boolean(
  employee.firstName.trim() &&
  employee.email?.trim() &&
  employee.lastName.trim() &&
  employee.photoUrl?.trim() &&
  employee.residentialAddress?.trim() &&
  employee.mobileNumber.trim());
  return (
        <Box maxW="800px" mx="auto" p={1}>
          <Text fontSize="2xl" mb={4}>Create Employee</Text>
          <Divider mb={4} borderColor="gray.500" />

          <VStack spacing={4} align="stretch">
            <HStack spacing={4} align="start">
              <FormControl isRequired flex={1}>
                <FormLabel>First Name</FormLabel>
                <Input name="firstName" value={employee.firstName} onChange={handleChange} />
                  {validationErrors["firstName"] && (
                  <Text color="red.500" fontSize="sm">
                    {validationErrors["firstName"]}
                  </Text>
                )}
              </FormControl>

              <FormControl flex={1}>
                <FormLabel>Middle Name</FormLabel>
                <Input name="middleName" value={employee.middleName} onChange={handleChange} />
              </FormControl>

              <FormControl isRequired flex={1}>
                <FormLabel>Last Name</FormLabel>
                <Input name="lastName" value={employee.lastName} onChange={handleChange} />
                {validationErrors["lastName"] && (              
                 <Text color="red.500" fontSize="sm">
                    {validationErrors["lastName"]}
                  </Text>
                )}
              </FormControl>
            </HStack>


            <HStack spacing={4} align="start">

            <FormControl  isRequired mb={4}>
              <FormLabel>Email</FormLabel>
              <Input name="email" type="email" value={employee.email} onChange={handleChange} />
             {validationErrors["email"] && (
              <Text color="red.500" fontSize="sm">
                  {validationErrors["email"]}
              </Text>  
             )}
            </FormControl>

            <FormControl isRequired mb={4}>
              <FormLabel>Mobile Number</FormLabel>
              <Input name="mobileNumber" value={employee.mobileNumber} onChange={handleChange} />
              {validationErrors["mobileNumber"] && (
              <Text color="red.500" fontSize="sm">
                  {validationErrors["mobileNumber"]}
              </Text>  
              )}
            </FormControl>

            </HStack>

            {/* <FormControl>
              <FormLabel>Residential Address</FormLabel>
              <Input name="residentialAddress" value={employee.residentialAddress} onChange={handleChange} />
            </FormControl> */}
          <FormControl isRequired mb={4}>
            <FormLabel>Street Address</FormLabel>
            <Input
              name="streetAddress"
              value={address.streetAddress}    
              onChange={handleAddressChange}
            />
            {validationErrors["streetAddress"] && (
              <Text color="red.500" fontSize="sm">
                {validationErrors["streetAddress"]}
              </Text>
            )}
          </FormControl>

            
        <FormControl isRequired mb={4}>
          <FormLabel>Suburb/Town</FormLabel>
          <Input name="suburb" value={address.suburb} onChange={handleAddressChange}/>
          {validationErrors["suburb"] && (
            <Text color="red.500" fontSize="sm">
              {validationErrors["suburb"]}
            </Text>
          )}
        </FormControl>


            <HStack spacing={4} align="start">
            <FormControl isRequired mb={4}>
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
            {validationErrors.state && <Text color="red.500">{validationErrors.state}</Text>}
            </FormControl>

         <FormControl isRequired mb={4}>
            <FormLabel>Postcode</FormLabel>
            <Input
              type="text"
              placeholder="3000"
              name="postcode"
              maxLength={4}
              value={address.postcode}
              onChange={handleAddressChange}
            />
            {validationErrors.postcode && (
              <Text color="red.500" fontSize="sm">
                {validationErrors.postcode}
              </Text>
            )}
          </FormControl>
            </HStack>


            {/* <FormControl>
              <FormLabel>Employee Status</FormLabel>
              <Select name="employeeStatus" value={employee.employeeStatus} onChange={handleChange}>
                <option value="Active">Active</option>
                <option value="Inactive">Inactive</option>
              </Select>
            </FormControl> */}

            {/* <FormControl>
              <FormLabel>Photo URL</FormLabel>
              <Input name="photoUrl" value={employee.photoUrl} onChange={handleChange} />
            </FormControl> */}
{/* 
            <FormControl>
              <FormLabel>Photo URL</FormLabel>
            <Input
                type="file"
                name="photoUrl"
                accept="image/*"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  if (file) {
                    // Create a local preview URL for the image
                    const previewUrl = URL.createObjectURL(file);
                    setEmployee((prev) => ({ ...prev, photoUrl: "https://randomuser.me/photos" }));
                  }
                }}
              />

              {employee.photoUrl && (
                <Box mt={2}>
                  <Image
                    src={employee.photoUrl}
                    alt="Photo Preview"
                    boxSize="100px"
                    objectFit="cover"
                    borderRadius="md"
                    fallbackSrc="./default_profile.png"
                  />
                </Box>
              )}
            </FormControl> */}

            <FormControl isRequired>
              <FormLabel>Photo URL</FormLabel>
               <ImagePicker onSelect={(url) => setEmployee((prev) => ({ ...prev, photoUrl: url }))} />

              {employee.photoUrl && (
                <Box mt={2}>
                  <Image
                    src={employee.photoUrl}
                    alt="Selected Photo"
                    boxSize="100px"
                    objectFit="cover"
                    borderRadius="md"
                    fallbackSrc="/default_profile.png"
                    onError={() => {
                      setEmployee((prev) => ({
                        ...prev,
                        photoUrl: "./default_profile.png",
                      }));
                    }}
                  />
                </Box>
              )}

            </FormControl>

            {employee.contracts && employee.contracts.length > 0 && (
         

              <Accordion allowToggle>
                <AccordionItem
                  border="1px solid"
                  borderColor="gray.200"
                  borderRadius="md"
                  overflow="hidden"
                  isDisabled={!employeeDetailsComplete}
                  mb={4}
                >
                <h2 style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                  <AccordionButton
                    bg="white"
                    _expanded={{ bg: "gray.100" }}
                    borderRadius="md"
                    p={4}
                    flex="1"
                    style={{ textAlign: 'left', fontWeight: 'bold' }}
                  >
                    Add Contract Details (Optional)
                    <AccordionIcon />
                  </AccordionButton>

                  <Tooltip label="Reset Contract Details" aria-label="Reset Contract Details Tooltip">
                    <IconButton
                      aria-label="Reset Contract Details"
                      icon={<RepeatIcon />}
                      size="sm"
                      ml={2}
                      onClick={resetContractDetails}
                    />
                  </Tooltip>
                </h2>

                  <AccordionPanel bg="white" p={4}>
                    <Stack spacing={3}>
                      <FormControl isRequired>
                        <FormLabel>Contract Type</FormLabel>
                        <Select
                          value={employee.contracts[0].contractType || "Permanent"}
                          onChange={(e) => handleContractChange("contractType", e.target.value)}
                          placeholder="Select contract type"
                        >
                          <option value="Permanent">Permanent</option>
                          <option value="Temporary">Temporary</option>
                        </Select>
                      </FormControl>

                      {/* <FormControl>
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
                        {validationErrors["contracts.0.finishDate"] && (
                          <Text color="red.500" fontSize="sm">
                            {validationErrors["contracts.0.finishDate"]}
                          </Text>
                        )}                        
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
                          onChange={(_, num) => handleContractChange("hoursPerWeek", num)}
                        >
                          <NumberInputField />
                        </NumberInput>
                        {validationErrors["contracts.0.hoursPerWeek"] && (
                          <Text color="red.500" fontSize="sm">
                            {validationErrors["contracts.0.hoursPerWeek"]}
                          </Text>
                        )}                        
                      </FormControl>
                    </Stack>
                  </AccordionPanel>
                </AccordionItem>
              </Accordion>
    )}


      <HStack spacing={2} mt={2} align="right">
        <Button onClick={handleSubmit} colorScheme="green">
          Submit
        </Button>

        <Button onClick={handleCancel} colorScheme="blue">
          Cancel
        </Button>
      </HStack>
        
      </VStack>
    </Box>
  );
};