import * as React from "react";
import { api } from "../services/api";
import { EmployeeCreateDTO, 
         EmployeeGetDTO, 
         } from "../types/Employee";
import { ContractCreateDTO, ContractViewDTO } from "../types/Contract";


interface EmptyReport{
  id: 0;
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  mobileNumber: string;
  residentialAddress: string;
  employeeStatus: string;
  createdAt: string;
  updatedAt: string;
  photoUrl: string;
  contracts: any[],
};

const getEmptyReport = (): EmptyReport => {
  return {
    id: 0,
    firstName: '',
    middleName: '',
    lastName: '',
    email: '',
    mobileNumber: '',
    residentialAddress: '',
    employeeStatus: '',
    createdAt: '',
    updatedAt: '',
    photoUrl: '',
    contracts: [],

  };
};


export const EmployeeContext = React.createContext<{
  employees: EmployeeGetDTO[];
  setEmployees: React.Dispatch<React.SetStateAction<EmployeeGetDTO[]>>;
  refreshEmployees: () => Promise<void>;
  deleteEmployee: (empid: number) => Promise<void>;
  updateEmployee: (empid: number, emp: EmployeeGetDTO) => Promise<void>;
  createNewEmployee: (emp: EmployeeCreateDTO) => Promise<void>;
  createContract: ( empid: number, contract: ContractCreateDTO) => Promise<void>;
  updateContract: ( contractid: number, contract: ContractViewDTO) => Promise<void>;
  createReport: () => Promise<EmployeeGetDTO[]>;
  getEmployeeById: (id: number) => Promise<EmployeeGetDTO | undefined>;
  refreshRecentContract: (id: number) => Promise<ContractViewDTO | undefined>;
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
      console.log(employeedata.contracts);
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
      console.log("Sending", updatedEmployeeData);
      const res = await api.post<EmployeeGetDTO[]>("/employees", updatedEmployeeData);
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

  const updateEmployee = async (empid: number, employeedata: EmployeeGetDTO) => {
    try {
      const res = await api.put(`/employees/${empid}`, employeedata);
      await refreshEmployees();
    } catch (error) {
      console.error("Unable to update employee:", error);
    }
  };

  const createReport = async (): Promise<EmployeeGetDTO[]>  => {
    try {
      const response = await api.get(`/employees`);
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error("Unable to generate report:", error);
      return [getEmptyReport()];
    }
  };

  const updateContract = async (contractid:number, contractData: ContractViewDTO) => {
    try{
      const response = await api.put(`/contracts/${contractid}`, contractData);
      console.log(response.data);
      return response.data;
    }
    catch (error) {
      console.error("Unable to create contract:", error);
      throw error;
    }
  }

    const createContract = async (empid: number, contractdata: ContractCreateDTO) => {
    try{
      console.log(contractdata);
      const res = await api.post(`/employees/${empid}/contracts`, contractdata);
      await refreshEmployees();
      return res.data;      
    } catch(error){
      console.log("Unable to create a new contract for employee: ", empid);
      throw error;
    }
  }

  const getEmployeeById = async (id: number): Promise<EmployeeGetDTO | undefined> => {
    try{
       const response = await api.get(`/api/employees/${id}`);
      return response.data;
    } catch (error) {
      console.error("Unable to fetch eployees", error);
      return undefined;
    }
  };

  
  const refreshRecentContract = async (id: number): Promise<ContractViewDTO | undefined> => {
    try {
      const response = await api.get(`/api/employees/${id}/contracts`);
      const contracts: ContractViewDTO[] = response.data;

      if (contracts && contracts.length > 0) {
        return contracts[0];  
      }
    } catch (error) {
      console.error("Unable to fetch first contract", error);
    }
    return undefined;
  };








  React.useEffect(() => {
    refreshEmployees();
  }, []);

  return (
    <EmployeeContext.Provider value={{ employees, setEmployees, refreshEmployees, 
                                        deleteEmployee, updateEmployee, createNewEmployee,
                                        createContract, updateContract, createReport, 
                                        getEmployeeById, refreshRecentContract  }}>
      {children}
    </EmployeeContext.Provider>
  );
};

export default EmployeeProvider;
