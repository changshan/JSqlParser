---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select * from dual where 1 < > 2 and 1 ! = 2 and 1 ^ /*aaa */ = 2


--@FAILURE: Encountered unexpected token: "=" "=" recorded first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "1" <S_LONG> recorded first on Nov 10, 2022 4:11:13 AM