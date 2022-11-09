---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select 
ROOT,LEV,OBJ,LinK,PaTH,cycle,
    case
    when (LEV - LEaD(LEV) over (order by orD)) < 0 then 0
    else 1
    end is_LEaF
from T



--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "then" "THEN" recorded first on Nov 10, 2022 4:11:14 AM