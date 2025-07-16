import { Button, ButtonProps } from "@chakra-ui/react";

interface DeleteButtonProps {
  empid: number;
  onDelete: (empid: number) => void;
}

const DeleteButton: React.FC<DeleteButtonProps> = ({ empid, onDelete }) => {
  console.log("Parent has passed Delete click event to Delete component", empid)
  return (
    <Button
      bg="#007fff"
      color="white"
      _hover={{ bg: "#0066cc" }}
      mx={4}
      mb={2}
      onClick={() => onDelete(empid)} 
    >
      Delete
    </Button>
  );
};

export default DeleteButton;