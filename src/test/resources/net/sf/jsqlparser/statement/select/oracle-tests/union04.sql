---
-- #%L
-- JSQLParser library
-- %%
-- Copyright (C) 2004 - 2019 JSQLParser
-- %%
-- Dual licensed under GNU LGPL 2.1 or Apache License 2.0
-- #L%
---
(
	select distinct job_id from hr.jobs
)
union all
(
	select distinct job_id from hr.job_history
	union all
	((((
		select distinct job_id from hr.job_history
		union all
		(
			select distinct job_id from hr.job_history
		)
	)))
	union all
		select distinct job_id from hr.job_history	
	)	
)
union all
(
	select distinct job_id from hr.job_history
	union all
	(
		select distinct job_id from hr.job_history
		union all
		(
			select distinct job_id from hr.job_history
		)
	)
)
union all
(
	select distinct job_id from hr.job_history
	union all
	select distinct job_id from hr.job_history
)
union all
	select distinct job_id from hr.job_history
union all
	select distinct job_id from hr.job_history
union all
	select distinct job_id from hr.job_history
union all
	select distinct job_id from hr.job_history	


--@SUCCESSFULLY_PARSED_AND_DEPARSED first on Aug 3, 2021, 7:20:08 AM
--@FAILURE: Encountered unexpected token: "union all" <K_UNION_ALL> recorded first on Nov 1, 2022 2:19:16 AM