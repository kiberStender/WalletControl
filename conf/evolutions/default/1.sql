# accounttype schema

# --- !Ups

CREATE TABLE public.accounttype (
  acctypeid VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  balance NUMERIC(4,2) NOT NULL,
  closingday DATE NOT NULL,
  CONSTRAINT accounttypeid PRIMARY KEY (acctypeid)
);

# --- !Downs

DROP TABLE accounttype;