INSERT INTO employee_details 
(first_name, middle_name, last_name, email, mobile_number, photo_url, residential_address, employee_status)
VALUES 
('Alice', 'Marie', 'Johnson', 'alice.johnson@example.com', '0412345678', 'https://example.com/photos/alice.jpg', '123 Main St, Sydney', 'Active'),
('Bob', NULL, 'Smith', 'bob.smith@example.com', '0423456789', NULL, '456 High St, Melbourne', 'Active'),
('Charlie', 'Lee', 'Brown', 'charlie.brown@example.com', NULL, NULL, '789 River Rd, Brisbane', 'Inactive'),
('Diana', 'Rose', 'Taylor', 'diana.taylor@example.com', '0434567890', 'https://example.com/photos/diana.jpg', '321 Sunset Blvd, Perth', 'Active'),
('Edward', NULL, 'White', 'edward.white@example.com', '0445678901', NULL, '654 Ocean Ave, Adelaide', 'On Leave');


INSERT INTO contracts 
(employee_id, contract_type, contract_term, start_date, finish_date, ongoing, work_type, hours_per_week)
VALUES 
(1, 'Permanent', '2 years', '2023-01-01', '2024-12-31', FALSE, 'FullTime', 38.0),
(2, 'Temporary', '6 months', '2024-03-01', '2024-08-31', FALSE, 'PartTime', 20.0),
(3, 'Permanent', NULL, '2022-05-01', NULL, TRUE, 'FullTime', 38.0),
(4, 'Temporary', '1 year', '2024-01-15', '2025-01-14', FALSE, 'FullTime', 38.0),
(5, 'Permanent', 'Indefinite', '2020-07-01', NULL, TRUE, 'PartTime', 25.0);



INSERT INTO employee_details_archive 
(id, first_name, middle_name, last_name, email, mobile_number, photo_url, residential_address, employee_status, deleted_at, deleted_by)
VALUES 
(3, 'Charlie', 'Lee', 'Brown', 'charlie.brown@example.com', NULL, NULL, '789 River Rd, Brisbane', 'Inactive', NOW(), 'admin.user'),
(5, 'Edward', NULL, 'White', 'edward.white@example.com', '0445678901', NULL, '654 Ocean Ave, Adelaide', 'On Leave', NOW(), 'hr.manager');


INSERT INTO contract_archive 
(employee_id, contract_type, contract_term, start_date, finish_date, ongoing, work_type, hours_per_week, archived_at, archived_by)
VALUES 
(3, 'Permanent', NULL, '2022-05-01', NULL, TRUE, 'FullTime', 38.0, NOW(), 'admin.user'),
(5, 'Permanent', 'Indefinite', '2020-07-01', NULL, TRUE, 'PartTime', 25.0, NOW(), 'hr.manager');