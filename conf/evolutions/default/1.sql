# accounttype schema

# --- !Ups

CREATE TABLE public.transactiontype (
  transactiontypeid VARCHAR(40) NOT NULL,
  transactiontypename VARCHAR(15) NOT NULL,
  CONSTRAINT trtid PRIMARY KEY (transactiontypeid)
);


CREATE TABLE public.accuser (
  accuserid VARCHAR(40) NOT NULL,
  usermail VARCHAR(25) NOT NULL,
  CONSTRAINT accuserid PRIMARY KEY (accuserid)
);


CREATE TABLE public.accounttype (
  acctypeid VARCHAR(40) NOT NULL,
  accuserid VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  closingday CHAR(2) NOT NULL,
  accname VARCHAR(25) NOT NULL,
  CONSTRAINT accounttypeid PRIMARY KEY (acctypeid, accuserid)
);


CREATE TABLE public.balance (
  balanceid VARCHAR(40) NOT NULL,
  acctypeid VARCHAR(40) NOT NULL,
  accuserid VARCHAR(40) NOT NULL,
  calcbalance NUMERIC(6,2) NOT NULL,
  realbalance NUMERIC(6,2) NOT NULL,
  balancedate DATE NOT NULL,
  CONSTRAINT balanceid PRIMARY KEY (balanceid, acctypeid, accuserid)
);


CREATE TABLE public.item (
  itemid VARCHAR(40) NOT NULL,
  transactiontypeid VARCHAR(40) NOT NULL,
  acctypeid VARCHAR(40) NOT NULL,
  accuserid VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  itemvalue NUMERIC(6,2) NOT NULL,
  purchaseDate DATE NOT NULL,
  CONSTRAINT itemid PRIMARY KEY (itemid, transactiontypeid, acctypeid, accuserid)
);


ALTER TABLE public.item ADD CONSTRAINT transactiontype_item_fk
FOREIGN KEY (transactiontypeid)
REFERENCES public.transactiontype (transactiontypeid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.accounttype ADD CONSTRAINT accuser_accounttype_fk
FOREIGN KEY (accuserid)
REFERENCES public.accuser (accuserid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.item ADD CONSTRAINT accounttype_item_fk
FOREIGN KEY (acctypeid, accuserid)
REFERENCES public.accounttype (acctypeid, accuserid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.balance ADD CONSTRAINT accounttype_balance_fk
FOREIGN KEY (acctypeid, accuserid)
REFERENCES public.accounttype (acctypeid, accuserid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

# --- !Downs

DROP TABLE item;
DROP TABLE balance;
DROP TABLE accounttype;
DROP TABLE accuser;