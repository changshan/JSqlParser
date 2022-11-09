---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
delete from table_name
where current of cursor_name

--@FAILURE: Encountered unexpected token: "of" "OF" recorded first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "current" "CURRENT" recorded first on Nov 10, 2022 4:11:14 AM