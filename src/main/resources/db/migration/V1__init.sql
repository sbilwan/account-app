CREATE TABLE client
(
  id bigserial NOT NULL,
  first_name character varying(200),
  last_name character varying(200),
  primary_address character varying(500),
  secondary_address character varying(500),
  created_on timestamp without time zone NOT NULL,
  last_updated_on timestamp without time zone NOT NULL,
  CONSTRAINT client_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE client
  OWNER TO admin;
