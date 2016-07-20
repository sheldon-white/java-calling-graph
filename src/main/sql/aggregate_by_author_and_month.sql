drop table if exists commitWithDate;
drop table if exists commitWithMonth;
drop table if exists LineCountByMonthAndAuthorJson;
drop table if exists LineCountByMonthAndAuthor;

create table LineCountByMonthAndAuthorJson(authorEmail VARCHAR(200), value JSON);

create table commitWithDate as select commitID, authorEmail, FROM_UNIXTIME(commitDate/1000) as commitDate, linesAdded, linesDeleted from GitCommitRecord;
create table commitWithMonth as select commitID, authorEmail, DATE_FORMAT(commitDate, '%Y-%m') as month, linesAdded, linesDeleted from commitWithDate;
create table LineCountByMonthAndAuthor as  select authorEmail, month, sum(linesAdded) as linesAdded, sum(linesDeleted) as linesDeleted from commitWithMonth
  group by month, authorEmail;
ALTER TABLE LineCountByMonthAndAuthor add column id INT AUTO_INCREMENT PRIMARY KEY FIRST;

insert into LineCountByMonthAndAuthorJson(authorEmail, value) select authorEmail, JSON_OBJECT('monthTS', UNIX_TIMESTAMP(STR_TO_DATE(concat(month, '-10'), '%Y-%m-%d')), 'lines', linesAdded - linesDeleted,  'authorEmail', authorEmail) from LineCountByMonthAndAuthor;
