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

export const australianAddressRegex =
  /^(?:(?:Unit|Suite|Apt\.?)\s*\d+[A-Za-z]?(?:\/\d+)?\s*)?\d+\s+[A-Za-z\s]+(?:\s+(?:St|Street|Rd|Road|Ave|Avenue|Blvd|Boulevard|Dr|Drive|Pl|Place|Ct|Court|Cres|Crescent|Tce|Terrace))?,\s*[A-Za-z\s]+,\s*(NSW|VIC|QLD|WA|SA|TAS|ACT|NT)\s+\d{4}$/i
  
  
export const residentialAddressSchema = z
  .string()
  .trim()
  .regex(
    australianAddressRegex,
    "Address must be in the format: '123 Street Name, Suburb, NSW 2000'"
  );

export const mobileNumberRegex = /^\+61[2-478]\d{8}$/;

export const mobileNumberSchema = z
  .string()
  .trim()
  .transform((val) => {
    let cleaned = val.replace(/[^\d+]/g, "");

    if (cleaned.startsWith("04") && cleaned.length === 10) {
      return `+61${cleaned.slice(1)}`;
    }

    if (cleaned.startsWith("61") && cleaned.length === 11) {
      return `+${cleaned}`;
    }

    return cleaned.replace(/\s+/g, "");
  })
   .refine((val) => mobileNumberRegex.test(val), {
    message: "Mobile number must be a valid Australian number starting with +61",
  });;


export const employeeSchema = z.object({
    firstName: z.string().min(1, "First name is required"),
    middleName: z.string().trim().optional().transform(val => val === undefined ? "" : val),
    lastName: z.string().min(1, "Last name is required"),
    email: z.email("Invalid email address"),
    mobileNumber: mobileNumberSchema,
    residentialAddress: addressSchema,
    photoUrl: z.url("Invalid photo URL").optional(),
    contracts: z.array(getContractSchema()).optional(),
});


export const employeeUpdateSchema = z.object({
    firstName: z.string().min(1, "First name is required"),
    // middleName: z.string().trim().optional().transform(val => val === "" ? "" : val), 
    middleName: z.string()
    .trim()
    .optional()
    .nullable()
    .transform(val => (!val || val === "" ? "" : val)),
    // middleName: z.string().trim().optional().transform(val => val === "" ? undefined : val),
    lastName: z.string().min(1, "Last name is required"),
    email: z.email("Invalid email address"),
    photoUrl: z.url("Invalid photo URL").optional(),    
    mobileNumber: mobileNumberSchema,
});