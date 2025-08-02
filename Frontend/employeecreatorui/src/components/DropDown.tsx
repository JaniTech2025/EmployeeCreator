import { useContext, useState } from 'react';
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
import { EmployeeContext } from '../context/EmployeeContext';
import { EmployeeReport } from './EmployeeReport';
const DropDown = () => {
  const [isCreateOpen, setIsCreateOpen] = useState(false);  
  const context = useContext(EmployeeContext);

  if(!context){
    throw new Error('DropDown must be used within an EmployeeProvider');
  }

  const {createReport} = context;

  const handleClick = () => {
    setIsCreateOpen(true);
  }

  const handleClickReport = async() => {
      const output = await createReport();
       EmployeeReport(output);
      
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
        <MenuItem fontSize="lg" color="blue.800" fontWeight="bold" onClick={handleClickReport}>Generate Report</MenuItem>

      </MenuList>
    </Menu>

    {isCreateOpen && (  <EmpModal isOpen={true} onClose={handleClose}>
        <CreateEmployee onClose={handleClose}/>
      </EmpModal>)}



    </>
  );
};

export default DropDown;