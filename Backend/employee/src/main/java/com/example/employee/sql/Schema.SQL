CREATE TABLE employee_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mobile_number VARCHAR(20),
    photoUrl TEXT,
    residential_address TEXT,
    employee_status VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE contracts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    contract_type ENUM('Temporary', 'Permanent') NOT NULL,
    contract_term VARCHAR(100),
    start_date DATE,
    finish_date DATE,
    ongoing BOOLEAN DEFAULT FALSE,
    work_type ENUM('FullTime', 'PartTime') NOT NULL,
    hours_per_week DECIMAL(4,1),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (employee_id) REFERENCES employee_details(id) ON DELETE CASCADE
);

CREATE TABLE employee_details_archive (
    id INT PRIMARY KEY,
    first_name VARCHAR(100),
    middle_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    mobile_number VARCHAR(20),
    photoUrl TEXT,
    residential_address TEXT,
    employee_status VARCHAR(100),

    deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_by VARCHAR(100)
);


CREATE TABLE contract_archive (
    id INT AUTO_INCREMENT PRIMARY KEY,

    employee_id INT NOT NULL,
    contract_type ENUM('Temporary', 'Permanent') NOT NULL,
    contract_term VARCHAR(100),
    start_date DATE,
    finish_date DATE,
    ongoing BOOLEAN DEFAULT FALSE,
    work_type ENUM('FullTime', 'PartTime') NOT NULL,
    hours_per_week DECIMAL(4,1),

    archived_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    archived_by VARCHAR(100),

    INDEX (employee_id)
);