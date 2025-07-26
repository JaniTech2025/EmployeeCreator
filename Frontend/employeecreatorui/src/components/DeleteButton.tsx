import { Button, ButtonProps } from "@chakra-ui/react";


interface DeleteButtonProps {
  empid: number;
  onDelete: (empid: number) => void;
}

const DeleteButton: React.FC<DeleteButtonProps> = ({ empid, onDelete }) => {
  return (
    <Button
      bg="red.500"
      color="white"
      _hover={{ bg: "red.600" }}
      mx={4}
      mb={2}
      onClick={() => onDelete(empid)} 
    >
      Delete
    </Button>
  );
};

export default DeleteButton;