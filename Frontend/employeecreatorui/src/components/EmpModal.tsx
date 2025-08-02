import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  IconButton,
} from '@chakra-ui/react'
import { ReactNode } from "react";
import { FaTimes } from "react-icons/fa";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  children: ReactNode;
}

export default function EmpModal({ isOpen, onClose, children }: ModalProps) {
  return (
    <Modal isOpen={isOpen} onClose={onClose} 
           isCentered size="2xl" 
           closeOnOverlayClick={false}>
      <ModalOverlay backdropFilter='blur(10px) hue-rotate(90deg)'/>
      <ModalContent position="relative" p={4}>
        {/* <CreateEmployee/> */}
        <IconButton
          aria-label="Close modal"
          icon={<FaTimes />}
          onClick={onClose}
          variant="ghost"
          size="sm"
          position="absolute"
          top={2}
          right={2}
          _focus={{ boxShadow: "none" }}
        />
        <ModalBody pt={6}>{children}</ModalBody>
      </ModalContent>
    </Modal>
  );
}
