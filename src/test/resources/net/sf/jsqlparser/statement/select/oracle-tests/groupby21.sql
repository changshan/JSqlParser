---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
select channel_desc, calendar_month_desc, countries.country_iso_code,
      to_char(sum(amount_sold), '9,999,999,999') sales$
from sales, customers, times, channels, countries
where sales.time_id=times.time_id and sales.cust_id=customers.cust_id and
  sales.channel_id= channels.channel_id
 and customers.country_id = countries.country_id
 and channels.channel_desc in
  ('direct sales', 'internet') and times.calendar_month_desc in
  ('2000-09', '2000-10') and countries.country_iso_code in ('gb', 'us')
group by cube(channel_desc, calendar_month_desc, countries.country_iso_code)

--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "sales" <S_IDENTIFIER> recorded first on Nov 10, 2022 4:11:13 AM