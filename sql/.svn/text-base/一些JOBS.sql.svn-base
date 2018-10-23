
variable job1 number;

begin
  dbms_job.submit(:job1,'SYNC_BASE_DATA;',sysdate,'TRUNC(SYSDATE + 1) + (23*60+30)/(24*60)');
end;

variable job2 number;

begin
  dbms_job.submit(:job2,'TMP_CORRECT_ESTIMATE_TIMES;',sysdate, 'sysdate + 1/1440');
end;
