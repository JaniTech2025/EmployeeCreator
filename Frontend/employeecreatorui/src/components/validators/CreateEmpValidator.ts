import { z } from "zod";

export const contractSchema = z.object({
  startDate: z.string().min(1, "Start date is required").refine(date => !isNaN(Date.parse(date)), {
    message: "Invalid start date",
  }),
  contractType: z.enum(["Permanent", "Temporary"]),
 workType: z.enum(["FullTime", "PartTime"]),
  finishDate: z.string().optional().nullable().refine(date => {
    return !date || !isNaN(Date.parse(date));
  }, { message: "Invalid finish date" }),
  hoursPerWeek: z.number().min(0).max(100).optional()
});

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
    contracts: z.array(contractSchema).optional(),
});

export const contractSchemaWithPrevFinishDate = (prevFinishDate: string | null) =>
  z.object({
    contractType: z.enum(["Temporary", "Permanent"]),
    startDate: z.string().refine((startDate) => {
      if (!prevFinishDate) return true; // no previous contract, so valid
      return new Date(startDate) > new Date(prevFinishDate);
    }, {
      message: "Start date must be after previous contract's finish date",
    }),
    finishDate: z.string().nullable(),
    workType: z.enum(["FullTime", "PartTime"]),
    hoursPerWeek: z.number().min(0).max(168),
    createdAt: z.string(),
    updatedAt: z.string(),
    ongoing: z.boolean(),
  });

