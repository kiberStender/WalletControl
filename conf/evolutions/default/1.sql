# accounttype schema

# --- !Ups

CREATE TABLE public.accounttype (
  acctypeid VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  closingday DATE NOT NULL,
  accname VARCHAR(25) NOT NULL,
  CONSTRAINT accounttypeid PRIMARY KEY (acctypeid)
);


CREATE TABLE public.balance (
  balanceid VARCHAR(40) NOT NULL,
  typeid VARCHAR(40) NOT NULL,
  calcbalance NUMERIC(6,2) NOT NULL,
  realbalance NUMERIC(6,2) NOT NULL,
  balancedate DATE NOT NULL,
  CONSTRAINT balanceid PRIMARY KEY (balanceid, typeid)
);


CREATE TABLE public.accuser (
  accuserid VARCHAR(40) NOT NULL,
  username VARCHAR(25) NOT NULL,
  password VARCHAR(40) NOT NULL,
  CONSTRAINT accuserid PRIMARY KEY (accuserid)
);


CREATE TABLE public.item (
  itemid VARCHAR(40) NOT NULL,
  userid VARCHAR(40) NOT NULL,
  acctype VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  purchaseDate DATE NOT NULL,
  trtype VARCHAR(3) NOT NULL,
  CONSTRAINT itemid PRIMARY KEY (itemid, userid, acctype)
);


ALTER TABLE public.item ADD CONSTRAINT accounttype_item_fk
FOREIGN KEY (acctype)
REFERENCES public.accounttype (acctypeid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.balance ADD CONSTRAINT accounttype_balance_fk
FOREIGN KEY (typeid)
REFERENCES public.accounttype (acctypeid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.item ADD CONSTRAINT accuser_item_fk
FOREIGN KEY (userid)
REFERENCES public.accuser (accuserid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

# --- !Downs

DROP TABLE balance;
DROP TABLE item;
DROP TABLE accounttype;
DROP TABLE accuser;