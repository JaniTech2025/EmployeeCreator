export interface ContractCreateDTO {
  contractType: string;         
  contractTerm?: string;        
  startDate: string;            // format:  ISO date string 
  finishDate?: string | null;   // format: ISO date string or null
  ongoing: boolean;
  workType: string;             // "FullTime" or "PartTime"
  hoursPerWeek?: number;        // range: 0.0 and 100.0
  createdAt?: string;           // format: ISO datetime string
  updatedAt?: string;           // format: ISO datetime string
}


export interface ContractViewDTO {
  id: number;
  contractType: string;         
  // contract_term?: string;        
  startDate: string;            // format:  ISO date string 
  finishDate?: string | null;   // format: ISO date string or null
  ongoing: boolean;
  workType: string;             // "FullTime" or "PartTime"
  hoursPerWeek?: number;        // range: 0.0 and 100.0
  createdAt?: string;           // format: ISO datetime string
  updatedAt?: string;           // format: ISO datetime string
}


export interface ContractResponseDTO {
  id: number;
  contractType: string;
  startDate: string;        
  finishDate: string | null;
  workType: string;
  hoursPerWeek: number;
  createdAt: string;        
  updatedAt: string | null;
}