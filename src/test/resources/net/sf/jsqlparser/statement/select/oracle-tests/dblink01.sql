---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select last_name, department_name 
   from employees@remote, departments
   where employees.department_id = departments.department_id



--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "employees" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM