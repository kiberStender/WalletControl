# accuser schema

# --- !Ups

CREATE TABLE public.accuser (
  accuserid VARCHAR(40) NOT NULL,
  username VARCHAR(25) NOT NULL,
  password VARCHAR(40) NOT NULL,
  CONSTRAINT accuserid PRIMARY KEY (accuserid)
);

# --- !Downs

DROP TABLE accuser;