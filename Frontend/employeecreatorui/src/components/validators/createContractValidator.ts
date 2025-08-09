import { z } from "zod";

export const getContractSchema = (prevFinishDate: string | null)  =>
  z.object({
    startDate: z.string()
      .min(1, "Start date is required")
      .refine(dateStr => !isNaN(Date.parse(dateStr)), {
        message: "Start date must be a valid date",
      })
      .refine(startDate => {
        if (!prevFinishDate) return true;
        return new Date(startDate) > new Date(prevFinishDate);
      }, {
        message: "Start date must be after previous contract's finish date",
      }),

    finishDate: z.string()
      .optional()
      .nullable()
      .refine(val => !val || !isNaN(Date.parse(val)), {
        message: "Finish date must be a valid date or empty",
      }),

    hoursPerWeek: z.coerce.number()
      .min(18, "Hours per week must be atleast 10")
      .max(40, "Hours per week must be 40 or less"),
  });
