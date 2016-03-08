--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.10
-- Dumped by pg_dump version 9.3.10
-- Started on 2016-01-20 14:53:33 EET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE service_catalog;
--
-- TOC entry 2008 (class 1262 OID 16386)
-- Name: service_catalog; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE service_catalog WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';
-- on windows, use
-- CREATE DATABASE service_catalog WITH TEMPLATE = template0 ENCODING = 'UTF8';

ALTER DATABASE service_catalog OWNER TO postgres;

\connect service_catalog

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2009 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 178 (class 3079 OID 11787)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2011 (class 0 OID 0)
-- Dependencies: 178
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 16399)
-- Name: member; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

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


ALTER TABLE public.member OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 16397)
-- Name: member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.member_id_seq OWNER TO postgres;

--
-- TOC entry 2012 (class 0 OID 0)
-- Dependencies: 170
-- Name: member_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE member_id_seq OWNED BY member.id;


--
-- TOC entry 173 (class 1259 OID 16446)
-- Name: service; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

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


ALTER TABLE public.service OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16444)
-- Name: service_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.service_id_seq OWNER TO postgres;

--
-- TOC entry 2013 (class 0 OID 0)
-- Dependencies: 172
-- Name: service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE service_id_seq OWNED BY service.id;


--
-- TOC entry 176 (class 1259 OID 16529)
-- Name: subsystem; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE subsystem (
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    subsystem_code text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
);


ALTER TABLE public.subsystem OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 16535)
-- Name: subsystem_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE subsystem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.subsystem_id_seq OWNER TO postgres;

--
-- TOC entry 2014 (class 0 OID 0)
-- Dependencies: 177
-- Name: subsystem_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE subsystem_id_seq OWNED BY subsystem.id;


--
-- TOC entry 175 (class 1259 OID 16464)
-- Name: wsdl; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
--

CREATE TABLE wsdl (
    id bigint NOT NULL,
    service_id bigint NOT NULL,
    data text NOT NULL,
    data_hash text NOT NULL,
    external_id text NOT NULL,
    created timestamp with time zone NOT NULL,
    changed timestamp with time zone NOT NULL,
    fetched timestamp with time zone NOT NULL,
    removed timestamp with time zone
);


ALTER TABLE public.wsdl OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 16462)
-- Name: wsdl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE wsdl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.wsdl_id_seq OWNER TO postgres;

--
-- TOC entry 2015 (class 0 OID 0)
-- Dependencies: 174
-- Name: wsdl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE wsdl_id_seq OWNED BY wsdl.id;

--
--
-- TOC entry 1886 (class 2606 OID 16456)
-- Name: primary_key_client; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY member
    ADD CONSTRAINT primary_key_client PRIMARY KEY (id);


--
-- TOC entry 1888 (class 2606 OID 16454)
-- Name: primary_key_service; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY service
    ADD CONSTRAINT primary_key_service PRIMARY KEY (id);


--
-- TOC entry 1893 (class 2606 OID 16545)
-- Name: primary_key_subsystem; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY subsystem
    ADD CONSTRAINT primary_key_subsystem PRIMARY KEY (id);


--
-- TOC entry 1891 (class 2606 OID 16472)
-- Name: primary_key_wsdl; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY wsdl
    ADD CONSTRAINT primary_key_wsdl PRIMARY KEY (id);


--
-- TOC entry 1889 (class 1259 OID 16528)
-- Name: idx_wsdl_external_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace:
--

CREATE UNIQUE INDEX idx_wsdl_external_id ON wsdl USING btree (external_id);


-- additional indexes for performance
CREATE INDEX idx_wsdl_changed ON wsdl(changed);
CREATE INDEX idx_service_changed ON service(changed);
CREATE INDEX idx_subsystem_changed ON subsystem(changed);
CREATE INDEX idx_member_changed ON member(changed);
CREATE INDEX idx_wsdl_service_id ON wsdl(service_id);
CREATE INDEX idx_service_subsystem_id ON service(subsystem_id);
CREATE INDEX idx_subsystem_member_id ON subsystem(member_id);



--
-- TOC entry 1896 (class 2606 OID 16551)
-- Name: foreign_key_member; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subsystem
    ADD CONSTRAINT foreign_key_member FOREIGN KEY (member_id) REFERENCES member(id);


--
-- TOC entry 1895 (class 2606 OID 16473)
-- Name: foreign_key_service; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY wsdl
    ADD CONSTRAINT foreign_key_service FOREIGN KEY (service_id) REFERENCES service(id);


--
-- TOC entry 1894 (class 2606 OID 16546)
-- Name: foreign_key_subsystem; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY service
    ADD CONSTRAINT foreign_key_subsystem FOREIGN KEY (subsystem_id) REFERENCES subsystem(id);


--
-- TOC entry 2010 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-01-20 14:53:34 EET

--
-- PostgreSQL database dump complete
--

