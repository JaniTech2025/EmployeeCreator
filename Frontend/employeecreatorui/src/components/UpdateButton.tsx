import { Button, ButtonProps } from "@chakra-ui/react";

interface UpdateButtonProps extends ButtonProps {
  onUpdate: () => void;
}

const UpdateButton: React.FC<UpdateButtonProps> = ({ onUpdate }) => {
  return (
    <Button
      bg="teal.400"
      color="white"
      _hover={{ bg: "teal.600" }}
      mx={4}
      mb={2}
    >
      Update
    </Button>
  );
};

export default UpdateButton;