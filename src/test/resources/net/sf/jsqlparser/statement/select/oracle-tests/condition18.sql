---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
update tab1
set c1 = 'x'
where current of c_cur1


--@FAILURE: Encountered unexpected token: "of" "OF" recorded first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "\nwhere" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:12 AM
--@FAILURE: Encountered unexpected token: "update" <S_IDENTIFIER> recorded first on Nov 10, 2022 5:27:11 AM