import { z } from "zod";


const baseContractSchema = z.object({
  contractType: z.enum(["Temporary", "Permanent"]).optional().nullable(),
  startDate: z.string()
    .optional()
    .refine(val => val === undefined || val === "" || !isNaN(Date.parse(val)), {
      message: "Start date must be a valid date or empty",
    }),
  finishDate: z.string()
    .optional()
    .nullable()
    .refine(val => !val || !isNaN(Date.parse(val)), {
      message: "Finish date must be a valid date or empty",
    }),
  workType: z.enum(["FullTime", "PartTime"]).optional(),
  hoursPerWeek: z.coerce.number()
    .min(10, "Hours per week must be at least 10")
    .max(40, "Hours per week must be 40 or less")
    .optional(),
  createdAt: z.string().optional(),
  updatedAt: z.string().optional(),
})
.refine((data) => {
  if (!data.finishDate) return true;

  const start = new Date(data.startDate || "");
  const finish = new Date(data.finishDate);

  if (isNaN(start.getTime()) || isNaN(finish.getTime())) return false;

  const oneMonthLater = new Date(start);
  oneMonthLater.setMonth(oneMonthLater.getMonth() + 1);

  return finish >= oneMonthLater;
}, {
  message: "Finish date must be at least one month after start date",
  path: ["finishDate"],
});

export const getContractSchema = () => baseContractSchema;


export const contractSchemaWithPrevFinishDate = (prevFinishDate?: string | null) =>
  prevFinishDate
    ? baseContractSchema.refine(
        (data) => new Date(data.startDate || "") > new Date(prevFinishDate),
        {
          message: "Start date must be after previous contract's finish date",
          path: ["startDate"],
        }
      )
    : baseContractSchema;

