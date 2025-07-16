import { Button, ButtonProps } from "@chakra-ui/react";

interface UpdateButtonProps extends ButtonProps {
  onUpdate: () => void;
}

const UpdateButton: React.FC<UpdateButtonProps> = ({ onUpdate }) => {
  return (
    <Button
      bg="#007fff"
      color="white"
      _hover={{ bg: "#0066cc" }}
      mx={4}
      mb={2}
    >
      Update
    </Button>
  );
};

export default UpdateButton;