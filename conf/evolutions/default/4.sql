# accounttype schema

# --- !Ups

CREATE TABLE public.balance (
  balanceid VARCHAR(40) NOT NULL,
  balance NUMERIC(6,2) NOT NULL,
  balancedate DATE NOT NULL,
  typeid VARCHAR(40) NOT NULL,
  CONSTRAINT balanceid PRIMARY KEY (balanceid)
);

ALTER TABLE public.balance ADD CONSTRAINT accounttype_balance_fk
FOREIGN KEY (typeid)
REFERENCES public.accounttype (acctypeid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

# --- !Downs

DROP TABLE balance;