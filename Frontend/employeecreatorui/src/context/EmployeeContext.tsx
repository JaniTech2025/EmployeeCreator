import * as React from "react";
import { api } from "../services/api";
import { EmployeeGetDTO } from "../types/Employee";

export const EmployeeContext = React.createContext<{
  employees: EmployeeGetDTO[];
  setEmployees: React.Dispatch<React.SetStateAction<EmployeeGetDTO[]>>;
  refreshEmployees: () => Promise<void>;
} | null>(null);

const EmployeeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [employees, setEmployees] = React.useState<EmployeeGetDTO[]>([]);

  const refreshEmployees = async () => {
    try {
      const res = await api.get<EmployeeGetDTO[]>("/employees"); // <- expect array directly
      setEmployees(res.data);
    } catch (error) {
      console.error("Failed to fetch employees:", error);
    }
  };

  React.useEffect(() => {
    refreshEmployees();
  }, []);

  return (
    <EmployeeContext.Provider value={{ employees, setEmployees, refreshEmployees }}>
      {children}
    </EmployeeContext.Provider>
  );
};

export default EmployeeProvider;
