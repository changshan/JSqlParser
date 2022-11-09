---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select * from dual where sysdate > date '2013-04-10'

--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "sysdate" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM
--@FAILURE: Encountered unexpected token: "\'2013-04-10\'" <S_CHAR_LITERAL> recorded first on Nov 10, 2022 4:13:39 AM