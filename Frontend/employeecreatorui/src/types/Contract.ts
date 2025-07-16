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
  contract_type: string;         
  contract_term?: string;        
  start_date: string;            // format:  ISO date string 
  finish_date?: string | null;   // format: ISO date string or null
  ongoing: boolean;
  work_type: string;             // "FullTime" or "PartTime"
  hours_per_week?: number;        // range: 0.0 and 100.0
  created_at?: string;           // format: ISO datetime string
  updated_at?: string;           // format: ISO datetime string
}