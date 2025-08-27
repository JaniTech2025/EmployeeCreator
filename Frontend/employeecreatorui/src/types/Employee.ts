import { ContractCreateDTO, ContractViewDTO, ContractResponseDTO } from "./Contract";



export interface EmployeeCreateDTO {
  firstName: string;
  middleName?: string;
  lastName: string;
  email?: string;
  mobileNumber: string;
  residentialAddress?: string;
  employeeStatus?: string;
  createdAt?: string; //format: ISO date string
  updatedAt?: string; // format: ISO date string
  photoUrl?: string;
  contracts?: ContractCreateDTO[];
  ongoing?: boolean;
}


export interface EmployeeGetDTO {
  id: number;
  firstName: string;
  middleName?: string;
  lastName: string;
  email?: string;
  mobileNumber: string;
  residentialAddress?: string;
  employeeStatus?: string;
  createdAt?: string; 
  updatedAt?: string; 
  photoUrl?: string;
  contracts?: ContractViewDTO[];
  ongoing?: boolean;
}

export interface EmployeeResponseDTO {
  id: number;
  firstName: string;
  middleName: string | null;
  lastName: string;
  email: string;
  mobileNumber: string;
  residentialAddress: string;
  employeeStatus: string;
  createdAt: string; 
  updatedAt: string; 
  photoUrl: string;
  contracts: ContractResponseDTO[];
}
