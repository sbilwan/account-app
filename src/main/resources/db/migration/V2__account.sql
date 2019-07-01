
CREATE TABLE account
(
  id bigserial NOT NULL,
  created_on timestamp without time zone NOT NULL,
  last_updated_on timestamp without time zone NOT NULL,
  balance numeric(13,4),
  acc_status character varying(2),
  acc_type character varying(7),
  overdraw_limit numeric(13,4),
  customer_id bigint,
  CONSTRAINT account_pkey PRIMARY KEY (id),
  CONSTRAINT foreign_key FOREIGN KEY (customer_id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE account
  OWNER TO admin;