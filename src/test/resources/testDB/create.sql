
create class customer;
create property customer.name string;
create index customer_name on customer (name) unique;
create property customer.friends linklist;


insert into customer (name, pesel, address, contactDetails, labels) values ('Ahmed', '8234044554',
  {"street":"Naser", "city":"Cairo", "country":"Egypt"},
  {"email":"ahmed@yahoo.eg", "phone":"987654"},  ["solid","stubborn"]);
insert into customer (name, pesel, address, contactDetails, labels, friends) values ('Maciek', '820445555',{"street":"Popularna", "city":"Warsaw", "country":"Poland"},
  {"email":"mproch@gazeta.pl", "phone":"12345678"}, ["lazy","nice"], (select from customer where name = 'Ahmed'));
insert into customer (name, pesel, address, contactDetails, labels, friends) values ('Tammo', '1234567',{"street":"Keskus", "city":"Tampere", "country":"Finland"},
  {"email":"tammo@gazeta.fi", "phone":"4512345678"}, ["lazy","nice"], []);


create class saleAgent;
create property saleAgent.code string;
create index saleAgent_code on saleAgent (code) unique;
create property customer.saleAgent link;

create class branch;
create property branch.name string;
insert into branch (name) values ('local');
insert into branch (name) values ('global');

insert into saleAgent (code, branch) values ('1', (select from branch where name = 'local'));
insert into saleAgent (code, branch) values ('2', (select from branch where name = 'local'));