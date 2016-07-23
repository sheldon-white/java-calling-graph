drop table if exists commitWithDate;
drop table if exists commitWithMonth;
drop table if exists lineCountByMonth;
drop table if exists LineCountByMonthAndAuthor;
create table commitWithDate as select commitID, authorEmail, FROM_UNIXTIME(commitDate/1000) as commitDate, linesAdded - linesDeleted as linesAdded from GitCommitRecord where filename not like '%/vendor/%';
create table commitWithMonth as select commitID, authorEmail, DATE_FORMAT(commitDate, '%Y-%m-01 00:00:00') as month, linesAdded from commitWithDate;
alter table commitWithMonth add column id INT AUTO_INCREMENT PRIMARY KEY FIRST;
create table lineCountByMonth as select authorEmail, month, sum(linesAdded) as linesAdded from commitWithMonth
  group by month, authorEmail;
alter table lineCountByMonth add column id INT AUTO_INCREMENT PRIMARY KEY FIRST;
create table LineCountByMonthAndAuthor as select c.id, c.authorEmail, month,
         (select SUM(x.linesAdded)
          from lineCountByMonth x
          where x.id <= c.id AND x.authorEmail = c.authorEmail) AS linesAdded
    from lineCountByMonth c
ORDER by c.id
