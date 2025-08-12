import { z } from "zod";
import { getContractSchema } from "./ContractSchema";


export const addressSchema = z.object({
  streetAddress: z.string().min(1, "Street address is required"),
  suburb: z.string().min(1, "Suburb is required"),
  state: z.enum([
    "NSW", 
    "VIC",
    "QLD", 
    "WA", 
    "SA", 
    "TAS",
    "ACT",
    "NT"  
  ], "State/Territory is required"),
  postcode: z.string()
    .regex(/^\d{4}$/, "Postcode must be exactly 4 digits")
});


export const employeeSchema = z.object({
    firstName: z.string().min(1, "First name is required"),
    middleName: z.string().optional(),
    lastName: z.string().min(1, "Last name is required"),
    email: z.email("Invalid email address"),
    mobileNumber: z.string().regex(
   /^(61\d{8}|04\d{8})$/,
    "Mobile number must start with 61 or 04 followed by 8 digits"),
    residentialAddress: addressSchema,
    photoUrl: z.url("Invalid photo URL").optional(),
    contracts: z.array(getContractSchema()).optional(),
});