import { ContractCreateDTO, ContractViewDTO } from "./Contract";


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
  createdAt?: string; //format: ISO date string
  updatedAt?: string; // format: ISO date string
  photoUrl?: string;
  contracts?: ContractViewDTO[];
}


