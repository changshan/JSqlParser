---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select count(*)
  from employees
  where lnnvl(commission_pct >= .2)

--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "(" "(" recorded first on Nov 7, 2022 9:33:36 AM
--@FAILURE: Encountered unexpected token: "lnnvl" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM