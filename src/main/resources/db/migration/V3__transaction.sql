
CREATE TABLE transaction
(
  id bigserial NOT NULL,
  created_on timestamp without time zone NOT NULL,
  amount numeric(13,4),
  source_account_id bigint NOT NULL,
  destination_account_id bigint NOT NULL,
  message character varying(100),
  CONSTRAINT transaction_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE account
  OWNER TO admin;