import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import { EmployeeGetDTO } from '../types/Employee';

export const EmployeeReport = (data: EmployeeGetDTO[]) => {
  const flattenedData = data.flatMap((employee) =>
    employee.contracts?.map((contract: any) => ({
      EmployeeID: employee.id,
      FirstName: employee.firstName,
      MiddleName: employee.middleName,
      LastName: employee.lastName,
      Email: employee.email,
      Mobile: employee.mobileNumber,
      Address: employee.residentialAddress,
      Status: employee.employeeStatus,
      EmployeeCreated: employee.createdAt,
      EmployeeUpdated: employee.updatedAt,

      ContractID: contract.id,
      ContractType: contract.contractType,
      StartDate: contract.startDate,
      FinishDate: contract.finishDate ?? '',
      WorkType: contract.workType,
      HoursPerWeek: contract.hoursPerWeek,
      ContractCreated: contract.createdAt,
      ContractUpdated: contract.updatedAt ?? '',
    }))
  );

  const worksheet = XLSX.utils.json_to_sheet(flattenedData);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Employees');

  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });

  const now = new Date();
  const timestamp = now.toISOString().replace(/[:.-]/g, '').slice(0, 15); 
  saveAs(blob, `Employee_Report_${timestamp}.xlsx`);
};