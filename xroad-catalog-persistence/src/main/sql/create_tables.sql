-- noinspection SqlNoDataSourceInspectionForFile
-- Create tables

\connect xroad_catalog;

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE TABLE member (
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

CREATE SEQUENCE member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE member_id_seq OWNED BY member.id;

CREATE TABLE subsystem (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    member_id BIGSERIAL NOT NULL REFERENCES member(id),
    subsystem_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE subsystem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE subsystem_id_seq OWNED BY subsystem.id;

CREATE TABLE service (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    subsystem_id BIGSERIAL NOT NULL REFERENCES subsystem(id),
    service_code TEXT NOT NULL,
    service_version TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE service_id_seq OWNED BY service.id;

CREATE TABLE wsdl (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE wsdl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE wsdl_id_seq OWNED BY wsdl.id;

CREATE SEQUENCE wsdl_external_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE wsdl_external_id_seq OWNED BY wsdl.external_id;

CREATE TABLE open_api (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE openapi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE openapi_id_seq OWNED BY open_api.id;

CREATE SEQUENCE openapi_external_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE openapi_external_id_seq OWNED BY open_api.external_id;

CREATE TABLE organization (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_type TEXT NOT NULL,
    publishing_status TEXT NOT NULL,
    business_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_id_seq OWNED BY organization.id;

CREATE TABLE organization_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE organization_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_name_id_seq OWNED BY organization_name.id;

CREATE TABLE organization_description (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE organization_description_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_description_id_seq OWNED BY organization_description.id;

CREATE TABLE email (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    description TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE email_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE email_id_seq OWNED BY email.id;

CREATE TABLE phone_number (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    additional_information TEXT,
    service_charge_type TEXT,
    charge_description TEXT,
    prefix_number TEXT NOT NULL,
    is_finnish_service_number boolean NOT NULL,
    number TEXT NOT NULL
);

CREATE SEQUENCE phone_number_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE phone_number_id_seq OWNED BY phone_number.id;

CREATE TABLE webpage (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    url TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE webpage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE webpage_id_seq OWNED BY webpage.id;

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    country TEXT NOT NULL,
    type TEXT NOT NULL,
    sub_type TEXT NOT NULL
);

CREATE SEQUENCE address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE address_id_seq OWNED BY address.id;

CREATE TABLE street_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES address(id),
    street_number TEXT NOT NULL,
    postal_code TEXT NOT NULL,
    latitude TEXT NOT NULL,
    longitude TEXT NOT NULL,
    coordinate_state TEXT NOT NULL
);

CREATE SEQUENCE street_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_id_seq OWNED BY street_address.id;

CREATE TABLE street (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE street_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_id_seq OWNED BY street.id;

CREATE TABLE street_address_postoffice (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE street_address_postoffice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_postoffice_id_seq OWNED BY street_address_postoffice.id;

CREATE TABLE municipality (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    code TEXT NOT NULL
);

CREATE SEQUENCE municipality_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE municipality_id_seq OWNED BY municipality.id;

CREATE TABLE municipality_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    municipality_id BIGSERIAL NOT NULL REFERENCES municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE municipality_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE municipality_name_id_seq OWNED BY municipality_name.id;

CREATE TABLE street_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE street_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_additional_information_id_seq OWNED BY street_address_additional_information.id;

CREATE TABLE post_office_box_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES address(id),
    postal_code TEXT NOT NULL,
    municipality TEXT
);

CREATE SEQUENCE post_office_box_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_id_seq OWNED BY post_office_box_address.id;

CREATE TABLE post_office_box (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE post_office_box_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_id_seq OWNED BY post_office_box.id;

CREATE TABLE post_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE post_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_id_seq OWNED BY post_office.id;

CREATE TABLE post_office_box_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL
);

CREATE SEQUENCE post_office_box_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_additional_information_id_seq OWNED BY post_office_box_address_additional_information.id;

CREATE UNIQUE INDEX idx_wsdl_external_id ON wsdl USING btree (external_id);
CREATE UNIQUE INDEX idx_openapi_external_id ON open_api USING btree (external_id);
CREATE UNIQUE INDEX idx_member_natural_keys ON member(member_code, member_class, x_road_instance);
CREATE UNIQUE INDEX idx_service_unique_fields ON service(subsystem_id, service_code, service_version);
CREATE UNIQUE INDEX idx_subsystem_unique_fields ON subsystem(member_id, subsystem_code);
CREATE UNIQUE INDEX idx_organization_unique_fields ON organization(organization_type, business_code);
CREATE UNIQUE INDEX idx_organization_description_unique_fields ON organization_description(type, value);
CREATE UNIQUE INDEX idx_organization_name_unique_fields ON organization_name(type, value);
CREATE UNIQUE INDEX idx_email_unique_fields ON email(description, value);
CREATE UNIQUE INDEX idx_address_unique_fields ON address(country, type, sub_type);
CREATE UNIQUE INDEX idx_webpage_unique_fields ON webpage(url, value);
CREATE UNIQUE INDEX idx_phone_number_unique_fields ON phone_number(additional_information, prefix_number, number);

CREATE INDEX idx_wsdl_changed ON wsdl(changed);
CREATE INDEX idx_openapi_changed ON open_api(changed);
CREATE INDEX idx_service_changed ON service(changed);
CREATE INDEX idx_subsystem_changed ON subsystem(changed);
CREATE INDEX idx_member_changed ON member(changed);
CREATE INDEX idx_organization_changed ON organization(changed);
CREATE INDEX idx_wsdl_service_id ON wsdl(service_id);
CREATE INDEX idx_openapi_service_id ON open_api(service_id);
CREATE INDEX idx_organization_description_organization_id ON organization_description(organization_id);
CREATE INDEX idx_organization_name_organization_id ON organization_name(organization_id);
CREATE INDEX idx_email_organization_id ON email(organization_id);
CREATE INDEX idx_address_organization_id ON address(organization_id);
CREATE INDEX idx_phone_number_organization_id ON phone_number(organization_id);
CREATE INDEX idx_webpage_organization_id ON webpage(organization_id);

ALTER TABLE member OWNER TO xroad_catalog;
ALTER TABLE service OWNER TO xroad_catalog;
ALTER TABLE subsystem OWNER TO xroad_catalog;
ALTER TABLE wsdl OWNER TO xroad_catalog;
ALTER TABLE open_api OWNER TO xroad_catalog;
ALTER TABLE organization OWNER TO xroad_catalog;
ALTER TABLE organization_name OWNER TO xroad_catalog;
ALTER TABLE organization_description OWNER TO xroad_catalog;
ALTER TABLE email OWNER TO xroad_catalog;
ALTER TABLE phone_number OWNER TO xroad_catalog;
ALTER TABLE webpage OWNER TO xroad_catalog;
ALTER TABLE address OWNER TO xroad_catalog;
ALTER TABLE street_address OWNER TO xroad_catalog;
ALTER TABLE street OWNER TO xroad_catalog;
ALTER TABLE street_address_postoffice OWNER TO xroad_catalog;
ALTER TABLE municipality OWNER TO xroad_catalog;
ALTER TABLE municipality_name OWNER TO xroad_catalog;
ALTER TABLE street_address_additional_information OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address OWNER TO xroad_catalog;
ALTER TABLE post_office_box OWNER TO xroad_catalog;
ALTER TABLE post_office OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address_additional_information OWNER TO xroad_catalog;