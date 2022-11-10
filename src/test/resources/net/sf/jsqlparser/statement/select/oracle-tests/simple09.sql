---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select a||last_name,
        employee_id
    from employees
    start with job_id = 'ad_vp' 
    connect by prior employee_id = manager_id



--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "with" "WITH" recorded first on Nov 10, 2022 4:02:15 AM
--@FAILURE: Encountered unexpected token: " with" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM