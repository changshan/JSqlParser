---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select employee_id from (select employee_id+1 as employee_id from employees)
   for update of employee_id skip locked


--@FAILURE: Encountered unexpected token: "skip" "SKIP" recorded first on Aug 3, 2021, 7:20:08 AM
--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Oct 19, 2022, 6:34:12 PM