drop table if exists commitWithDate;
drop table if exists commitWithDay;
drop table if exists lineCountByDay;
drop table if exists LineCountByDayAndAuthor;
create table commitWithDate as select commitID, authorEmail, FROM_UNIXTIME(commitDate/1000) as commitDate, linesAdded - linesDeleted as linesAdded from GitCommitRecord where filename not like '%/vendor/%';
create table commitWithDay as select commitID, authorEmail, DATE_FORMAT(commitDate, '%Y-%m-%d 00:00:00') as day, linesAdded from commitWithDate;
alter table commitWithDay add column id INT AUTO_INCREMENT PRIMARY KEY FIRST;
create table lineCountByDay as select commitID, authorEmail, day, sum(linesAdded) as linesAdded from commitWithDay
  group by day, authorEmail;
alter table lineCountByDay add column id INT AUTO_INCREMENT PRIMARY KEY FIRST;
create table LineCountByDayAndAuthor as select c.id, c.commitID, c.authorEmail, day,
         (select SUM(x.linesAdded)
          from lineCountByDay x
          where x.id <= c.id AND x.authorEmail = c.authorEmail) AS linesAdded
    from lineCountByDay c
ORDER by c.id
