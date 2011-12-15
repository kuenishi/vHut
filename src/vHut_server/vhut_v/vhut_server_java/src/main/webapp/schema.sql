--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: vhut
--

CREATE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO vhut;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: application; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    vhut_user_id bigint NOT NULL,
    image_url character varying(256),
    structure character varying(10485760),
    reservation_id bigint NOT NULL,
    status integer,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application OWNER TO vhut;

--
-- Name: application_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_id_seq OWNER TO vhut;

--
-- Name: application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_id_seq OWNED BY application.id;


--
-- Name: application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_id_seq', 1, false);


--
-- Name: application_instance; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_instance (
    id bigint NOT NULL,
    vhut_user_id bigint NOT NULL,
    status integer,
    application_instance_group_id bigint NOT NULL,
    released_application_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_instance OWNER TO vhut;

--
-- Name: application_instance_group; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_instance_group (
    id bigint NOT NULL,
    vhut_user_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    application_id bigint NOT NULL,
    password character varying(24),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    delete_time timestamp without time zone,
    reservation_id_to_create bigint NOT NULL,
    reservation_id_to_start bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_instance_group OWNER TO vhut;

--
-- Name: application_instance_group_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_instance_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_instance_group_id_seq OWNER TO vhut;

--
-- Name: application_instance_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_instance_group_id_seq OWNED BY application_instance_group.id;


--
-- Name: application_instance_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_instance_group_id_seq', 1, false);


--
-- Name: application_instance_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_instance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_instance_id_seq OWNER TO vhut;

--
-- Name: application_instance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_instance_id_seq OWNED BY application_instance.id;


--
-- Name: application_instance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_instance_id_seq', 1, false);


--
-- Name: application_instance_security_group; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_instance_security_group (
    id bigint NOT NULL,
    application_instance_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    security_group_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_instance_security_group OWNER TO vhut;

--
-- Name: application_instance_security_group_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_instance_security_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_instance_security_group_id_seq OWNER TO vhut;

--
-- Name: application_instance_security_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_instance_security_group_id_seq OWNED BY application_instance_security_group.id;


--
-- Name: application_instance_security_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_instance_security_group_id_seq', 1, false);


--
-- Name: application_instance_vm; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_instance_vm (
    id bigint NOT NULL,
    application_instance_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(10485760),
    image_url character varying(256),
    vm_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_instance_vm OWNER TO vhut;

--
-- Name: application_instance_vm_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_instance_vm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_instance_vm_id_seq OWNER TO vhut;

--
-- Name: application_instance_vm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_instance_vm_id_seq OWNED BY application_instance_vm.id;


--
-- Name: application_instance_vm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_instance_vm_id_seq', 1, false);


--
-- Name: application_instance_vm_security_group_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_instance_vm_security_group_map (
    id bigint NOT NULL,
    application_instance_vm_id bigint NOT NULL,
    application_instance_security_group_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_instance_vm_security_group_map OWNER TO vhut;

--
-- Name: application_instance_vm_security_group_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_instance_vm_security_group_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_instance_vm_security_group_map_id_seq OWNER TO vhut;

--
-- Name: application_instance_vm_security_group_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_instance_vm_security_group_map_id_seq OWNED BY application_instance_vm_security_group_map.id;


--
-- Name: application_instance_vm_security_group_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_instance_vm_security_group_map_id_seq', 1, false);


--
-- Name: application_security_group; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_security_group (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    security_group_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_security_group OWNER TO vhut;

--
-- Name: application_security_group_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_security_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_security_group_id_seq OWNER TO vhut;

--
-- Name: application_security_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_security_group_id_seq OWNED BY application_security_group.id;


--
-- Name: application_security_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_security_group_id_seq', 1, false);


--
-- Name: application_vm; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_vm (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(10485760),
    image_url character varying(256),
    vm_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_vm OWNER TO vhut;

--
-- Name: application_vm_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_vm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_vm_id_seq OWNER TO vhut;

--
-- Name: application_vm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_vm_id_seq OWNED BY application_vm.id;


--
-- Name: application_vm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_vm_id_seq', 1, false);


--
-- Name: application_vm_security_group_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE application_vm_security_group_map (
    id bigint NOT NULL,
    application_vm_id bigint NOT NULL,
    application_security_group_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.application_vm_security_group_map OWNER TO vhut;

--
-- Name: application_vm_security_group_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE application_vm_security_group_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.application_vm_security_group_map_id_seq OWNER TO vhut;

--
-- Name: application_vm_security_group_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE application_vm_security_group_map_id_seq OWNED BY application_vm_security_group_map.id;


--
-- Name: application_vm_security_group_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('application_vm_security_group_map_id_seq', 1, false);


--
-- Name: base_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE base_template (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(10485760),
    cloud_id bigint NOT NULL,
    image_url character varying(256),
    template_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.base_template OWNER TO vhut;

--
-- Name: base_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE base_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.base_template_id_seq OWNER TO vhut;

--
-- Name: base_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE base_template_id_seq OWNED BY base_template.id;


--
-- Name: base_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('base_template_id_seq', 1, false);


--
-- Name: cloud; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cloud (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    type integer,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.cloud OWNER TO vhut;

--
-- Name: cloud_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cloud_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cloud_id_seq OWNER TO vhut;

--
-- Name: cloud_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cloud_id_seq OWNED BY cloud.id;


--
-- Name: cloud_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cloud_id_seq', 1, false);


--
-- Name: cloud_user; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cloud_user (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    account character varying(256),
    first_name character varying(256),
    last_name character varying(256),
    email character varying(256),
    conflict_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.cloud_user OWNER TO vhut;

--
-- Name: cloud_user_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cloud_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cloud_user_id_seq OWNER TO vhut;

--
-- Name: cloud_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cloud_user_id_seq OWNED BY cloud_user.id;


--
-- Name: cloud_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cloud_user_id_seq', 1, false);


--
-- Name: cluster; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cluster (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    conflict_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.cluster OWNER TO vhut;

--
-- Name: cluster_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cluster_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cluster_id_seq OWNER TO vhut;

--
-- Name: cluster_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cluster_id_seq OWNED BY cluster.id;


--
-- Name: cluster_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cluster_id_seq', 1, false);


--
-- Name: cluster_reservation; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cluster_reservation (
    id bigint NOT NULL,
    cluster_id bigint NOT NULL,
    cpu_core integer NOT NULL,
    memory integer NOT NULL,
    reservation_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.cluster_reservation OWNER TO vhut;

--
-- Name: cluster_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cluster_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cluster_reservation_id_seq OWNER TO vhut;

--
-- Name: cluster_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cluster_reservation_id_seq OWNED BY cluster_reservation.id;


--
-- Name: cluster_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cluster_reservation_id_seq', 1, false);


--
-- Name: cluster_reservation_vm_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cluster_reservation_vm_map (
    id bigint NOT NULL,
    cluster_reservation_id bigint NOT NULL,
    vm_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.cluster_reservation_vm_map OWNER TO vhut;

--
-- Name: cluster_reservation_vm_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cluster_reservation_vm_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cluster_reservation_vm_map_id_seq OWNER TO vhut;

--
-- Name: cluster_reservation_vm_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cluster_reservation_vm_map_id_seq OWNED BY cluster_reservation_vm_map.id;


--
-- Name: cluster_reservation_vm_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cluster_reservation_vm_map_id_seq', 1, false);


--
-- Name: cluster_resource; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE cluster_resource (
    id bigint NOT NULL,
    cluster_id bigint NOT NULL,
    "time" timestamp without time zone NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    cpu_core_max integer NOT NULL,
    cpu_core_terminably_used integer NOT NULL,
    memory_max integer NOT NULL,
    memory_terminably_used integer NOT NULL
);


ALTER TABLE public.cluster_resource OWNER TO vhut;

--
-- Name: cluster_resource_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE cluster_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.cluster_resource_id_seq OWNER TO vhut;

--
-- Name: cluster_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE cluster_resource_id_seq OWNED BY cluster_resource.id;


--
-- Name: cluster_resource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('cluster_resource_id_seq', 1, false);


--
-- Name: command; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE command (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    operation integer,
    parameter bytea,
    result bytea,
    status integer,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    error_message character varying(10485760),
    depending_command_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.command OWNER TO vhut;

--
-- Name: command_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE command_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.command_id_seq OWNER TO vhut;

--
-- Name: command_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE command_id_seq OWNED BY command.id;


--
-- Name: command_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('command_id_seq', 1, false);


--
-- Name: command_template_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE command_template_map (
    id bigint NOT NULL,
    command_id bigint NOT NULL,
    template_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.command_template_map OWNER TO vhut;

--
-- Name: command_template_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE command_template_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.command_template_map_id_seq OWNER TO vhut;

--
-- Name: command_template_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE command_template_map_id_seq OWNED BY command_template_map.id;


--
-- Name: command_template_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('command_template_map_id_seq', 1, false);


--
-- Name: command_vm_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE command_vm_map (
    id bigint NOT NULL,
    command_id bigint NOT NULL,
    vm_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.command_vm_map OWNER TO vhut;

--
-- Name: command_vm_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE command_vm_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.command_vm_map_id_seq OWNER TO vhut;

--
-- Name: command_vm_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE command_vm_map_id_seq OWNED BY command_vm_map.id;


--
-- Name: command_vm_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('command_vm_map_id_seq', 1, false);


--
-- Name: conflict; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE conflict (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    status integer,
    detail character varying(10485760),
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.conflict OWNER TO vhut;

--
-- Name: conflict_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE conflict_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.conflict_id_seq OWNER TO vhut;

--
-- Name: conflict_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE conflict_id_seq OWNED BY conflict.id;


--
-- Name: conflict_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('conflict_id_seq', 1, false);


--
-- Name: disk; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE disk (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    vm_id bigint NOT NULL,
    size integer NOT NULL,
    storage_id bigint,
    disk_template_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.disk OWNER TO vhut;

--
-- Name: disk_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE disk_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.disk_id_seq OWNER TO vhut;

--
-- Name: disk_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE disk_id_seq OWNED BY disk.id;


--
-- Name: disk_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('disk_id_seq', 1, false);


--
-- Name: disk_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE disk_template (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    template_id bigint NOT NULL,
    size integer NOT NULL,
    storage_id bigint,
    storage_reservation_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.disk_template OWNER TO vhut;

--
-- Name: disk_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE disk_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.disk_template_id_seq OWNER TO vhut;

--
-- Name: disk_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE disk_template_id_seq OWNED BY disk_template.id;


--
-- Name: disk_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('disk_template_id_seq', 1, false);


--
-- Name: host; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE host (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    cluster_id bigint,
    cpu_core integer,
    memory integer,
    status integer,
    conflict_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.host OWNER TO vhut;

--
-- Name: host_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE host_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.host_id_seq OWNER TO vhut;

--
-- Name: host_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE host_id_seq OWNED BY host.id;


--
-- Name: host_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('host_id_seq', 1, false);


--
-- Name: local_id; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE local_id (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    type integer,
    global_id bigint NOT NULL,
    local_id character varying(256) NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.local_id OWNER TO vhut;

--
-- Name: local_id_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE local_id_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.local_id_id_seq OWNER TO vhut;

--
-- Name: local_id_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE local_id_id_seq OWNED BY local_id.id;


--
-- Name: local_id_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('local_id_id_seq', 1, false);


--
-- Name: network; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE network (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    vlan smallint,
    network_address character varying(8),
    mask character varying(8),
    gateway character varying(8),
    dns character varying(8),
    broadcast character varying(8),
    dhcp character varying(8),
    status integer,
    conflict_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.network OWNER TO vhut;

--
-- Name: network_adapter; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE network_adapter (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    vm_id bigint NOT NULL,
    security_group_id bigint NOT NULL,
    public_ip character varying(8),
    private_ip character varying(8),
    mac character varying(12),
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.network_adapter OWNER TO vhut;

--
-- Name: network_adapter_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE network_adapter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.network_adapter_id_seq OWNER TO vhut;

--
-- Name: network_adapter_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE network_adapter_id_seq OWNED BY network_adapter.id;


--
-- Name: network_adapter_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('network_adapter_id_seq', 1, false);


--
-- Name: network_adapter_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE network_adapter_template (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    template_id bigint NOT NULL,
    security_group_template_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    mac character varying(12)
);


ALTER TABLE public.network_adapter_template OWNER TO vhut;

--
-- Name: network_adapter_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE network_adapter_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.network_adapter_template_id_seq OWNER TO vhut;

--
-- Name: network_adapter_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE network_adapter_template_id_seq OWNED BY network_adapter_template.id;


--
-- Name: network_adapter_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('network_adapter_template_id_seq', 1, false);


--
-- Name: network_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE network_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.network_id_seq OWNER TO vhut;

--
-- Name: network_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE network_id_seq OWNED BY network.id;


--
-- Name: network_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('network_id_seq', 1, false);


--
-- Name: public_ip_reservation; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE public_ip_reservation (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    public_ip character varying(8) NOT NULL,
    reservation_id bigint NOT NULL,
    network_adapter_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.public_ip_reservation OWNER TO vhut;

--
-- Name: public_ip_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE public_ip_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.public_ip_reservation_id_seq OWNER TO vhut;

--
-- Name: public_ip_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE public_ip_reservation_id_seq OWNED BY public_ip_reservation.id;


--
-- Name: public_ip_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('public_ip_reservation_id_seq', 1, false);


--
-- Name: public_ip_resource; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE public_ip_resource (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    "time" timestamp without time zone NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    public_ip_max integer NOT NULL,
    public_ip_terminably_used integer NOT NULL
);


ALTER TABLE public.public_ip_resource OWNER TO vhut;

--
-- Name: released_application; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE released_application (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    created_time timestamp without time zone NOT NULL,
    structure character varying(10485760),
    reservation_id bigint NOT NULL,
    status integer,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.released_application OWNER TO vhut;

--
-- Name: released_application_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE released_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.released_application_id_seq OWNER TO vhut;

--
-- Name: released_application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE released_application_id_seq OWNED BY released_application.id;


--
-- Name: released_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('released_application_id_seq', 1, false);


--
-- Name: released_application_security_group_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE released_application_security_group_template (
    id bigint NOT NULL,
    released_application_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    security_group_template_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.released_application_security_group_template OWNER TO vhut;

--
-- Name: released_application_security_group_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE released_application_security_group_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.released_application_security_group_template_id_seq OWNER TO vhut;

--
-- Name: released_application_security_group_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE released_application_security_group_template_id_seq OWNED BY released_application_security_group_template.id;


--
-- Name: released_application_security_group_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('released_application_security_group_template_id_seq', 1, false);


--
-- Name: released_application_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE released_application_template (
    id bigint NOT NULL,
    released_application_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(10485760),
    image_url character varying(256),
    template_id bigint,
    cloud_id bigint NOT NULL,
    private_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.released_application_template OWNER TO vhut;

--
-- Name: released_application_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE released_application_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.released_application_template_id_seq OWNER TO vhut;

--
-- Name: released_application_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE released_application_template_id_seq OWNED BY released_application_template.id;


--
-- Name: released_application_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('released_application_template_id_seq', 1, false);


--
-- Name: released_application_template_security_group_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE released_application_template_security_group_map (
    id bigint NOT NULL,
    released_application_template_id bigint NOT NULL,
    released_application_security_group_template_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.released_application_template_security_group_map OWNER TO vhut;

--
-- Name: released_application_template_security_group_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE released_application_template_security_group_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.released_application_template_security_group_map_id_seq OWNER TO vhut;

--
-- Name: released_application_template_security_group_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE released_application_template_security_group_map_id_seq OWNED BY released_application_template_security_group_map.id;


--
-- Name: released_application_template_security_group_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('released_application_template_security_group_map_id_seq', 1, false);


--
-- Name: reservation; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE reservation (
    id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.reservation OWNER TO vhut;

--
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.reservation_id_seq OWNER TO vhut;

--
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE reservation_id_seq OWNED BY reservation.id;


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('reservation_id_seq', 1, false);


--
-- Name: role; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE role (
    id bigint NOT NULL,
    name character varying(256),
    rights bytea NOT NULL,
    is_default boolean,
    sys_lock boolean,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.role OWNER TO vhut;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO vhut;

--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE role_id_seq OWNED BY role.id;


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('role_id_seq', 1, false);


--
-- Name: schema_info; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE schema_info (
    version integer
);


ALTER TABLE public.schema_info OWNER TO vhut;

--
-- Name: security_group; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE security_group (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    network_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.security_group OWNER TO vhut;

--
-- Name: security_group_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE security_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.security_group_id_seq OWNER TO vhut;

--
-- Name: security_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE security_group_id_seq OWNED BY security_group.id;


--
-- Name: security_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('security_group_id_seq', 1, false);


--
-- Name: security_group_template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE security_group_template (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.security_group_template OWNER TO vhut;

--
-- Name: security_group_template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE security_group_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.security_group_template_id_seq OWNER TO vhut;

--
-- Name: security_group_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE security_group_template_id_seq OWNED BY security_group_template.id;


--
-- Name: security_group_template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('security_group_template_id_seq', 1, false);


--
-- Name: storage; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE storage (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    conflict_id bigint,
    available_size integer,
    commited_size integer,
    physical_size integer,
    status integer,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.storage OWNER TO vhut;

--
-- Name: storage_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE storage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.storage_id_seq OWNER TO vhut;

--
-- Name: storage_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE storage_id_seq OWNED BY storage.id;


--
-- Name: storage_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('storage_id_seq', 1, false);


--
-- Name: storage_reservation; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE storage_reservation (
    id bigint NOT NULL,
    storage_id bigint NOT NULL,
    size integer NOT NULL,
    reservation_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.storage_reservation OWNER TO vhut;

--
-- Name: storage_reservation_disk_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE storage_reservation_disk_map (
    id bigint NOT NULL,
    storage_reservation_id bigint NOT NULL,
    disk_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.storage_reservation_disk_map OWNER TO vhut;

--
-- Name: storage_reservation_disk_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE storage_reservation_disk_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.storage_reservation_disk_map_id_seq OWNER TO vhut;

--
-- Name: storage_reservation_disk_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE storage_reservation_disk_map_id_seq OWNED BY storage_reservation_disk_map.id;


--
-- Name: storage_reservation_disk_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('storage_reservation_disk_map_id_seq', 1, false);


--
-- Name: storage_reservation_disk_template_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE storage_reservation_disk_template_map (
    id bigint NOT NULL,
    storage_reservation_id bigint NOT NULL,
    disk_template_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.storage_reservation_disk_template_map OWNER TO vhut;

--
-- Name: storage_reservation_disk_template_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE storage_reservation_disk_template_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.storage_reservation_disk_template_map_id_seq OWNER TO vhut;

--
-- Name: storage_reservation_disk_template_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE storage_reservation_disk_template_map_id_seq OWNED BY storage_reservation_disk_template_map.id;


--
-- Name: storage_reservation_disk_template_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('storage_reservation_disk_template_map_id_seq', 1, false);


--
-- Name: storage_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE storage_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.storage_reservation_id_seq OWNER TO vhut;

--
-- Name: storage_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE storage_reservation_id_seq OWNED BY storage_reservation.id;


--
-- Name: storage_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('storage_reservation_id_seq', 1, false);


--
-- Name: storage_resource; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE storage_resource (
    id bigint NOT NULL,
    storage_id bigint NOT NULL,
    "time" timestamp without time zone NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    storage_max integer NOT NULL,
    storage_terminably_used integer NOT NULL
);


ALTER TABLE public.storage_resource OWNER TO vhut;

--
-- Name: storage_resource_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE storage_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.storage_resource_id_seq OWNER TO vhut;

--
-- Name: storage_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE storage_resource_id_seq OWNED BY storage_resource.id;


--
-- Name: storage_resource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('storage_resource_id_seq', 1, false);


--
-- Name: template; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE template (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    description character varying(10485760),
    status integer,
    spec_id bigint,
    cpu_core integer,
    memory integer,
    os integer,
    conflict_id bigint,
    cluster_id bigint,
    storage_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.template OWNER TO vhut;

--
-- Name: template_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.template_id_seq OWNER TO vhut;

--
-- Name: template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE template_id_seq OWNED BY template.id;


--
-- Name: template_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('template_id_seq', 1, false);


--
-- Name: term; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE term (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    reservation_id bigint,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.term OWNER TO vhut;

--
-- Name: term_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE term_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.term_id_seq OWNER TO vhut;

--
-- Name: term_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE term_id_seq OWNED BY term.id;


--
-- Name: term_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('term_id_seq', 1, false);


--
-- Name: vhut_user; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vhut_user (
    id bigint NOT NULL,
    account character varying(256),
    first_name character varying(256),
    last_name character varying(256),
    email character varying(256),
    image_url character varying(256),
    sys_lock boolean,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vhut_user OWNER TO vhut;

--
-- Name: vhut_user_cloud_user_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vhut_user_cloud_user_map (
    id bigint NOT NULL,
    cloud_user_id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    vhut_user_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vhut_user_cloud_user_map OWNER TO vhut;

--
-- Name: vhut_user_cloud_user_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vhut_user_cloud_user_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vhut_user_cloud_user_map_id_seq OWNER TO vhut;

--
-- Name: vhut_user_cloud_user_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vhut_user_cloud_user_map_id_seq OWNED BY vhut_user_cloud_user_map.id;


--
-- Name: vhut_user_cloud_user_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vhut_user_cloud_user_map_id_seq', 1, false);


--
-- Name: vhut_user_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vhut_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vhut_user_id_seq OWNER TO vhut;

--
-- Name: vhut_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vhut_user_id_seq OWNED BY vhut_user.id;


--
-- Name: vhut_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vhut_user_id_seq', 1, false);


--
-- Name: vhut_user_role_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vhut_user_role_map (
    id bigint NOT NULL,
    vhut_user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vhut_user_role_map OWNER TO vhut;

--
-- Name: vhut_user_role_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vhut_user_role_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vhut_user_role_map_id_seq OWNER TO vhut;

--
-- Name: vhut_user_role_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vhut_user_role_map_id_seq OWNED BY vhut_user_role_map.id;


--
-- Name: vhut_user_role_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vhut_user_role_map_id_seq', 1, false);


--
-- Name: vlan_reservation; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vlan_reservation (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    reservation_id bigint NOT NULL,
    security_group_id bigint,
    network_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vlan_reservation OWNER TO vhut;

--
-- Name: vlan_reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vlan_reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vlan_reservation_id_seq OWNER TO vhut;

--
-- Name: vlan_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vlan_reservation_id_seq OWNED BY vlan_reservation.id;


--
-- Name: vlan_reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vlan_reservation_id_seq', 1, false);


--
-- Name: vlan_resource; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vlan_resource (
    id bigint NOT NULL,
    cloud_id bigint,
    "time" timestamp without time zone NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    vlan_max integer NOT NULL,
    vlan_terminably_used integer NOT NULL
);


ALTER TABLE public.vlan_resource OWNER TO vhut;

--
-- Name: vlan_resource_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vlan_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vlan_resource_id_seq OWNER TO vhut;

--
-- Name: vlan_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vlan_resource_id_seq OWNED BY vlan_resource.id;


--
-- Name: vlan_resource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vlan_resource_id_seq', 1, false);


--
-- Name: vm; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vm (
    id bigint NOT NULL,
    cloud_id bigint NOT NULL,
    name character varying(256),
    description character varying(10485760),
    status integer,
    spec_id bigint,
    cpu_core integer,
    memory integer,
    version bigint DEFAULT 0 NOT NULL,
    cpu_usage integer,
    memory_usage integer,
    os integer,
    template_id bigint NOT NULL,
    conflict_id bigint,
    cluster_id bigint,
    host_id bigint,
    storage_id bigint
);


ALTER TABLE public.vm OWNER TO vhut;

--
-- Name: vm_cloud_user_map; Type: TABLE; Schema: public; Owner: vhut; Tablespace: 
--

CREATE TABLE vm_cloud_user_map (
    id bigint NOT NULL,
    vm_id bigint NOT NULL,
    cloud_user_id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.vm_cloud_user_map OWNER TO vhut;

--
-- Name: vm_cloud_user_map_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vm_cloud_user_map_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vm_cloud_user_map_id_seq OWNER TO vhut;

--
-- Name: vm_cloud_user_map_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vm_cloud_user_map_id_seq OWNED BY vm_cloud_user_map.id;


--
-- Name: vm_cloud_user_map_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vm_cloud_user_map_id_seq', 1, false);


--
-- Name: vm_id_seq; Type: SEQUENCE; Schema: public; Owner: vhut
--

CREATE SEQUENCE vm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.vm_id_seq OWNER TO vhut;

--
-- Name: vm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vhut
--

ALTER SEQUENCE vm_id_seq OWNED BY vm.id;


--
-- Name: vm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: vhut
--

SELECT pg_catalog.setval('vm_id_seq', 1, false);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application ALTER COLUMN id SET DEFAULT nextval('application_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_instance ALTER COLUMN id SET DEFAULT nextval('application_instance_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_instance_group ALTER COLUMN id SET DEFAULT nextval('application_instance_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_instance_security_group ALTER COLUMN id SET DEFAULT nextval('application_instance_security_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_instance_vm ALTER COLUMN id SET DEFAULT nextval('application_instance_vm_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_instance_vm_security_group_map ALTER COLUMN id SET DEFAULT nextval('application_instance_vm_security_group_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_security_group ALTER COLUMN id SET DEFAULT nextval('application_security_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_vm ALTER COLUMN id SET DEFAULT nextval('application_vm_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE application_vm_security_group_map ALTER COLUMN id SET DEFAULT nextval('application_vm_security_group_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE base_template ALTER COLUMN id SET DEFAULT nextval('base_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cloud ALTER COLUMN id SET DEFAULT nextval('cloud_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cloud_user ALTER COLUMN id SET DEFAULT nextval('cloud_user_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cluster ALTER COLUMN id SET DEFAULT nextval('cluster_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cluster_reservation ALTER COLUMN id SET DEFAULT nextval('cluster_reservation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cluster_reservation_vm_map ALTER COLUMN id SET DEFAULT nextval('cluster_reservation_vm_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE cluster_resource ALTER COLUMN id SET DEFAULT nextval('cluster_resource_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE command ALTER COLUMN id SET DEFAULT nextval('command_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE command_template_map ALTER COLUMN id SET DEFAULT nextval('command_template_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE command_vm_map ALTER COLUMN id SET DEFAULT nextval('command_vm_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE conflict ALTER COLUMN id SET DEFAULT nextval('conflict_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE disk ALTER COLUMN id SET DEFAULT nextval('disk_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE disk_template ALTER COLUMN id SET DEFAULT nextval('disk_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE host ALTER COLUMN id SET DEFAULT nextval('host_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE local_id ALTER COLUMN id SET DEFAULT nextval('local_id_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE network ALTER COLUMN id SET DEFAULT nextval('network_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE network_adapter ALTER COLUMN id SET DEFAULT nextval('network_adapter_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE network_adapter_template ALTER COLUMN id SET DEFAULT nextval('network_adapter_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE public_ip_reservation ALTER COLUMN id SET DEFAULT nextval('public_ip_reservation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE released_application ALTER COLUMN id SET DEFAULT nextval('released_application_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE released_application_security_group_template ALTER COLUMN id SET DEFAULT nextval('released_application_security_group_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE released_application_template ALTER COLUMN id SET DEFAULT nextval('released_application_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE released_application_template_security_group_map ALTER COLUMN id SET DEFAULT nextval('released_application_template_security_group_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE reservation ALTER COLUMN id SET DEFAULT nextval('reservation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE role ALTER COLUMN id SET DEFAULT nextval('role_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE security_group ALTER COLUMN id SET DEFAULT nextval('security_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE security_group_template ALTER COLUMN id SET DEFAULT nextval('security_group_template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE storage ALTER COLUMN id SET DEFAULT nextval('storage_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE storage_reservation ALTER COLUMN id SET DEFAULT nextval('storage_reservation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE storage_reservation_disk_map ALTER COLUMN id SET DEFAULT nextval('storage_reservation_disk_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE storage_reservation_disk_template_map ALTER COLUMN id SET DEFAULT nextval('storage_reservation_disk_template_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE storage_resource ALTER COLUMN id SET DEFAULT nextval('storage_resource_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE template ALTER COLUMN id SET DEFAULT nextval('template_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE term ALTER COLUMN id SET DEFAULT nextval('term_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vhut_user ALTER COLUMN id SET DEFAULT nextval('vhut_user_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vhut_user_cloud_user_map ALTER COLUMN id SET DEFAULT nextval('vhut_user_cloud_user_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vhut_user_role_map ALTER COLUMN id SET DEFAULT nextval('vhut_user_role_map_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vlan_reservation ALTER COLUMN id SET DEFAULT nextval('vlan_reservation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vlan_resource ALTER COLUMN id SET DEFAULT nextval('vlan_resource_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vm ALTER COLUMN id SET DEFAULT nextval('vm_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vhut
--

ALTER TABLE vm_cloud_user_map ALTER COLUMN id SET DEFAULT nextval('vm_cloud_user_map_id_seq'::regclass);


--
-- Data for Name: application; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application (id, name, vhut_user_id, image_url, structure, reservation_id, status, version) FROM stdin;
\.


--
-- Data for Name: application_instance; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_instance (id, vhut_user_id, status, application_instance_group_id, released_application_id, version) FROM stdin;
\.


--
-- Data for Name: application_instance_group; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_instance_group (id, vhut_user_id, name, application_id, password, start_time, end_time, delete_time, reservation_id_to_create, reservation_id_to_start, version) FROM stdin;
\.


--
-- Data for Name: application_instance_security_group; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_instance_security_group (id, application_instance_id, name, security_group_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: application_instance_vm; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_instance_vm (id, application_instance_id, name, description, image_url, vm_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: application_instance_vm_security_group_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_instance_vm_security_group_map (id, application_instance_vm_id, application_instance_security_group_id, version) FROM stdin;
\.


--
-- Data for Name: application_security_group; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_security_group (id, application_id, name, security_group_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: application_vm; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_vm (id, application_id, name, description, image_url, vm_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: application_vm_security_group_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY application_vm_security_group_map (id, application_vm_id, application_security_group_id, version) FROM stdin;
\.


--
-- Data for Name: base_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY base_template (id, name, description, cloud_id, image_url, template_id, version) FROM stdin;
\.


--
-- Data for Name: cloud; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cloud (id, name, type, version) FROM stdin;
\.


--
-- Data for Name: cloud_user; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cloud_user (id, cloud_id, account, first_name, last_name, email, conflict_id, version) FROM stdin;
\.


--
-- Data for Name: cluster; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cluster (id, cloud_id, name, conflict_id, version) FROM stdin;
\.


--
-- Data for Name: cluster_reservation; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cluster_reservation (id, cluster_id, cpu_core, memory, reservation_id, version) FROM stdin;
\.


--
-- Data for Name: cluster_reservation_vm_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cluster_reservation_vm_map (id, cluster_reservation_id, vm_id, version) FROM stdin;
\.


--
-- Data for Name: cluster_resource; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY cluster_resource (id, cluster_id, "time", version, cpu_core_max, cpu_core_terminably_used, memory_max, memory_terminably_used) FROM stdin;
\.


--
-- Data for Name: command; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY command (id, cloud_id, operation, parameter, result, status, start_time, end_time, error_message, depending_command_id, version) FROM stdin;
\.


--
-- Data for Name: command_template_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY command_template_map (id, command_id, template_id, version) FROM stdin;
\.


--
-- Data for Name: command_vm_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY command_vm_map (id, command_id, vm_id, version) FROM stdin;
\.


--
-- Data for Name: conflict; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY conflict (id, cloud_id, status, detail, version) FROM stdin;
\.


--
-- Data for Name: disk; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY disk (id, cloud_id, name, vm_id, size, storage_id, disk_template_id, version) FROM stdin;
\.


--
-- Data for Name: disk_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY disk_template (id, cloud_id, name, template_id, size, storage_id, storage_reservation_id, version) FROM stdin;
\.


--
-- Data for Name: host; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY host (id, cloud_id, name, cluster_id, cpu_core, memory, status, conflict_id, version) FROM stdin;
\.


--
-- Data for Name: local_id; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY local_id (id, cloud_id, type, global_id, local_id, version) FROM stdin;
\.


--
-- Data for Name: network; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY network (id, cloud_id, name, vlan, network_address, mask, gateway, dns, broadcast, dhcp, status, conflict_id, version) FROM stdin;
\.


--
-- Data for Name: network_adapter; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY network_adapter (id, cloud_id, name, vm_id, security_group_id, public_ip, private_ip, mac, version) FROM stdin;
\.


--
-- Data for Name: network_adapter_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY network_adapter_template (id, cloud_id, name, template_id, security_group_template_id, version, mac) FROM stdin;
\.


--
-- Data for Name: public_ip_reservation; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY public_ip_reservation (id, cloud_id, public_ip, reservation_id, network_adapter_id, version) FROM stdin;
\.


--
-- Data for Name: public_ip_resource; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY public_ip_resource (id, cloud_id, "time", version, public_ip_max, public_ip_terminably_used) FROM stdin;
\.


--
-- Data for Name: released_application; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY released_application (id, application_id, created_time, structure, reservation_id, status, version) FROM stdin;
\.


--
-- Data for Name: released_application_security_group_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY released_application_security_group_template (id, released_application_id, name, security_group_template_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: released_application_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY released_application_template (id, released_application_id, name, description, image_url, template_id, cloud_id, private_id, version) FROM stdin;
\.


--
-- Data for Name: released_application_template_security_group_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY released_application_template_security_group_map (id, released_application_template_id, released_application_security_group_template_id, version) FROM stdin;
\.


--
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY reservation (id, start_time, end_time, version) FROM stdin;
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY role (id, name, rights, is_default, sys_lock, version) FROM stdin;
\.


--
-- Data for Name: schema_info; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY schema_info (version) FROM stdin;
36
\.


--
-- Data for Name: security_group; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY security_group (id, cloud_id, name, network_id, version) FROM stdin;
\.


--
-- Data for Name: security_group_template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY security_group_template (id, cloud_id, name, version) FROM stdin;
\.


--
-- Data for Name: storage; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY storage (id, cloud_id, name, conflict_id, available_size, commited_size, physical_size, status, version) FROM stdin;
\.


--
-- Data for Name: storage_reservation; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY storage_reservation (id, storage_id, size, reservation_id, version) FROM stdin;
\.


--
-- Data for Name: storage_reservation_disk_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY storage_reservation_disk_map (id, storage_reservation_id, disk_id, version) FROM stdin;
\.


--
-- Data for Name: storage_reservation_disk_template_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY storage_reservation_disk_template_map (id, storage_reservation_id, disk_template_id, version) FROM stdin;
\.


--
-- Data for Name: storage_resource; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY storage_resource (id, storage_id, "time", version, storage_max, storage_terminably_used) FROM stdin;
\.


--
-- Data for Name: template; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY template (id, cloud_id, name, description, status, spec_id, cpu_core, memory, os, conflict_id, cluster_id, storage_id, version) FROM stdin;
\.


--
-- Data for Name: term; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY term (id, application_id, start_time, end_time, reservation_id, version) FROM stdin;
\.


--
-- Data for Name: vhut_user; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vhut_user (id, account, first_name, last_name, email, image_url, sys_lock, version) FROM stdin;
\.


--
-- Data for Name: vhut_user_cloud_user_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vhut_user_cloud_user_map (id, cloud_user_id, cloud_id, vhut_user_id, version) FROM stdin;
\.


--
-- Data for Name: vhut_user_role_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vhut_user_role_map (id, vhut_user_id, role_id, version) FROM stdin;
\.


--
-- Data for Name: vlan_reservation; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vlan_reservation (id, cloud_id, reservation_id, security_group_id, network_id, version) FROM stdin;
\.


--
-- Data for Name: vlan_resource; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vlan_resource (id, cloud_id, "time", version, vlan_max, vlan_terminably_used) FROM stdin;
\.


--
-- Data for Name: vm; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vm (id, cloud_id, name, description, status, spec_id, cpu_core, memory, version, cpu_usage, memory_usage, os, template_id, conflict_id, cluster_id, host_id, storage_id) FROM stdin;
\.


--
-- Data for Name: vm_cloud_user_map; Type: TABLE DATA; Schema: public; Owner: vhut
--

COPY vm_cloud_user_map (id, vm_id, cloud_user_id, version) FROM stdin;
\.


--
-- Name: application_instance_group_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_instance_group
    ADD CONSTRAINT application_instance_group_pk PRIMARY KEY (id);


--
-- Name: application_instance_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_instance
    ADD CONSTRAINT application_instance_pk PRIMARY KEY (id);


--
-- Name: application_instance_security_group_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_instance_security_group
    ADD CONSTRAINT application_instance_security_group_pk PRIMARY KEY (id);


--
-- Name: application_instance_vm_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_instance_vm
    ADD CONSTRAINT application_instance_vm_pk PRIMARY KEY (id);


--
-- Name: application_instance_vm_security_group_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_instance_vm_security_group_map
    ADD CONSTRAINT application_instance_vm_security_group_map_pk PRIMARY KEY (id);


--
-- Name: application_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application
    ADD CONSTRAINT application_pk PRIMARY KEY (id);


--
-- Name: application_security_group_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_security_group
    ADD CONSTRAINT application_security_group_pk PRIMARY KEY (id);


--
-- Name: application_vm_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_vm
    ADD CONSTRAINT application_vm_pk PRIMARY KEY (id);


--
-- Name: application_vm_security_group_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY application_vm_security_group_map
    ADD CONSTRAINT application_vm_security_group_map_pk PRIMARY KEY (id);


--
-- Name: base_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY base_template
    ADD CONSTRAINT base_template_pk PRIMARY KEY (id);


--
-- Name: cloud_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cloud
    ADD CONSTRAINT cloud_pk PRIMARY KEY (id);


--
-- Name: cloud_user_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cloud_user
    ADD CONSTRAINT cloud_user_pk PRIMARY KEY (id);


--
-- Name: cluster_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster
    ADD CONSTRAINT cluster_pk PRIMARY KEY (id);


--
-- Name: cluster_reservation_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_reservation
    ADD CONSTRAINT cluster_reservation_pk PRIMARY KEY (id, cluster_id);


--
-- Name: cluster_reservation_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_reservation
    ADD CONSTRAINT cluster_reservation_uk1 UNIQUE (id);


--
-- Name: cluster_reservation_vm_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_reservation_vm_map
    ADD CONSTRAINT cluster_reservation_vm_map_pk PRIMARY KEY (id, cluster_reservation_id, vm_id);


--
-- Name: cluster_reservation_vm_map_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_reservation_vm_map
    ADD CONSTRAINT cluster_reservation_vm_map_uk1 UNIQUE (id);


--
-- Name: cluster_resource_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_resource
    ADD CONSTRAINT cluster_resource_pk PRIMARY KEY (id, cluster_id);


--
-- Name: cluster_resource_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY cluster_resource
    ADD CONSTRAINT cluster_resource_uk1 UNIQUE (id);


--
-- Name: command_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY command
    ADD CONSTRAINT command_pk PRIMARY KEY (id);


--
-- Name: command_template_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY command_template_map
    ADD CONSTRAINT command_template_map_pk PRIMARY KEY (id);


--
-- Name: command_vm_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY command_vm_map
    ADD CONSTRAINT command_vm_map_pk PRIMARY KEY (id);


--
-- Name: conflict_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY conflict
    ADD CONSTRAINT conflict_pk PRIMARY KEY (id);


--
-- Name: disk_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY disk
    ADD CONSTRAINT disk_pk PRIMARY KEY (id);


--
-- Name: disk_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY disk_template
    ADD CONSTRAINT disk_template_pk PRIMARY KEY (id);


--
-- Name: host_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY host
    ADD CONSTRAINT host_pk PRIMARY KEY (id);


--
-- Name: local_id_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY local_id
    ADD CONSTRAINT local_id_pk PRIMARY KEY (id);


--
-- Name: network_adapter_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY network_adapter
    ADD CONSTRAINT network_adapter_pk PRIMARY KEY (id);


--
-- Name: network_adapter_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY network_adapter_template
    ADD CONSTRAINT network_adapter_template_pk PRIMARY KEY (id);


--
-- Name: network_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY network
    ADD CONSTRAINT network_pk PRIMARY KEY (id);


--
-- Name: public_ip_reservation_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY public_ip_reservation
    ADD CONSTRAINT public_ip_reservation_pk PRIMARY KEY (id, cloud_id);


--
-- Name: public_ip_reservation_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY public_ip_reservation
    ADD CONSTRAINT public_ip_reservation_uk1 UNIQUE (id);


--
-- Name: public_ip_resource_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY public_ip_resource
    ADD CONSTRAINT public_ip_resource_pk PRIMARY KEY (id, cloud_id);


--
-- Name: public_ip_resource_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY public_ip_resource
    ADD CONSTRAINT public_ip_resource_uk1 UNIQUE (id);


--
-- Name: released_application_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY released_application
    ADD CONSTRAINT released_application_pk PRIMARY KEY (id);


--
-- Name: released_application_security_group_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY released_application_security_group_template
    ADD CONSTRAINT released_application_security_group_template_pk PRIMARY KEY (id);


--
-- Name: released_application_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY released_application_template
    ADD CONSTRAINT released_application_template_pk PRIMARY KEY (id);


--
-- Name: released_application_template_security_group_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY released_application_template_security_group_map
    ADD CONSTRAINT released_application_template_security_group_map_pk PRIMARY KEY (id);


--
-- Name: reservation_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY reservation
    ADD CONSTRAINT reservation_pk PRIMARY KEY (id);


--
-- Name: role_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pk PRIMARY KEY (id);


--
-- Name: security_group_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY security_group
    ADD CONSTRAINT security_group_pk PRIMARY KEY (id);


--
-- Name: security_group_template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY security_group_template
    ADD CONSTRAINT security_group_template_pk PRIMARY KEY (id);


--
-- Name: storage_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage
    ADD CONSTRAINT storage_pk PRIMARY KEY (id);


--
-- Name: storage_reservation_disk_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_reservation_disk_map
    ADD CONSTRAINT storage_reservation_disk_map_pk PRIMARY KEY (id);


--
-- Name: storage_reservation_disk_template_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_reservation_disk_template_map
    ADD CONSTRAINT storage_reservation_disk_template_map_pk PRIMARY KEY (id);


--
-- Name: storage_reservation_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_reservation
    ADD CONSTRAINT storage_reservation_pk PRIMARY KEY (id, storage_id);


--
-- Name: storage_reservation_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_reservation
    ADD CONSTRAINT storage_reservation_uk1 UNIQUE (id);


--
-- Name: storage_resource_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_resource
    ADD CONSTRAINT storage_resource_pk PRIMARY KEY (id, storage_id);


--
-- Name: storage_resource_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY storage_resource
    ADD CONSTRAINT storage_resource_uk1 UNIQUE (id);


--
-- Name: template_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY template
    ADD CONSTRAINT template_pk PRIMARY KEY (id);


--
-- Name: term_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY term
    ADD CONSTRAINT term_pk PRIMARY KEY (id);


--
-- Name: vhut_user_cloud_user_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vhut_user_cloud_user_map
    ADD CONSTRAINT vhut_user_cloud_user_map_pk PRIMARY KEY (id);


--
-- Name: vhut_user_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vhut_user
    ADD CONSTRAINT vhut_user_pk PRIMARY KEY (id);


--
-- Name: vhut_user_role_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vhut_user_role_map
    ADD CONSTRAINT vhut_user_role_map_pk PRIMARY KEY (id);


--
-- Name: vlan_reservation_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vlan_reservation
    ADD CONSTRAINT vlan_reservation_pk PRIMARY KEY (id, cloud_id);


--
-- Name: vlan_reservation_uk1; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vlan_reservation
    ADD CONSTRAINT vlan_reservation_uk1 UNIQUE (id);


--
-- Name: vlan_resource_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vlan_resource
    ADD CONSTRAINT vlan_resource_pk PRIMARY KEY (id);


--
-- Name: vm_cloud_user_map_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vm_cloud_user_map
    ADD CONSTRAINT vm_cloud_user_map_pk PRIMARY KEY (id);


--
-- Name: vm_pk; Type: CONSTRAINT; Schema: public; Owner: vhut; Tablespace: 
--

ALTER TABLE ONLY vm
    ADD CONSTRAINT vm_pk PRIMARY KEY (id);


--
-- Name: application_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application
    ADD CONSTRAINT application_fk1 FOREIGN KEY (vhut_user_id) REFERENCES vhut_user(id);


--
-- Name: application_instance_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance
    ADD CONSTRAINT application_instance_fk1 FOREIGN KEY (released_application_id) REFERENCES released_application(id);


--
-- Name: application_instance_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance
    ADD CONSTRAINT application_instance_fk2 FOREIGN KEY (application_instance_group_id) REFERENCES application_instance_group(id);


--
-- Name: application_instance_fk3; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance
    ADD CONSTRAINT application_instance_fk3 FOREIGN KEY (vhut_user_id) REFERENCES vhut_user(id);


--
-- Name: application_instance_group_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_group
    ADD CONSTRAINT application_instance_group_fk1 FOREIGN KEY (application_id) REFERENCES application(id);


--
-- Name: application_instance_group_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_group
    ADD CONSTRAINT application_instance_group_fk2 FOREIGN KEY (vhut_user_id) REFERENCES vhut_user(id);


--
-- Name: application_instance_security_group_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_security_group
    ADD CONSTRAINT application_instance_security_group_fk1 FOREIGN KEY (application_instance_id) REFERENCES application_instance(id);


--
-- Name: application_instance_vm_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_vm
    ADD CONSTRAINT application_instance_vm_fk1 FOREIGN KEY (application_instance_id) REFERENCES application_instance(id);


--
-- Name: application_instance_vm_security_group_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_vm_security_group_map
    ADD CONSTRAINT application_instance_vm_security_group_map_fk1 FOREIGN KEY (application_instance_vm_id) REFERENCES application_instance_vm(id);


--
-- Name: application_instance_vm_security_group_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_instance_vm_security_group_map
    ADD CONSTRAINT application_instance_vm_security_group_map_fk2 FOREIGN KEY (application_instance_security_group_id) REFERENCES application_instance_security_group(id);


--
-- Name: application_security_group_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_security_group
    ADD CONSTRAINT application_security_group_fk1 FOREIGN KEY (application_id) REFERENCES application(id);


--
-- Name: application_vm_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_vm
    ADD CONSTRAINT application_vm_fk1 FOREIGN KEY (application_id) REFERENCES application(id);


--
-- Name: application_vm_security_group_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_vm_security_group_map
    ADD CONSTRAINT application_vm_security_group_map_fk1 FOREIGN KEY (application_vm_id) REFERENCES application_vm(id);


--
-- Name: application_vm_security_group_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY application_vm_security_group_map
    ADD CONSTRAINT application_vm_security_group_map_fk2 FOREIGN KEY (application_security_group_id) REFERENCES application_security_group(id);


--
-- Name: cloud_user_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cloud_user
    ADD CONSTRAINT cloud_user_fk1 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: cluster_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster
    ADD CONSTRAINT cluster_fk1 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: cluster_reservation_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster_reservation
    ADD CONSTRAINT cluster_reservation_fk1 FOREIGN KEY (cluster_id) REFERENCES cluster(id);


--
-- Name: cluster_reservation_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster_reservation
    ADD CONSTRAINT cluster_reservation_fk2 FOREIGN KEY (reservation_id) REFERENCES reservation(id);


--
-- Name: cluster_reservation_vm_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster_reservation_vm_map
    ADD CONSTRAINT cluster_reservation_vm_map_fk1 FOREIGN KEY (cluster_reservation_id) REFERENCES cluster_reservation(id);


--
-- Name: cluster_reservation_vm_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster_reservation_vm_map
    ADD CONSTRAINT cluster_reservation_vm_map_fk2 FOREIGN KEY (vm_id) REFERENCES vm(id);


--
-- Name: cluster_resource_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY cluster_resource
    ADD CONSTRAINT cluster_resource_fk1 FOREIGN KEY (cluster_id) REFERENCES cluster(id);


--
-- Name: command_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY command
    ADD CONSTRAINT command_fk1 FOREIGN KEY (depending_command_id) REFERENCES command(id);


--
-- Name: command_template_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY command_template_map
    ADD CONSTRAINT command_template_map_fk1 FOREIGN KEY (command_id) REFERENCES command(id);


--
-- Name: command_template_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY command_template_map
    ADD CONSTRAINT command_template_map_fk2 FOREIGN KEY (template_id) REFERENCES template(id);


--
-- Name: command_vm_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY command_vm_map
    ADD CONSTRAINT command_vm_map_fk1 FOREIGN KEY (command_id) REFERENCES command(id);


--
-- Name: command_vm_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY command_vm_map
    ADD CONSTRAINT command_vm_map_fk2 FOREIGN KEY (vm_id) REFERENCES vm(id);


--
-- Name: disk_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY disk
    ADD CONSTRAINT disk_fk1 FOREIGN KEY (storage_id) REFERENCES storage(id);


--
-- Name: disk_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY disk
    ADD CONSTRAINT disk_fk2 FOREIGN KEY (disk_template_id) REFERENCES disk_template(id);


--
-- Name: disk_fk3; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY disk
    ADD CONSTRAINT disk_fk3 FOREIGN KEY (vm_id) REFERENCES vm(id);


--
-- Name: disk_template_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY disk_template
    ADD CONSTRAINT disk_template_fk1 FOREIGN KEY (storage_id) REFERENCES storage(id);


--
-- Name: disk_template_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY disk_template
    ADD CONSTRAINT disk_template_fk2 FOREIGN KEY (template_id) REFERENCES template(id);


--
-- Name: host_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY host
    ADD CONSTRAINT host_fk1 FOREIGN KEY (cluster_id) REFERENCES cluster(id);


--
-- Name: host_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY host
    ADD CONSTRAINT host_fk2 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: network_adapter_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY network_adapter
    ADD CONSTRAINT network_adapter_fk1 FOREIGN KEY (security_group_id) REFERENCES security_group(id);


--
-- Name: network_adapter_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY network_adapter
    ADD CONSTRAINT network_adapter_fk2 FOREIGN KEY (vm_id) REFERENCES vm(id);


--
-- Name: network_adapter_template_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY network_adapter_template
    ADD CONSTRAINT network_adapter_template_fk1 FOREIGN KEY (security_group_template_id) REFERENCES security_group_template(id);


--
-- Name: network_adapter_template_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY network_adapter_template
    ADD CONSTRAINT network_adapter_template_fk2 FOREIGN KEY (template_id) REFERENCES template(id);


--
-- Name: network_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY network
    ADD CONSTRAINT network_fk1 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: public_ip_reservation_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY public_ip_reservation
    ADD CONSTRAINT public_ip_reservation_fk1 FOREIGN KEY (network_adapter_id) REFERENCES network_adapter(id);


--
-- Name: public_ip_reservation_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY public_ip_reservation
    ADD CONSTRAINT public_ip_reservation_fk2 FOREIGN KEY (reservation_id) REFERENCES reservation(id);


--
-- Name: released_application_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY released_application
    ADD CONSTRAINT released_application_fk1 FOREIGN KEY (application_id) REFERENCES application(id);


--
-- Name: released_application_security_group_template_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY released_application_security_group_template
    ADD CONSTRAINT released_application_security_group_template_fk1 FOREIGN KEY (released_application_id) REFERENCES released_application(id);


--
-- Name: released_application_template_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY released_application_template
    ADD CONSTRAINT released_application_template_fk1 FOREIGN KEY (released_application_id) REFERENCES released_application(id);


--
-- Name: released_application_template_security_group_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY released_application_template_security_group_map
    ADD CONSTRAINT released_application_template_security_group_map_fk1 FOREIGN KEY (released_application_template_id) REFERENCES released_application_template(id);


--
-- Name: released_application_template_security_group_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY released_application_template_security_group_map
    ADD CONSTRAINT released_application_template_security_group_map_fk2 FOREIGN KEY (released_application_security_group_template_id) REFERENCES released_application_security_group_template(id);


--
-- Name: security_group_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY security_group
    ADD CONSTRAINT security_group_fk1 FOREIGN KEY (network_id) REFERENCES network(id);


--
-- Name: storage_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage
    ADD CONSTRAINT storage_fk1 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: storage_reservation_disk_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation_disk_map
    ADD CONSTRAINT storage_reservation_disk_map_fk1 FOREIGN KEY (storage_reservation_id) REFERENCES storage_reservation(id);


--
-- Name: storage_reservation_disk_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation_disk_map
    ADD CONSTRAINT storage_reservation_disk_map_fk2 FOREIGN KEY (disk_id) REFERENCES disk(id);


--
-- Name: storage_reservation_disk_template_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation_disk_template_map
    ADD CONSTRAINT storage_reservation_disk_template_map_fk1 FOREIGN KEY (storage_reservation_id) REFERENCES storage_reservation(id);


--
-- Name: storage_reservation_disk_template_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation_disk_template_map
    ADD CONSTRAINT storage_reservation_disk_template_map_fk2 FOREIGN KEY (disk_template_id) REFERENCES disk_template(id);


--
-- Name: storage_reservation_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation
    ADD CONSTRAINT storage_reservation_fk1 FOREIGN KEY (reservation_id) REFERENCES reservation(id);


--
-- Name: storage_reservation_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_reservation
    ADD CONSTRAINT storage_reservation_fk2 FOREIGN KEY (storage_id) REFERENCES storage(id);


--
-- Name: storage_resource_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY storage_resource
    ADD CONSTRAINT storage_resource_fk1 FOREIGN KEY (storage_id) REFERENCES storage(id);


--
-- Name: template_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY template
    ADD CONSTRAINT template_fk1 FOREIGN KEY (cluster_id) REFERENCES cluster(id);


--
-- Name: template_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY template
    ADD CONSTRAINT template_fk2 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: term_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY term
    ADD CONSTRAINT term_fk1 FOREIGN KEY (application_id) REFERENCES application(id);


--
-- Name: vhut_user_cloud_user_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vhut_user_cloud_user_map
    ADD CONSTRAINT vhut_user_cloud_user_map_fk1 FOREIGN KEY (vhut_user_id) REFERENCES vhut_user(id);


--
-- Name: vhut_user_role_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vhut_user_role_map
    ADD CONSTRAINT vhut_user_role_map_fk1 FOREIGN KEY (role_id) REFERENCES role(id);


--
-- Name: vhut_user_role_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vhut_user_role_map
    ADD CONSTRAINT vhut_user_role_map_fk2 FOREIGN KEY (vhut_user_id) REFERENCES vhut_user(id);


--
-- Name: vlan_reservation_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vlan_reservation
    ADD CONSTRAINT vlan_reservation_fk1 FOREIGN KEY (security_group_id) REFERENCES security_group(id);


--
-- Name: vlan_reservation_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vlan_reservation
    ADD CONSTRAINT vlan_reservation_fk2 FOREIGN KEY (network_id) REFERENCES network(id);


--
-- Name: vlan_reservation_fk3; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vlan_reservation
    ADD CONSTRAINT vlan_reservation_fk3 FOREIGN KEY (reservation_id) REFERENCES reservation(id);


--
-- Name: vm_cloud_user_map_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm_cloud_user_map
    ADD CONSTRAINT vm_cloud_user_map_fk1 FOREIGN KEY (cloud_user_id) REFERENCES cloud_user(id);


--
-- Name: vm_cloud_user_map_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm_cloud_user_map
    ADD CONSTRAINT vm_cloud_user_map_fk2 FOREIGN KEY (vm_id) REFERENCES vm(id);


--
-- Name: vm_fk1; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm
    ADD CONSTRAINT vm_fk1 FOREIGN KEY (cluster_id) REFERENCES cluster(id);


--
-- Name: vm_fk2; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm
    ADD CONSTRAINT vm_fk2 FOREIGN KEY (conflict_id) REFERENCES conflict(id);


--
-- Name: vm_fk3; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm
    ADD CONSTRAINT vm_fk3 FOREIGN KEY (host_id) REFERENCES host(id);


--
-- Name: vm_fk4; Type: FK CONSTRAINT; Schema: public; Owner: vhut
--

ALTER TABLE ONLY vm
    ADD CONSTRAINT vm_fk4 FOREIGN KEY (template_id) REFERENCES template(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--



/**
 * =====================================================================
 * 
 *    Copyright 2011 NTT Sofware Corporation
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * =====================================================================
 */
