# --- Sample dataset

# --- !Ups

Insert into transactiontype(transactiontypeid, transactiontypename) values('id00', 'AV');
Insert into transactiontype(transactiontypeid, transactiontypename) values('id01', 'AP');
Insert into transactiontype(transactiontypeid, transactiontypename) values('id02', 'ET');

# --- !Downs

Delete from transactiontype;