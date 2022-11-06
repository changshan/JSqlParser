---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select deptno
     ,      cast(
               collect(all job)
                  as varchar2_ntt) as distinct_jobs
     from   emp
     group  by
            deptno

--@FAILURE: Encountered unexpected token: "(" "(" recorded first on Nov 6, 2022 5:07:57 PM