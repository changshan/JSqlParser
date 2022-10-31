---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
(select bs.keep keep, bs.keep_until keep_until 
from v$backup_set bs) 
union all 
(select null keep, null keep_until
from v$backup_piece bp)


--@SUCCESSFULLY_PARSED_AND_DEPARSED first on 3 Jun 2022, 18:48:09
--@FAILURE: Encountered unexpected token: "union all" <K_UNION_ALL> recorded first on Nov 1, 2022 2:19:16 AM