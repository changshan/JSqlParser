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
   for update of employee_id wait 10


--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: " wait" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM
--@FAILURE: Encountered unexpected token: "for" "FOR" recorded first on Nov 10, 2022 5:27:11 AM