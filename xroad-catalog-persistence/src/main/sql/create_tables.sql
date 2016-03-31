-- Create tables

\connect xroad_catalog;

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;



CREATE TABLE member (
    id bigint NOT NULL,
    x_road_instance text NOT NULL,
    member_class text NOT NULL,
    member_code text NOT NULL,
    name text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
);





CREATE SEQUENCE member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE member_id_seq OWNED BY member.id;



CREATE TABLE service (
    id bigint NOT NULL,
    subsystem_id bigint NOT NULL,
    service_code text NOT NULL,
    service_version text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
);




CREATE SEQUENCE service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE service_id_seq OWNED BY service.id;



CREATE TABLE subsystem (
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    subsystem_code text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
);


CREATE SEQUENCE subsystem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER SEQUENCE subsystem_id_seq OWNED BY subsystem.id;


CREATE TABLE wsdl (
    id bigint NOT NULL,
    service_id bigint NOT NULL,
    data text NOT NULL,
    external_id text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
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



ALTER TABLE ONLY member
    ADD CONSTRAINT primary_key_client PRIMARY KEY (id);



ALTER TABLE ONLY service
    ADD CONSTRAINT primary_key_service PRIMARY KEY (id);




ALTER TABLE ONLY subsystem
    ADD CONSTRAINT primary_key_subsystem PRIMARY KEY (id);



ALTER TABLE ONLY wsdl
    ADD CONSTRAINT primary_key_wsdl PRIMARY KEY (id);


CREATE UNIQUE INDEX idx_wsdl_external_id ON wsdl USING btree (external_id);
CREATE UNIQUE INDEX idx_member_natural_keys ON member(member_code, member_class, x_road_instance);
CREATE UNIQUE INDEX idx_service_unique_fields ON service(subsystem_id, service_code, service_version);
CREATE UNIQUE INDEX idx_subsystem_unique_fields ON subsystem(member_id, subsystem_code);

CREATE INDEX idx_wsdl_changed ON wsdl(changed);
CREATE INDEX idx_service_changed ON service(changed);
CREATE INDEX idx_subsystem_changed ON subsystem(changed);
CREATE INDEX idx_member_changed ON member(changed);
CREATE INDEX idx_wsdl_service_id ON wsdl(service_id);




ALTER TABLE ONLY subsystem
    ADD CONSTRAINT foreign_key_member FOREIGN KEY (member_id) REFERENCES member(id);



ALTER TABLE ONLY wsdl
    ADD CONSTRAINT foreign_key_service FOREIGN KEY (service_id) REFERENCES service(id);



ALTER TABLE ONLY service
    ADD CONSTRAINT foreign_key_subsystem FOREIGN KEY (subsystem_id) REFERENCES subsystem(id);


ALTER TABLE member OWNER TO xroad_catalog;
ALTER TABLE service OWNER TO xroad_catalog;
ALTER TABLE subsystem OWNER TO xroad_catalog;
ALTER TABLE wsdl OWNER TO xroad_catalog;