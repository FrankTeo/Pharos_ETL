create or replace procedure AutoSynData is

  V_Begin_TM DATE;
  ERRTEXT    VARCHAR2(2000);
begin

  SELECT sysdate - 1.5 INTO V_Begin_TM FROM dual;
  --保单
  delete from htywdb.pharos;
  delete from htywdb.pharos_edr;
  delete from tmp_2013_pha;
  delete from tmp_2013_edr;

  insert into tmp_2013_pha

    select a.PolicyNo 保单号,
           a.EndorseNo 批单号,
           a.AcquisitionBranch 机构编码,
           a.InsuranceCode 险种编码,
           (select PartyCode
              from T_PolicyApplicant b
             where b.PolicyNo = a.PolicyNo
               and rownum = 1) 投保人编码,
           (select substrb(PartyName, 1, 150)
              from T_PolicyApplicant b
             where b.PolicyNo = a.PolicyNo
               and rownum = 1) 投保人名称,
           nvl((select PartyCode
                 from T_PolicyInsure c
                where c.PolicyNo = a.PolicyNo
                  and rownum = 1),
               (select PartyCode
                  from T_PolicyApplicant b
                 where b.PolicyNo = a.PolicyNo
                   and rownum = 1)) 被保险人编码,
           nvl((select PartyName
                 from T_PolicyInsure c
                where c.PolicyNo = a.PolicyNo
                  and rownum = 1),
               (select substrb(PartyName, 1, 150)
                  from T_PolicyApplicant b
                 where b.PolicyNo = a.PolicyNo
                   and rownum = 1)) 被保险人名称,
           a.premium 保费,
           a.Coverage 保额,
           a.StartDate 保险起期,
           a.EndDate 保险止期,
           a.UnderWriteDate 核保时间,
           a.ProposalDate 投保日期,
           a.BusinessOrigin 业务来源,
           a.AcquisitionStaff 业务员编码,
           decode(a.UnderWriteStaff, 'Administrator', '9999', a.UnderWriteStaff) 核保人编码,
           a.AgencyNo 代理人编码,
           a.BrokerNo 经纪人代码,
           a.SaleSystem 销售体系,
           d.CommissionRate 手续费比例,
           d.BrokerageRate 经纪费比例,
           d.AcquisitionExpenseRate 销售费用比例,
           d.MeritPayRate 绩效工资比例,
           a.CoInsuranceRate 共保比例,
           a.CoInsuranceType 共保类型,
           a.proposalno 投保单号,
           a.premiumcurrency 保费币种,
           a.coveragecurrency 保额币种,
           a.issuedate 发单日期,
           decode(a.createusercode, 'Administrator', 'admin', a.createusercode) 录单人,
           a.renewalpolicyno 续保保单号

      from T_Policy a, T_AcquisitionCost d

     where a.PolicyNo = d.PolicyNo
       and a.EndorseNo = d.EndorseNo
       and a.PolicyNo = a.EndorseNo
          --pharos 中保单
       and a.policyfrom <> 5

          --核保日期范围
       and a.crttime > V_Begin_TM;

  UPDATE tmp_2013_pha SET 共保比例 = 1 WHERE 共保类型 = '0';
  insert into htywdb.pharos
    SELECT * FROM tmp_2013_pha;
  commit;

--批单

insert into tmp_2013_edr
select a.PolicyNo 保单号,
       a.EndorseNo 批单号,
       a.AcquisitionBranch 机构编码,
       a.InsuranceCode 险种编码,
       (select PartyCode
          from T_PolicyApplicant b
         where b.PolicyNo = a.PolicyNo
           and rownum = 1) 投保人编码,
       (select PartyName
          from T_PolicyApplicant b
         where b.PolicyNo = a.PolicyNo
           and rownum = 1) 投保人名称,
         nvl((select PartyCode
               from T_PolicyInsure c
              where c.PolicyNo = a.PolicyNo
                and rownum = 1),
             (select PartyCode
                from T_PolicyApplicant b
               where b.PolicyNo = a.PolicyNo
                 and rownum = 1)) 被保险人编码,
         nvl((select PartyName
               from T_PolicyInsure c
              where c.PolicyNo = a.PolicyNo
                and rownum = 1),
             (select substrb(PartyName, 1, 150)
                from T_PolicyApplicant b
               where b.PolicyNo = a.PolicyNo
                 and rownum = 1)) 被保险人名称,
       a.premium 保费,
       a.Coverage 保额,
       e.PermiumChange 保费变化,
       a.StartDate 保险起期,
       a.EndDate 保险止期,
       a.UnderWriteDate 核保时间,
       a.issuedate 发单日期_A,
       a.BusinessOrigin 业务来源,
       a.AcquisitionStaff 业务员编码,
       decode(a.UnderWriteStaff,'Administrator','9999',a.UnderWriteStaff) 核保人编码,
       a.AgencyNo 代理人编码,
       a.BrokerNo 经纪人代码,
       a.SaleSystem 销售体系,
       d.CommissionRate 手续费比例,
       d.BrokerageRate 经纪费比例,
       d.AcquisitionExpenseRate 销售费用比例,
       d.MeritPayRate 绩效工资比例,
       a.CoInsuranceRate 共保比例,
       a.CoInsuranceType 共保类型,
       a.premiumcurrency 保费币种,
       a.coveragecurrency 保额币种,
     decode(e.EndorseType,'1001','3','1008','2','1') 批改类型,
     a.issuedate 发单日期,
     e.CoverageChange 保额变化,
     decode(a.createusercode,'Administrator','admin',a.createusercode) 录单人,
     e.PostPermium 批改后保费,
     a.renewalpolicyno 续保保单号

  from T_Policy a, T_Endorse e, T_AcquisitionCost d

 where

 a.PolicyNo = d.PolicyNo
 and a.endorseno like 'P%'
 and a.PolicyNo <> a.EndorseNo
 and a.EndorseNo = e.EndorseNo
 and a.EndorseNo = d.EndorseNo
 --日期范围
 and a.crttime > V_Begin_TM;

UPDATE tmp_2013_edr SET 共保比例 = 1 WHERE 共保类型 = '0';
insert into htywdb.pharos_edr select * from tmp_2013_edr;
commit;

EXCEPTION
  WHEN OTHERS THEN
    errtext := '错误' || SQLERRM;
    INSERT INTO stg_run_log
      (log_id,error_code, error_message, time_stamp)
    VALUES
      (to_char(sysdate,'ddhh24miss'), '9', errtext, SYSDATE);

end AutoSynData;
/
