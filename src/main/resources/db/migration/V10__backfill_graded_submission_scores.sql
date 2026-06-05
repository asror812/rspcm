-- Backfill scores for already-graded submissions that have no score yet.
-- Identified by the student's email (stable seed identity).

update practice_assignments pa
set score = 88
where pa.status = 'GRADED'
  and pa.score is null
  and pa.student_id = (select id from users where email = 'k1.asror.abdullayev@rspcm.local');

update practice_assignments pa
set score = 95
where pa.status = 'GRADED'
  and pa.score is null
  and pa.student_id = (select id from users where email = 'k1.anvar.rasulov@rspcm.local');
