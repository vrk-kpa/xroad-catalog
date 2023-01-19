-- noinspection SqlNoDataSourceInspectionForFile
-- Create tables
DROP SCHEMA IF EXISTS test CASCADE;
CREATE SCHEMA IF NOT EXISTS test;

CREATE TABLE IF NOT EXISTS test.member (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    x_road_instance TEXT NOT NULL,
    member_class TEXT NOT NULL,
    member_code TEXT NOT NULL,
    name TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.member_id_seq OWNED BY test.member.id;

CREATE TABLE IF NOT EXISTS test.subsystem (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    member_id BIGSERIAL NOT NULL REFERENCES test.member(id),
    subsystem_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.subsystem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.subsystem_id_seq OWNED BY test.subsystem.id;

CREATE TABLE IF NOT EXISTS test.service (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    subsystem_id BIGSERIAL NOT NULL REFERENCES test.subsystem(id),
    service_code TEXT NOT NULL,
    service_version TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.service_id_seq OWNED BY test.service.id;

CREATE TABLE IF NOT EXISTS test.wsdl (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES test.service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.wsdl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.wsdl_id_seq OWNED BY test.wsdl.id;

CREATE SEQUENCE IF NOT EXISTS test.wsdl_external_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.wsdl_external_id_seq OWNED BY test.wsdl.external_id;

CREATE TABLE IF NOT EXISTS test.open_api (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES test.service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.open_api_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.open_api_id_seq OWNED BY test.open_api.id;

CREATE SEQUENCE IF NOT EXISTS test.open_api_external_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER SEQUENCE test.open_api_external_id_seq OWNED BY test.open_api.external_id;

CREATE TABLE IF NOT EXISTS test.rest (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES test.service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
                          );

CREATE SEQUENCE IF NOT EXISTS test.rest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.rest_id_seq OWNED BY test.rest.id;

CREATE SEQUENCE IF NOT EXISTS test.rest_external_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

ALTER SEQUENCE test.rest_external_id_seq OWNED BY test.rest.external_id;

CREATE TABLE IF NOT EXISTS test.endpoint (
                                    id BIGSERIAL PRIMARY KEY NOT NULL,
                                    service_id BIGSERIAL NOT NULL REFERENCES test.service(id),
    method TEXT NOT NULL,
    path TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
                          );

CREATE SEQUENCE IF NOT EXISTS test.endpoint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.endpoint_id_seq OWNED BY test.endpoint.id;

CREATE TABLE IF NOT EXISTS test.organization (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_type TEXT NOT NULL,
    publishing_status TEXT NOT NULL,
    business_code TEXT NOT NULL,
    guid TEXT UNIQUE NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.organization_id_seq OWNED BY test.organization.id;

CREATE TABLE IF NOT EXISTS test.organization_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.organization_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.organization_name_id_seq OWNED BY test.organization_name.id;

CREATE TABLE IF NOT EXISTS test.organization_description (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.organization_description_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.organization_description_id_seq OWNED BY test.organization_description.id;

CREATE TABLE IF NOT EXISTS test.email (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
    language TEXT NOT NULL,
    description TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.email_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.email_id_seq OWNED BY test.email.id;

CREATE TABLE IF NOT EXISTS test.phone_number (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
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

CREATE SEQUENCE IF NOT EXISTS test.phone_number_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.phone_number_id_seq OWNED BY test.phone_number.id;

CREATE TABLE IF NOT EXISTS test.web_page (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
    language TEXT NOT NULL,
    url TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.web_page_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.web_page_id_seq OWNED BY test.web_page.id;

CREATE TABLE IF NOT EXISTS test.address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES test.organization(id),
    country TEXT NOT NULL,
    type TEXT NOT NULL,
    sub_type TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.address_id_seq OWNED BY test.address.id;

CREATE TABLE IF NOT EXISTS test.street_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES test.address(id),
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

CREATE SEQUENCE IF NOT EXISTS test.street_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_address_id_seq OWNED BY test.street_address.id;

CREATE TABLE IF NOT EXISTS test.post_office_box_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES test.address(id),
    postal_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_box_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_box_address_id_seq OWNED BY test.post_office_box_address.id;

CREATE TABLE IF NOT EXISTS test.street (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES test.street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.street_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_id_seq OWNED BY test.street.id;

CREATE TABLE IF NOT EXISTS test.street_address_post_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES test.street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.street_address_post_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_address_post_office_id_seq OWNED BY test.street_address_post_office.id;

CREATE TABLE IF NOT EXISTS test.street_address_municipality (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES test.street_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.street_address_municipality_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_address_municipality_id_seq OWNED BY test.street_address_municipality.id;

CREATE TABLE IF NOT EXISTS test.post_office_box_address_municipality (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES test.post_office_box_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_box_address_municipality_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_box_address_municipality_id_seq OWNED BY test.post_office_box_address_municipality.id;

CREATE TABLE IF NOT EXISTS test.street_address_municipality_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_municipality_id BIGSERIAL NOT NULL REFERENCES test.street_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.street_address_municipality_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_address_municipality_name_id_seq OWNED BY test.street_address_municipality_name.id;

CREATE TABLE IF NOT EXISTS test.post_office_box_address_municipality_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_municipality_id BIGSERIAL NOT NULL REFERENCES test.post_office_box_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_box_address_municipality_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_box_address_municipality_name_id_seq OWNED BY test.post_office_box_address_municipality_name.id;

CREATE TABLE IF NOT EXISTS test.street_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES test.street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.street_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.street_address_additional_information_id_seq OWNED BY test.street_address_additional_information.id;

CREATE TABLE IF NOT EXISTS test.post_office_box (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES test.post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_box_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_box_id_seq OWNED BY test.post_office_box.id;

CREATE TABLE IF NOT EXISTS test.post_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES test.post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_id_seq OWNED BY test.post_office.id;

CREATE TABLE IF NOT EXISTS test.post_office_box_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES test.post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.post_office_box_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.post_office_box_address_additional_information_id_seq OWNED BY test.post_office_box_address_additional_information.id;

CREATE TABLE IF NOT EXISTS test.company (
    id BIGSERIAL PRIMARY KEY NOT NULL,
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

CREATE SEQUENCE IF NOT EXISTS test.company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.company_id_seq OWNED BY test.company.id;

CREATE TABLE IF NOT EXISTS test.business_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    ordering BIGSERIAL NOT NULL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.business_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.business_name_id_seq OWNED BY test.business_name.id;

CREATE TABLE IF NOT EXISTS test.business_auxiliary_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    ordering BIGSERIAL NOT NULL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.business_auxiliary_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.business_auxiliary_name_id_seq OWNED BY test.business_auxiliary_name.id;

CREATE TABLE IF NOT EXISTS test.business_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    version BIGSERIAL NOT NULL,
    care_of TEXT,
    street TEXT,
    post_code TEXT,
    city TEXT,
    language TEXT,
    type BIGSERIAL NOT NULL,
    country TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.business_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.business_address_id_seq OWNED BY test.business_address.id;

CREATE TABLE IF NOT EXISTS test.company_form (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    type BIGSERIAL NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.company_form_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.company_form_id_seq OWNED BY test.company_form.id;

CREATE TABLE IF NOT EXISTS test.liquidation (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    type BIGSERIAL NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.liquidation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.liquidation_id_seq OWNED BY test.liquidation.id;

CREATE TABLE IF NOT EXISTS test.business_line (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    ordering BIGSERIAL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.business_line_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.business_line_id_seq OWNED BY test.business_line.id;

CREATE TABLE IF NOT EXISTS test.language (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.language_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.language_id_seq OWNED BY test.language.id;

CREATE TABLE IF NOT EXISTS test.registered_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    ordering BIGSERIAL NOT NULL,
    version BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    language TEXT,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.registered_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.registered_office_id_seq OWNED BY test.registered_office.id;

CREATE TABLE IF NOT EXISTS test.contact_detail (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
    version BIGSERIAL NOT NULL,
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

CREATE SEQUENCE IF NOT EXISTS test.contact_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.contact_detail_id_seq OWNED BY test.contact_detail.id;

CREATE TABLE IF NOT EXISTS test.registered_entry (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    description TEXT NOT NULL,
    status BIGSERIAL NOT NULL,
    register BIGSERIAL NOT NULL,
    language TEXT,
    authority BIGSERIAL NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS test.registered_entry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.registered_entry_id_seq OWNED BY test.registered_entry.id;

CREATE TABLE IF NOT EXISTS test.business_id_change (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    company_id BIGSERIAL NOT NULL REFERENCES test.company(id),
    source BIGSERIAL,
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

CREATE SEQUENCE IF NOT EXISTS test.business_id_change_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.business_id_change_id_seq OWNED BY test.business_id_change.id;

CREATE TABLE IF NOT EXISTS test.error_log (
    id BIGSERIAL PRIMARY KEY NOT NULL,
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

CREATE SEQUENCE IF NOT EXISTS test.error_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE test.error_log_id_seq OWNED BY test.error_log.id;

CREATE UNIQUE INDEX IF NOT EXISTS idx_wsdl_external_id ON test.wsdl USING btree (external_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_open_api_external_id ON test.open_api USING btree (external_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_rest_external_id ON test.rest USING btree (external_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_member_natural_keys ON test.member(member_code, member_class, x_road_instance);
CREATE UNIQUE INDEX IF NOT EXISTS idx_service_unique_fields ON test.service(subsystem_id, service_code, service_version);
CREATE UNIQUE INDEX IF NOT EXISTS idx_subsystem_unique_fields ON test.subsystem(member_id, subsystem_code);
CREATE UNIQUE INDEX IF NOT EXISTS idx_organization_guid ON test.organization USING btree (guid);

CREATE INDEX IF NOT EXISTS idx_wsdl_changed ON test.wsdl(changed);
CREATE INDEX IF NOT EXISTS idx_open_api_changed ON test.open_api(changed);
CREATE INDEX IF NOT EXISTS idx_rest_changed ON test.rest(changed);
CREATE INDEX IF NOT EXISTS idx_endpoint_changed ON test.endpoint(changed);
CREATE INDEX IF NOT EXISTS idx_service_changed ON test.service(changed);
CREATE INDEX IF NOT EXISTS idx_subsystem_changed ON test.subsystem(changed);
CREATE INDEX IF NOT EXISTS idx_member_changed ON test.member(changed);
CREATE INDEX IF NOT EXISTS idx_organization_changed ON test.organization(changed);
CREATE INDEX IF NOT EXISTS idx_address_changed ON test.address(changed);
CREATE INDEX IF NOT EXISTS idx_email_changed ON test.email(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_municipality_changed ON test.street_address_municipality(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_municipality_name_changed ON test.street_address_municipality_name(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_municipality_changed ON test.post_office_box_address_municipality(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_municipality_name_changed ON test.post_office_box_address_municipality_name(changed);
CREATE INDEX IF NOT EXISTS idx_organization_description_changed ON test.organization_description(changed);
CREATE INDEX IF NOT EXISTS idx_organization_name_changed ON test.organization_name(changed);
CREATE INDEX IF NOT EXISTS idx_phone_number_changed ON test.phone_number(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_changed ON test.post_office(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_changed ON test.post_office_box(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_changed ON test.post_office_box_address(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_additional_information_changed ON test.post_office_box_address_additional_information(changed);
CREATE INDEX IF NOT EXISTS idx_street_changed ON test.street(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_changed ON test.street_address(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_additional_information_changed ON test.street_address_additional_information(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_post_office_changed ON test.street_address_post_office(changed);
CREATE INDEX IF NOT EXISTS idx_webpage_changed ON test.web_page(changed);
CREATE INDEX IF NOT EXISTS idx_company_changed ON test.company(changed);
CREATE INDEX IF NOT EXISTS idx_business_name_changed ON test.business_name(changed);
CREATE INDEX IF NOT EXISTS idx_business_auxiliary_name_changed ON test.business_auxiliary_name(changed);
CREATE INDEX IF NOT EXISTS idx_business_address_changed ON test.business_address(changed);
CREATE INDEX IF NOT EXISTS idx_company_form_changed ON test.company_form(changed);
CREATE INDEX IF NOT EXISTS idx_liquidation_changed ON test.liquidation(changed);
CREATE INDEX IF NOT EXISTS idx_business_line_changed ON test.business_line(changed);
CREATE INDEX IF NOT EXISTS idx_language_changed ON test.language(changed);
CREATE INDEX IF NOT EXISTS idx_registered_office_changed ON test.registered_office(changed);
CREATE INDEX IF NOT EXISTS idx_contact_detail_changed ON test.contact_detail(changed);
CREATE INDEX IF NOT EXISTS idx_registered_entry_changed ON test.registered_entry(changed);
CREATE INDEX IF NOT EXISTS idx_business_id_change_changed ON test.business_id_change(changed);
CREATE INDEX IF NOT EXISTS idx_wsdl_service_id ON test.wsdl(service_id);
CREATE INDEX IF NOT EXISTS idx_open_api_service_id ON test.open_api(service_id);
CREATE INDEX IF NOT EXISTS idx_rest_service_id ON test.rest(service_id);
CREATE INDEX IF NOT EXISTS idx_endpoint_service_id ON test.endpoint(service_id);
CREATE INDEX IF NOT EXISTS idx_organization_description_organization_id ON test.organization_description(organization_id);
CREATE INDEX IF NOT EXISTS idx_organization_name_organization_id ON test.organization_name(organization_id);
CREATE INDEX IF NOT EXISTS idx_email_organization_id ON test.email(organization_id);
CREATE INDEX IF NOT EXISTS idx_address_organization_id ON test.address(organization_id);
CREATE INDEX IF NOT EXISTS idx_phone_number_organization_id ON test.phone_number(organization_id);
CREATE INDEX IF NOT EXISTS idx_webpage_organization_id ON test.web_page(organization_id);
CREATE INDEX IF NOT EXISTS idx_business_name_company_id ON test.business_name(company_id);
CREATE INDEX IF NOT EXISTS idx_business_auxiliary_name_company_id ON test.business_auxiliary_name(company_id);
CREATE INDEX IF NOT EXISTS idx_business_address_company_id ON test.business_address(company_id);
CREATE INDEX IF NOT EXISTS idx_company_form_company_id ON test.company_form(company_id);
CREATE INDEX IF NOT EXISTS idx_liquidation_company_id ON test.liquidation(company_id);
CREATE INDEX IF NOT EXISTS idx_business_line_company_id ON test.business_line(company_id);
CREATE INDEX IF NOT EXISTS idx_language_company_id ON test.language(company_id);
CREATE INDEX IF NOT EXISTS idx_registered_office_company_id ON test.registered_office(company_id);
CREATE INDEX IF NOT EXISTS idx_contact_detail_company_id ON test.contact_detail(company_id);
CREATE INDEX IF NOT EXISTS idx_registered_entry_company_id ON test.registered_entry(company_id);
CREATE INDEX IF NOT EXISTS idx_business_id_change_company_id ON test.business_id_change(company_id);

ALTER TABLE test.member OWNER TO xroad_catalog;
ALTER TABLE test.service OWNER TO xroad_catalog;
ALTER TABLE test.subsystem OWNER TO xroad_catalog;
ALTER TABLE test.wsdl OWNER TO xroad_catalog;
ALTER TABLE test.open_api OWNER TO xroad_catalog;
ALTER TABLE test.rest OWNER TO xroad_catalog;
ALTER TABLE test.endpoint OWNER TO xroad_catalog;
ALTER TABLE test.organization OWNER TO xroad_catalog;
ALTER TABLE test.organization_name OWNER TO xroad_catalog;
ALTER TABLE test.organization_description OWNER TO xroad_catalog;
ALTER TABLE test.email OWNER TO xroad_catalog;
ALTER TABLE test.phone_number OWNER TO xroad_catalog;
ALTER TABLE test.web_page OWNER TO xroad_catalog;
ALTER TABLE test.address OWNER TO xroad_catalog;
ALTER TABLE test.street_address OWNER TO xroad_catalog;
ALTER TABLE test.street OWNER TO xroad_catalog;
ALTER TABLE test.street_address_post_office OWNER TO xroad_catalog;
ALTER TABLE test.street_address_municipality OWNER TO xroad_catalog;
ALTER TABLE test.street_address_municipality_name OWNER TO xroad_catalog;
ALTER TABLE test.street_address_additional_information OWNER TO xroad_catalog;
ALTER TABLE test.post_office_box_address OWNER TO xroad_catalog;
ALTER TABLE test.post_office_box OWNER TO xroad_catalog;
ALTER TABLE test.post_office OWNER TO xroad_catalog;
ALTER TABLE test.post_office_box_address_municipality OWNER TO xroad_catalog;
ALTER TABLE test.post_office_box_address_municipality_name OWNER TO xroad_catalog;
ALTER TABLE test.post_office_box_address_additional_information OWNER TO xroad_catalog;
ALTER TABLE test.company OWNER TO xroad_catalog;
ALTER TABLE test.business_name OWNER TO xroad_catalog;
ALTER TABLE test.business_auxiliary_name OWNER TO xroad_catalog;
ALTER TABLE test.business_address OWNER TO xroad_catalog;
ALTER TABLE test.company_form OWNER TO xroad_catalog;
ALTER TABLE test.liquidation OWNER TO xroad_catalog;
ALTER TABLE test.business_line OWNER TO xroad_catalog;
ALTER TABLE test.language OWNER TO xroad_catalog;
ALTER TABLE test.registered_office OWNER TO xroad_catalog;
ALTER TABLE test.contact_detail OWNER TO xroad_catalog;
ALTER TABLE test.registered_entry OWNER TO xroad_catalog;
ALTER TABLE test.business_id_change OWNER TO xroad_catalog;
ALTER TABLE test.error_log OWNER TO xroad_catalog;