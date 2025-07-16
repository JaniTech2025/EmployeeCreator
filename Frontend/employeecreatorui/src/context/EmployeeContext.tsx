import * as React from "react";
import { api } from "../services/api";
import { EmployeeGetDTO } from "../types/Employee";

export const EmployeeContext = React.createContext<{
  employees: EmployeeGetDTO[];
  setEmployees: React.Dispatch<React.SetStateAction<EmployeeGetDTO[]>>;
  refreshEmployees: () => Promise<void>;
  deleteEmployee: (empid: number) => Promise<void>;
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
    <EmployeeContext.Provider value={{ employees, setEmployees, refreshEmployees, deleteEmployee }}>
      {children}
    </EmployeeContext.Provider>
  );
};

export default EmployeeProvider;
