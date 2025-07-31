import { useContext, useState } from "react";
import {
  Box,
  Image,
  Text,
  Stack,
  VStack,
  Heading,
  Divider,
  HStack,
  Grid,
  useToast,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalCloseButton,
  ModalBody,
} from "@chakra-ui/react";

import Pagination from "../components/Pagination";
import { ChevronRightIcon, ChevronDownIcon } from "@chakra-ui/icons";
import { EmployeeContext } from "../context/EmployeeContext";
import UpdateButton from "../components/UpdateButton";
import DeleteButton from "../components/DeleteButton";
import DropDown from "./DropDown";
import EmployeeUpdateForm from "./EmployeeUpdateForm";
import { EmployeeGetDTO } from "../types/Employee";


const ITEMS_PER_PAGE = 4;

export const ViewEmployees = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);  
  const [selectedEmployee, setSelectedEmployee] = useState<EmployeeGetDTO | null>(null);
  const toast = useToast();

  const [currentPage, setCurrentPage] = useState(1);
  const context = useContext(EmployeeContext);

  if (!context) throw new Error("ViewEmployees must be used within an EmployeeProvider");

  const { employees, refreshEmployees, deleteEmployee } = context;

  const indexOfLast = currentPage * ITEMS_PER_PAGE;
  const indexOfFirst = indexOfLast - ITEMS_PER_PAGE;
  const currentEmployees = employees.slice(indexOfFirst, indexOfLast);
  const totalPages = Math.ceil(employees.length / ITEMS_PER_PAGE);

  const [expandedId, setExpandedId] = useState<number | null>(null);

  const toggleContract = (id: number) => {
    setExpandedId((prev) => (prev === id ? null : id));
  };




  function handleDelete(empid: number): void {
    // console.log("Inside Parent delete component");

     toast({
      title: 'Success!',
      description: 'employee record with id:' + empid + 'has been deleted',
      status: 'success',
      duration: 3500,
      isClosable: true,
    });


    deleteEmployee(empid).catch(error => {
    console.error("Failed to delete employee:", error);
  })
 }



  // Open modal with the current employee details
  function handleUpdate(empid: number, emp: EmployeeGetDTO): void {
    // console.log("View Employee has received to update", emp)
    setSelectedEmployee(emp);
    setIsModalOpen(true);
    // updateEmployee(empid, emp);
  }

  // function handleCancel(): void {
  //   setIsModalOpen(false);
  // }

  return (
    <Box p={10}>

    <HStack justify="center" mb={20} spacing={10} color="blue.700">
      <Heading as="h2" size="xl" textAlign="center">
        All On Board
      </Heading>
      <DropDown />
    </HStack>

      <VStack spacing={6} align="stretch">
        {currentEmployees.map((emp) => (
          <Box key={emp.id} p={6} w="700px" mx="auto" borderWidth="1px" borderRadius="lg">
            <Stack direction="row" spacing={4}>
              <Image
                boxSize="70px"
                borderRadius="full"
                src={emp.photoUrl}
                alt={`${emp.firstName} ${emp.lastName}`}
              />

              <Box flex="1">
                <Heading fontSize="lg" mb={2}>
                  {emp.firstName} {emp.middleName} {emp.lastName}
                </Heading>
                <Text>Address: {emp.residentialAddress}</Text>
                <Text>Email: {emp.email}</Text>
                <Text>Mobile: {emp.mobileNumber}</Text>


                <UpdateButton 
                  onUpdate={() => handleUpdate(emp.id, emp)}
                />

                <DeleteButton 
                  empid={emp.id} 
                  onDelete={handleDelete} 
                />


                {emp.contracts && emp.contracts.length > 0 && (
                  <>
                    <Divider my={3} />
                    <HStack spacing={2} mb={2} cursor="pointer" onClick={() => toggleContract(emp.id)}>
                      {expandedId === emp.id ? (
                        <ChevronDownIcon boxSize={5} />
                      ) : (
                        <ChevronRightIcon boxSize={5} />
                      )}
                      <Text fontWeight="bold">Contract</Text>
                    </HStack>

                    {expandedId === emp.id && (
                      <VStack align="start" spacing={2} mt={2}>
                        {/* List top two contracts only */}
                        {emp.contracts.slice(0,2).map((contract, index) => (
                          <Box
                            key={contract.id || index}
                            pl={4}
                            py={3}
                            borderLeft="2px solid #3182ce"
                            bg="gray.50"
                            borderRadius="md"
                            w="100%"
                          >
                            <Grid templateColumns="150px 1fr" rowGap={2} columnGap={4}>
                              <Text fontWeight="semibold">Type:</Text>
                              <Text>{contract.contractType}</Text>

                              {/* <Text fontWeight="semibold">Term:</Text>
                              <Text>{contract.contract_term || "N/A"}</Text> */}

                              <Text fontWeight="semibold">Start:</Text>
                              <Text>{contract.startDate}</Text>

                              <Text fontWeight="semibold">
                                {contract.ongoing ? "Ongoing:" : "Finish:"}
                              </Text>
                              <Text>{contract.ongoing ? "Yes" : contract.finishDate || "N/A"}</Text>

                              <Text fontWeight="semibold">Work Type:</Text>
                              <Text>{contract.workType}</Text>

                              <Text fontWeight="semibold">Hours/Week:</Text>
                              <Text>{contract.hoursPerWeek}</Text>
                            </Grid>
                          </Box>
                        ))}
                      </VStack>
                    )}
                  </>
                )}
              </Box>
            </Stack>
          </Box>
        ))}
      </VStack>
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} size="xl" scrollBehavior="inside">
            <ModalOverlay />
            <ModalContent>
              {/* <ModalHeader>Update Employee</ModalHeader> */}
              <ModalCloseButton />
              <ModalBody>
                {selectedEmployee && (
                  <EmployeeUpdateForm
                    employee={selectedEmployee}
                    onUpdate={(updatedEmp) => {
                      setIsModalOpen(false);
                      setSelectedEmployee(null);
                      refreshEmployees();
                      toast({
                        title: "Employee Updated",
                        status: "success",
                        duration: 3000,
                        isClosable: true,
                      });
                    }}
                    setisModalOpen={setIsModalOpen}
                  />
                )}


              </ModalBody>
              {/* <ModalFooter>
                <Button 
                  bg="teal.400"
                  color="white"
                  _hover={{ bg: "teal.600" }}
                  mx={4}
                  mb={2}
                  onClick={() => setIsModalOpen(false)}>
                    Cancel
                </Button>
              </ModalFooter> */}
            </ModalContent>
          </Modal>

    </Box>
  );
};
