
CREATE TABLE IF NOT EXISTS member (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    x_road_instance TEXT NOT NULL,
    member_class TEXT NOT NULL,
    member_code TEXT NOT NULL,
    name TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS subsystem (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    member_id INT NOT NULL REFERENCES member(id),
    subsystem_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS service (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    subsystem_id INT NOT NULL REFERENCES subsystem(id),
    service_code TEXT NOT NULL,
    service_version TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS wsdl (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    service_id INT NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS open_api (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    service_id INT NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS rest (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    service_id INT NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS endpoint (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    service_id INT NOT NULL REFERENCES service(id),
    method TEXT NOT NULL,
    path TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS organization (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_type TEXT NOT NULL,
    publishing_status TEXT NOT NULL,
    business_code TEXT NOT NULL,
    guid TEXT UNIQUE NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS organization_name (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS organization_description (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS email (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    description TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS phone_number (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    additional_information TEXT,
    service_charge_type TEXT,
    charge_description TEXT,
    prefix_number TEXT NOT NULL,
    is_finnish_service_number boolean NOT NULL,
    number TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS web_page (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    url TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS address (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    organization_id INT NOT NULL REFERENCES organization(id),
    country TEXT NOT NULL,
    type TEXT NOT NULL,
    sub_type TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street_address (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    address_id INT NOT NULL REFERENCES address(id),
    street_number TEXT NOT NULL,
    postal_code TEXT NOT NULL,
    latitude TEXT NOT NULL,
    longitude TEXT NOT NULL,
    coordinate_state TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office_box_address (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    address_id INT NOT NULL REFERENCES address(id),
    postal_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    street_address_id INT NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street_address_post_office (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    street_address_id INT NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street_address_municipality (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    street_address_id INT NOT NULL REFERENCES street_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office_box_address_municipality (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    post_office_box_address_id INT NOT NULL REFERENCES post_office_box_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street_address_municipality_name (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    street_address_municipality_id INT NOT NULL REFERENCES street_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office_box_address_municipality_name (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    post_office_box_address_municipality_id INT NOT NULL REFERENCES post_office_box_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS street_address_additional_information (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    street_address_id INT NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office_box (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    post_office_box_address_id INT NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    post_office_box_address_id INT NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS post_office_box_address_additional_information (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    post_office_box_address_id INT NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS company (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    business_id TEXT NOT NULL,
    company_form TEXT,
    details_uri TEXT,
    name TEXT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS business_name (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    ordering INT NOT NULL,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS business_auxiliary_name (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    ordering INT NOT NULL,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS business_address (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    version INT NOT NULL,
    care_of TEXT,
    street TEXT,
    post_code TEXT,
    city TEXT,
    language TEXT,
    type INT NOT NULL,
    country TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS company_form (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    type INT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS liquidation (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    type INT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS business_line (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    ordering INT,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS language (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS registered_office (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    ordering INT NOT NULL,
    version INT NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS contact_detail (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    version INT NOT NULL,
    language TEXT,
    value TEXT NOT NULL,
    type TEXT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS registered_entry (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    description TEXT NOT NULL,
    status INT NOT NULL,
    register INT NOT NULL,
    language TEXT,
    authority INT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS business_id_change (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    company_id INT NOT NULL REFERENCES company(id),
    source INT,
    description TEXT NOT NULL,
    reason TEXT NOT NULL,
    change_date TEXT,
    change TEXT NOT NULL,
    old_business_id TEXT NOT NULL,
    new_business_id TEXT NOT NULL,
    language TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS error_log (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    message TEXT NOT NULL,
    code TEXT NOT NULL,
    x_road_instance TEXT,
    member_class TEXT,
    member_code TEXT,
    subsystem_code TEXT,
    group_code TEXT,
    service_code TEXT,
    service_version TEXT,
    security_category_code TEXT,
    server_code TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL
);