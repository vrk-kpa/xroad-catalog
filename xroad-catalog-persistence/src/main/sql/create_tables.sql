-- noinspection SqlNoDataSourceInspectionForFile
-- Create tables

\connect xroad_catalog;

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE TABLE IF NOT EXISTS member (
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

CREATE SEQUENCE IF NOT EXISTS member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE member_id_seq OWNED BY member.id;

CREATE TABLE IF NOT EXISTS subsystem (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    member_id BIGSERIAL NOT NULL REFERENCES member(id),
    subsystem_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS subsystem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE subsystem_id_seq OWNED BY subsystem.id;

CREATE TABLE IF NOT EXISTS service (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    subsystem_id BIGSERIAL NOT NULL REFERENCES subsystem(id),
    service_code TEXT NOT NULL,
    service_version TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE service_id_seq OWNED BY service.id;

CREATE TABLE IF NOT EXISTS wsdl (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS wsdl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE wsdl_id_seq OWNED BY wsdl.id;

CREATE SEQUENCE IF NOT EXISTS wsdl_external_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE wsdl_external_id_seq OWNED BY wsdl.external_id;

CREATE TABLE IF NOT EXISTS open_api (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES service(id),
    data TEXT NOT NULL,
    external_id TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS open_api_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE open_api_id_seq OWNED BY open_api.id;

CREATE SEQUENCE IF NOT EXISTS open_api_external_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE open_api_external_id_seq OWNED BY open_api.external_id;

CREATE TABLE IF NOT EXISTS organization (
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

CREATE SEQUENCE IF NOT EXISTS organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_id_seq OWNED BY organization.id;

CREATE TABLE IF NOT EXISTS organization_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS organization_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_name_id_seq OWNED BY organization_name.id;

CREATE TABLE IF NOT EXISTS organization_description (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS organization_description_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE organization_description_id_seq OWNED BY organization_description.id;

CREATE TABLE IF NOT EXISTS email (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    description TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS email_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE email_id_seq OWNED BY email.id;

CREATE TABLE IF NOT EXISTS phone_number (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
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

CREATE SEQUENCE IF NOT EXISTS phone_number_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE phone_number_id_seq OWNED BY phone_number.id;

CREATE TABLE IF NOT EXISTS web_page (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    language TEXT NOT NULL,
    url TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS web_page_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE web_page_id_seq OWNED BY web_page.id;

CREATE TABLE IF NOT EXISTS address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    organization_id BIGSERIAL NOT NULL REFERENCES organization(id),
    country TEXT NOT NULL,
    type TEXT NOT NULL,
    sub_type TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE address_id_seq OWNED BY address.id;

CREATE TABLE IF NOT EXISTS street_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES address(id),
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

CREATE SEQUENCE IF NOT EXISTS street_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_id_seq OWNED BY street_address.id;

CREATE TABLE IF NOT EXISTS post_office_box_address (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    address_id BIGSERIAL NOT NULL REFERENCES address(id),
    postal_code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_box_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_id_seq OWNED BY post_office_box_address.id;

CREATE TABLE IF NOT EXISTS street (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS street_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_id_seq OWNED BY street.id;

CREATE TABLE IF NOT EXISTS street_address_post_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS street_address_post_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_post_office_id_seq OWNED BY street_address_post_office.id;

CREATE TABLE IF NOT EXISTS street_address_municipality (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS street_address_municipality_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_municipality_id_seq OWNED BY street_address_municipality.id;

CREATE TABLE IF NOT EXISTS post_office_box_address_municipality (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    code TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_box_address_municipality_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_municipality_id_seq OWNED BY post_office_box_address_municipality.id;

CREATE TABLE IF NOT EXISTS street_address_municipality_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_municipality_id BIGSERIAL NOT NULL REFERENCES street_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS street_address_municipality_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_municipality_name_id_seq OWNED BY street_address_municipality_name.id;

CREATE TABLE IF NOT EXISTS post_office_box_address_municipality_name (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_municipality_id BIGSERIAL NOT NULL REFERENCES post_office_box_address_municipality(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_box_address_municipality_name_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_municipality_name_id_seq OWNED BY post_office_box_address_municipality_name.id;

CREATE TABLE IF NOT EXISTS street_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    street_address_id BIGSERIAL NOT NULL REFERENCES street_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS street_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE street_address_additional_information_id_seq OWNED BY street_address_additional_information.id;

CREATE TABLE IF NOT EXISTS post_office_box (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_box_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_id_seq OWNED BY post_office_box.id;

CREATE TABLE IF NOT EXISTS post_office (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_id_seq OWNED BY post_office.id;

CREATE TABLE IF NOT EXISTS post_office_box_address_additional_information (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    post_office_box_address_id BIGSERIAL NOT NULL REFERENCES post_office_box_address(id),
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    changed TIMESTAMP WITH TIME ZONE NOT NULL,
    fetched TIMESTAMP WITH TIME ZONE NOT NULL,
    removed TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE IF NOT EXISTS post_office_box_address_additional_information_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE post_office_box_address_additional_information_id_seq OWNED BY post_office_box_address_additional_information.id;

CREATE UNIQUE INDEX IF NOT EXISTS idx_wsdl_external_id ON wsdl USING btree (external_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_open_api_external_id ON open_api USING btree (external_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_member_natural_keys ON member(member_code, member_class, x_road_instance);
CREATE UNIQUE INDEX IF NOT EXISTS idx_service_unique_fields ON service(subsystem_id, service_code, service_version);
CREATE UNIQUE INDEX IF NOT EXISTS idx_subsystem_unique_fields ON subsystem(member_id, subsystem_code);
CREATE UNIQUE INDEX IF NOT EXISTS idx_organization_guid ON organization USING btree (guid);

CREATE INDEX IF NOT EXISTS idx_wsdl_changed ON wsdl(changed);
CREATE INDEX IF NOT EXISTS idx_open_api_changed ON open_api(changed);
CREATE INDEX IF NOT EXISTS idx_service_changed ON service(changed);
CREATE INDEX IF NOT EXISTS idx_subsystem_changed ON subsystem(changed);
CREATE INDEX IF NOT EXISTS idx_member_changed ON member(changed);
CREATE INDEX IF NOT EXISTS idx_organization_changed ON organization(changed);
CREATE INDEX IF NOT EXISTS idx_address_changed ON address(changed);
CREATE INDEX IF NOT EXISTS idx_email_changed ON email(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_municipality_changed ON street_address_municipality(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_municipality_name_changed ON street_address_municipality_name(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_municipality_changed ON post_office_box_address_municipality(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_municipality_name_changed ON post_office_box_address_municipality_name(changed);
CREATE INDEX IF NOT EXISTS idx_organization_description_changed ON organization_description(changed);
CREATE INDEX IF NOT EXISTS idx_organization_name_changed ON organization_name(changed);
CREATE INDEX IF NOT EXISTS idx_phone_number_changed ON phone_number(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_changed ON post_office(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_changed ON post_office_box(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_changed ON post_office_box_address(changed);
CREATE INDEX IF NOT EXISTS idx_post_office_box_address_additional_information_changed ON post_office_box_address_additional_information(changed);
CREATE INDEX IF NOT EXISTS idx_street_changed ON street(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_changed ON street_address(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_additional_information_changed ON street_address_additional_information(changed);
CREATE INDEX IF NOT EXISTS idx_street_address_post_office_changed ON street_address_post_office(changed);
CREATE INDEX IF NOT EXISTS idx_webpage_changed ON web_page(changed);
CREATE INDEX IF NOT EXISTS idx_wsdl_service_id ON wsdl(service_id);
CREATE INDEX IF NOT EXISTS idx_open_api_service_id ON open_api(service_id);
CREATE INDEX IF NOT EXISTS idx_organization_description_organization_id ON organization_description(organization_id);
CREATE INDEX IF NOT EXISTS idx_organization_name_organization_id ON organization_name(organization_id);
CREATE INDEX IF NOT EXISTS idx_email_organization_id ON email(organization_id);
CREATE INDEX IF NOT EXISTS idx_address_organization_id ON address(organization_id);
CREATE INDEX IF NOT EXISTS idx_phone_number_organization_id ON phone_number(organization_id);
CREATE INDEX IF NOT EXISTS idx_webpage_organization_id ON web_page(organization_id);

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
ALTER TABLE web_page OWNER TO xroad_catalog;
ALTER TABLE address OWNER TO xroad_catalog;
ALTER TABLE street_address OWNER TO xroad_catalog;
ALTER TABLE street OWNER TO xroad_catalog;
ALTER TABLE street_address_post_office OWNER TO xroad_catalog;
ALTER TABLE street_address_municipality OWNER TO xroad_catalog;
ALTER TABLE street_address_municipality_name OWNER TO xroad_catalog;
ALTER TABLE street_address_additional_information OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address OWNER TO xroad_catalog;
ALTER TABLE post_office_box OWNER TO xroad_catalog;
ALTER TABLE post_office OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address_municipality OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address_municipality_name OWNER TO xroad_catalog;
ALTER TABLE post_office_box_address_additional_information OWNER TO xroad_catalog;