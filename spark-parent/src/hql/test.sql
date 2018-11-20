select distinct sc.* from score sc ,(select s_id from score where s_score < 60 group by s_id)tmp
where sc.s_id not exists (tmp.s_id);


select sc.* from score sc left join(select s_id from score where s_score < 60 group by s_id)tmp
on sc.s_id=tmp.s_id where  tmp.s_id is  null;


select distinct sc.* from score sc ,(select s_id from score where s_score < 60 group by s_id)tmp
where sc.s_id not in ('04','06');