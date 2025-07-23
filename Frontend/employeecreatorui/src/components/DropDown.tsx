import { useState } from 'react';
import {
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  IconButton,
} from '@chakra-ui/react';
import { FiMoreVertical } from 'react-icons/fi';
import EmpModal from './EmpModal';
import { CreateEmployee } from './CreateEmployee';
const DropDown = () => {
  const [isCreateOpen, setIsCreateOpen] = useState(false);  

  const handleClick = () => {
    setIsCreateOpen(true);
  }

  const handleClose = () => {
    setIsCreateOpen(false);
  }

  return (
    <>
    <Menu>
      <MenuButton
        as={IconButton}
        icon={<FiMoreVertical
              style={{ transform: "scaleY(1.5)" }}
              size={27}
            />}
        variant="ghost"
        aria-label="Options"
      />
      <MenuList>
        <MenuItem fontSize="lg" color="blue.800" fontWeight="bold" onClick={handleClick}>Add an Employee</MenuItem>
      </MenuList>
    </Menu>

    {isCreateOpen && (  <EmpModal isOpen={true} onClose={handleClose}>
        <CreateEmployee onClose={handleClose}/>
      </EmpModal>)}

    </>
  );
};

export default DropDown;