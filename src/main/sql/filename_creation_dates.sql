drop table if exists fileCreationDate;
create table fileCreationDate as select g1.*
from GitCommitRecord g1
where g1.commitDate = (select min(g2.commitDate)
  from GitCommitRecord g2
  where g2.filename = g1.filename)
order by commitDate;