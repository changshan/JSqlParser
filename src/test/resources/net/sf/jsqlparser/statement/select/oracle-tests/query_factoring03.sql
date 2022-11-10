---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
with
  reports_to_101 (eid, emp_last, mgr_id, reportlevel) as
  (
    (select employee_id, last_name, manager_id, 0 reportlevel
    from employees
    where employee_id = 101)
  union all
    (select e.employee_id, e.last_name, e.manager_id, reportlevel+1
    from reports_to_101 r, employees e
    where r.eid = e.manager_id)
  )
select eid, emp_last, mgr_id, reportlevel
from reports_to_101
where reportlevel <= 1
order by reportlevel, eid


--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "union all" <K_UNION_ALL> recorded first on Nov 1, 2022 2:19:16 AM
--@FAILURE: Encountered unexpected token: "\nwith" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM