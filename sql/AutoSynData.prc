create or replace procedure AutoSynData is

  V_Begin_TM DATE;
  ERRTEXT    VARCHAR2(2000);
begin

  SELECT sysdate - 1.5 INTO V_Begin_TM FROM dual;
  --����
  delete from htywdb.pharos;
  delete from htywdb.pharos_edr;
  delete from tmp_2013_pha;
  delete from tmp_2013_edr;

  insert into tmp_2013_pha

    select a.PolicyNo ������,
           a.EndorseNo ������,
           a.AcquisitionBranch ��������,
           a.InsuranceCode ���ֱ���,
           (select PartyCode
              from T_PolicyApplicant b
             where b.PolicyNo = a.PolicyNo
               and rownum = 1) Ͷ���˱���,
           (select substrb(PartyName, 1, 150)
              from T_PolicyApplicant b
             where b.PolicyNo = a.PolicyNo
               and rownum = 1) Ͷ��������,
           nvl((select PartyCode
                 from T_PolicyInsure c
                where c.PolicyNo = a.PolicyNo
                  and rownum = 1),
               (select PartyCode
                  from T_PolicyApplicant b
                 where b.PolicyNo = a.PolicyNo
                   and rownum = 1)) �������˱���,
           nvl((select PartyName
                 from T_PolicyInsure c
                where c.PolicyNo = a.PolicyNo
                  and rownum = 1),
               (select substrb(PartyName, 1, 150)
                  from T_PolicyApplicant b
                 where b.PolicyNo = a.PolicyNo
                   and rownum = 1)) ������������,
           a.premium ����,
           a.Coverage ����,
           a.StartDate ��������,
           a.EndDate ����ֹ��,
           a.UnderWriteDate �˱�ʱ��,
           a.ProposalDate Ͷ������,
           a.BusinessOrigin ҵ����Դ,
           a.AcquisitionStaff ҵ��Ա����,
           decode(a.UnderWriteStaff, 'Administrator', '9999', a.UnderWriteStaff) �˱��˱���,
           a.AgencyNo �����˱���,
           a.BrokerNo �����˴���,
           a.SaleSystem ������ϵ,
           d.CommissionRate �����ѱ���,
           d.BrokerageRate ���ͷѱ���,
           d.AcquisitionExpenseRate ���۷��ñ���,
           d.MeritPayRate ��Ч���ʱ���,
           a.CoInsuranceRate ��������,
           a.CoInsuranceType ��������,
           a.proposalno Ͷ������,
           a.premiumcurrency ���ѱ���,
           a.coveragecurrency �������,
           a.issuedate ��������,
           decode(a.createusercode, 'Administrator', 'admin', a.createusercode) ¼����,
           a.renewalpolicyno ����������

      from T_Policy a, T_AcquisitionCost d

     where a.PolicyNo = d.PolicyNo
       and a.EndorseNo = d.EndorseNo
       and a.PolicyNo = a.EndorseNo
          --pharos �б���
       and a.policyfrom <> 5

          --�˱����ڷ�Χ
       and a.crttime > V_Begin_TM;

  UPDATE tmp_2013_pha SET �������� = 1 WHERE �������� = '0';
  insert into htywdb.pharos
    SELECT * FROM tmp_2013_pha;
  commit;

--����

insert into tmp_2013_edr
select a.PolicyNo ������,
       a.EndorseNo ������,
       a.AcquisitionBranch ��������,
       a.InsuranceCode ���ֱ���,
       (select PartyCode
          from T_PolicyApplicant b
         where b.PolicyNo = a.PolicyNo
           and rownum = 1) Ͷ���˱���,
       (select PartyName
          from T_PolicyApplicant b
         where b.PolicyNo = a.PolicyNo
           and rownum = 1) Ͷ��������,
         nvl((select PartyCode
               from T_PolicyInsure c
              where c.PolicyNo = a.PolicyNo
                and rownum = 1),
             (select PartyCode
                from T_PolicyApplicant b
               where b.PolicyNo = a.PolicyNo
                 and rownum = 1)) �������˱���,
         nvl((select PartyName
               from T_PolicyInsure c
              where c.PolicyNo = a.PolicyNo
                and rownum = 1),
             (select substrb(PartyName, 1, 150)
                from T_PolicyApplicant b
               where b.PolicyNo = a.PolicyNo
                 and rownum = 1)) ������������,
       a.premium ����,
       a.Coverage ����,
       e.PermiumChange ���ѱ仯,
       a.StartDate ��������,
       a.EndDate ����ֹ��,
       a.UnderWriteDate �˱�ʱ��,
       a.issuedate ��������_A,
       a.BusinessOrigin ҵ����Դ,
       a.AcquisitionStaff ҵ��Ա����,
       decode(a.UnderWriteStaff,'Administrator','9999',a.UnderWriteStaff) �˱��˱���,
       a.AgencyNo �����˱���,
       a.BrokerNo �����˴���,
       a.SaleSystem ������ϵ,
       d.CommissionRate �����ѱ���,
       d.BrokerageRate ���ͷѱ���,
       d.AcquisitionExpenseRate ���۷��ñ���,
       d.MeritPayRate ��Ч���ʱ���,
       a.CoInsuranceRate ��������,
       a.CoInsuranceType ��������,
       a.premiumcurrency ���ѱ���,
       a.coveragecurrency �������,
     decode(e.EndorseType,'1001','3','1008','2','1') ��������,
     a.issuedate ��������,
     e.CoverageChange ����仯,
     decode(a.createusercode,'Administrator','admin',a.createusercode) ¼����,
     e.PostPermium ���ĺ󱣷�,
     a.renewalpolicyno ����������

  from T_Policy a, T_Endorse e, T_AcquisitionCost d

 where

 a.PolicyNo = d.PolicyNo
 and a.endorseno like 'P%'
 and a.PolicyNo <> a.EndorseNo
 and a.EndorseNo = e.EndorseNo
 and a.EndorseNo = d.EndorseNo
 --���ڷ�Χ
 and a.crttime > V_Begin_TM;

UPDATE tmp_2013_edr SET �������� = 1 WHERE �������� = '0';
insert into htywdb.pharos_edr select * from tmp_2013_edr;
commit;

EXCEPTION
  WHEN OTHERS THEN
    errtext := '����' || SQLERRM;
    INSERT INTO stg_run_log
      (log_id,error_code, error_message, time_stamp)
    VALUES
      (to_char(sysdate,'ddhh24miss'), '9', errtext, SYSDATE);

end AutoSynData;
/
