create database local:/home/mproch/sandbox/scala-orientdb/src/test/resources/jdc/db admin admin local
create class customer
create property customer.name string
create index customer_name on customer (name) unique

insert into customer (name, address, contactDetails, labels) values ('Maciek', {"street":"Popularna", "city":"Warsaw", "country":"Poland"},
  {"email":"mproch@gazeta.pl", "phone":"12345678"}, ["lazy","nice"])
insert into customer (name, address, contactDetails, labels) values ('Ahmed',
  {"street":"Naser", "city":"Cairo", "country":"Egypt"},
  {"email":"ahmed@yahoo.eg", "phone":"987654"},  ["solid","stubborn"])

select address.street from customer where contactDetails.email = 'mproch@gazeta.pl'
update customer put contactDetails = 'phone2', '4444444' where name = 'Maciek'


create class saleAgent
create property saleAgent.code string
create index saleAgent_code on saleAgent (code) unique
create property customer.saleAgent link

create class branch
create property branch.name string
insert into branch (name) values ('local')
insert into branch (name) values ('global')

insert into saleAgent (code, branch) values ('1', (select from branch where name = 'local'))
insert into saleAgent (code, branch) values ('2', (select from branch where name = 'local'))

update customer set saleAgent = #9:1 where name =  'Maciek'
update customer set saleAgent = (select from saleAgent where code = '2') where name = 'Ahmed'

select saleAgent.code from customer





