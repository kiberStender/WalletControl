# item schema

# --- !Ups

CREATE TABLE public.item (
  itemid VARCHAR(40) NOT NULL,
  userid VARCHAR(40) NOT NULL,
  acctype VARCHAR(40) NOT NULL,
  description VARCHAR(70) NOT NULL,
  purchaseDate DATE NOT NULL,
  CONSTRAINT itemid PRIMARY KEY (itemid, userid, acctype)
);


ALTER TABLE public.item ADD CONSTRAINT accounttype_item_fk
FOREIGN KEY (acctype)
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

DROP TABLE item;