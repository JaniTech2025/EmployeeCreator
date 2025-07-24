import * as React from "react";
import { api } from "../services/api";
import { EmployeeCreateDTO, 
         EmployeeGetDTO, 
         EmployeeResponseDTO, 
         } from "../types/Employee";


export const EmployeeContext = React.createContext<{
  employees: EmployeeGetDTO[];
  setEmployees: React.Dispatch<React.SetStateAction<EmployeeGetDTO[]>>;
  refreshEmployees: () => Promise<void>;
  deleteEmployee: (empid: number) => Promise<void>;
  createNewEmployee: (emp: EmployeeCreateDTO) => Promise<void>;
} | null>(null);

const EmployeeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [employees, setEmployees] = React.useState<EmployeeGetDTO[]>([]);

  const refreshEmployees = async () => {
    try {
      const res = await api.get<EmployeeGetDTO[]>("/employees"); 
      setEmployees(res.data);
    } catch (error) {
      console.error("Failed to fetch employees:", error);
    }
  };

 const createNewEmployee = async(employeedata: EmployeeCreateDTO) => {
    try{
      const now = new Date().toISOString();
      const updatedEmployeeData: EmployeeCreateDTO = {
                            ...employeedata,
                            createdAt: now,
                            updatedAt: now,
                            contracts: employeedata.contracts?.map(contract => ({
                            ...contract,
                            createdAt: now,  
                            updatedAt: now
                            })) ?? []
                           };
      const res = await api.post<EmployeeResponseDTO[]>("/employees", updatedEmployeeData);
      await refreshEmployees();
      console.log("Employee created:", res.data);
    } catch(error){
      console.log("Unable to create employee:", error)
    }
  };
  
  const deleteEmployee = async (empid: number) => {
    console.log("inside delete context");
    try {
      const res = await api.delete(`/employees/${empid}`);
      await refreshEmployees();
    } catch (error) {
      console.error("Failed to delete employee:", error);
    }
  };


  React.useEffect(() => {
    refreshEmployees();
  }, []);

  return (
    <EmployeeContext.Provider value={{ employees, setEmployees, refreshEmployees, deleteEmployee, createNewEmployee }}>
      {children}
    </EmployeeContext.Provider>
  );
};

export default EmployeeProvider;
