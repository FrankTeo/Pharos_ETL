create or replace procedure autosynchron_pa as
  ncount  number;
  ERRTEXT VARCHAR2(2000);
  n       number;
  m       number;
  cedr    varchar2(24);
begin
--同步保批单
  for p in (select * from pharos) loop
    select count(*) into ncount from t_policy where c_ply_no = p.c_ply_no;
    if ncount = 0 then
      insert into t_policy
        (C_CRT_CDE,
         C_UPD_CDE,
         C_PLY_APP_NO,
         C_PLY_NO,
         C_INSRNC_CDE,
         C_DPT_CDE,
         C_BSNS_TYP,
         C_SLS_CDE,
         T_APP_TM,
         T_INSRNC_BGN_TM,
         T_INSRNC_END_TM,
         C_AMT_CUR,
         N_INSRNC_VLU,
         N_AMT,
         N_PRM,
         N_RDR_PRM,
         N_SUM_PRM,
         C_PRM_CUR,
         N_NCLM_PROP,
         N_APPNT_PROP,
         N_CMM_PROP,
         N_BRK_PROP,
         T_NEXT_EDR_UNDR_TM,
         C_EDR_PRJ_NO,
         C_OPR_TYP,
         C_UDR_MRK,
         C_CEPRN_MRK,
         C_INSRNT_CDE,
         C_INSRNT_NME,
         C_APP_CDE,
         C_APP_NME,
         C_CMPNY_AGT_CDE,
         C_BRKR_CDE,
         C_UNDR_PRSN,
         T_SIGN_TM,
         T_UNDR_TM,
         C_CRT_PRSN,
         T_INPUT_TM,
         C_UNDR_DPT,
         C_APP_TYP,
         C_OPERTYP_CDE,
         N_SALARY_PROP,
         C_SALE_SYS,
         C_COINSRNC_MRK,
         N_OUR_PROP,
         t_crt_Tm,
         C_ORIG_PLY_NO)
        select 'admin',
               'pharos',
               a.c_ply_app_no,
               C_PLY_NO,
               a.C_INSRNC_CDE,
               a.C_DPT_CDE,
               a.C_BSNS_TYP,
               a.C_SLS_CDE,
               a.T_APP_TM,
               a.T_INSRNC_BGN_TM,
               a.T_INSRNC_END_TM,
               a.C_AMT_CUR,
               a.n_amt,
               a.n_amt,
               a.n_prm, --decode(a.N_CO_MRK,'1',a.n_prm*a.N_CO_PROP,a.n_prm),
               0,
               a.n_prm, --decode(a.N_CO_MRK,'1',a.n_prm*a.N_CO_PROP,a.n_prm),
               a.C_PRM_CUR,
               a.N_NCLM_PROP,
               0,
               a.N_CMM_PROP,
               a.N_BRK_PROP,
               to_date('29991231', 'yyyymmdd'),
               0,
               '1',
               '1',
               '1',
               a.C_INSRNT_CDE,
               a.C_INSRNT_nme,
               a.C_APP_CDE,
               a.C_APP_NME,
               a.C_CMPNY_AGT_CDE,
               a.C_BRKR_CDE,
               a.C_UNDR_CDE,
               a.T_SIGN_TM, --签单日期
               a.T_UNDR_TM,
               a.C_CRT_PRSN,
               a.T_INSRNC_BGN_TM - 1,
               a.C_DPT_CDE,
               '0',
               '1',
               a.N_SALARY_PROP,
               a.C_SALE_SYS,
               a.N_CO_MRK,
               nvl(a.N_CO_PROP, 0),
               sysdate,
               a.C_ORIG_PLY_NO
          from pharos a
         where a.c_Ply_no = p.c_ply_no
           and a.n_amt <> 0;
    end if;
    commit;
  end loop;

  begin
    for a in (select *
                from pharos_edr
              --where n_amt <> 0
               order by C_PLY_NO, C_edr_NO) loop
    
      select count(*) into m FROM T_EDRSMT WHERE C_edr_NO = A.C_edr_NO;
    
      if m = 0 then
        select count(*) into n FROM T_EDRSMT WHERE C_PLY_NO = A.C_PLY_NO;
      
        if n = 0 then
          update t_policy
             set T_NEXT_EDR_UNDR_TM = a.t_undr_tm
           where c_ply_no = A.C_PLY_NO;
          insert into t_edrsmt
            (C_CRT_CDE,
             C_UPD_CDE,
             C_edr_no,
             C_PLY_NO,
             C_INSRNC_CDE,
             C_DPT_CDE,
             C_BSNS_TYP,
             C_SLS_CDE,
             T_APP_TM,
             T_INSRNC_BGN_TM,
             T_INSRNC_END_TM,
             C_AMT_CUR,
             N_INSRNC_VLU,
             N_AMT,
             N_PRM,
             N_RDR_PRM,
             N_SUM_PRM,
             C_PRM_CUR,
             N_NCLM_PROP,
             N_APPNT_PROP,
             N_CMM_PROP,
             N_BRK_PROP,
             T_NEXT_EDR_UNDR_TM,
             C_EDR_PRJ_NO,
             C_OPR_TYP,
             C_UDR_MRK,
             C_CEPRN_MRK,
             C_INSRNT_CDE,
             C_INSRNT_NME,
             C_APP_CDE,
             C_APP_NME,
             C_CMPNY_AGT_CDE,
             C_BRKR_CDE,
             C_UNDR_PRSN,
             T_SIGN_TM,
             T_UNDR_TM,
             C_CRT_PRSN,
             T_INPUT_TM,
             C_UNDR_DPT,
             C_APP_TYP,
             C_OPERTYP_CDE,
             N_SALARY_PROP,
             C_SALE_SYS,
             n_prm_var,
             c_edr_typ, --批改类型
             c_app_prsn_cde,
             c_app_prsn_nme,
             c_edr_rsn,
             c_edr_ctnt,
             n_amt_var,
             n_nclm_amt,
             n_appnt_amt,
             n_ndis_amt,
             n_nnclm_amt,
             c_agElong_mrk,
             C_COINSRNC_MRK,
             N_OUR_PROP,
             C_ORIG_PLY_NO, --续保保单号
             t_crt_tm)
          values
            ('admin',
             'pharos',
             a.C_edr_NO,
             a.C_PLY_NO,
             a.C_INSRNC_CDE,
             a.C_DPT_CDE,
             a.C_BSNS_TYP,
             a.C_SLS_CDE,
             a.T_INSRNC_BGN_TM - 1,
             a.T_INSRNC_BGN_TM,
             a.T_INSRNC_END_TM,
             a.C_AMT_CUR,
             a.n_amt,
             a.n_amt,
             a.n_prm, --decode(a.N_CO_MRK,'1',a.n_prm*a.N_CO_PROP,a.n_prm),
             0,
             a.N_SUM_PRM,
             a.C_PRM_CUR,
             a.N_NCLM_PROP,
             0,
             a.N_CMM_PROP,
             a.N_BRK_PROP,
             to_date('29991231', 'yyyymmdd'),
             n + 1,
             '1',
             '1',
             '1',
             a.C_INSRNT_CDE,
             a.C_INSRNT_nme,
             a.C_APP_CDE,
             a.C_APP_NME,
             a.C_CMPNY_AGT_CDE,
             a.C_BRKR_CDE,
             'admin', --a.C_UNDR_PRSN,
             a.T_SIGN_TM,
             a.T_UNDR_TM,
             a.C_CRT_PRSN,
             a.T_INSRNC_BGN_TM - 1,
             a.C_DPT_CDE,
             '0',
             '1',
             a.N_SALARY_PROP,
             a.C_SALE_SYS,
             a.n_prm_var,
             a.C_EDR_TYP, --批改类型
             a.C_APP_CDE,
             a.C_APP_NME,
             '04',
             '新系统转回',
             a.N_AMT_VAR,
             0,
             0,
             0,
             0,
             '0',
             a.N_CO_MRK,
             nvl(a.N_CO_PROP, 0),
             a.C_ORIG_PLY_NO, --续保保单号
             sysdate);
        
        else
        
          select max(c_edr_no)
            into cedr
            FROM T_EDRSMT
           WHERE C_PLY_NO = A.C_PLY_NO;
          update t_edrsmt
             set T_NEXT_EDR_UNDR_TM = a.t_undr_tm
           where c_edr_no = cedr;
          insert into t_edrsmt
            (C_CRT_CDE,
             C_UPD_CDE,
             C_edr_no,
             C_PLY_NO,
             C_INSRNC_CDE,
             C_DPT_CDE,
             C_BSNS_TYP,
             C_SLS_CDE,
             T_APP_TM,
             T_INSRNC_BGN_TM,
             T_INSRNC_END_TM,
             C_AMT_CUR,
             N_INSRNC_VLU,
             N_AMT,
             N_PRM,
             N_RDR_PRM,
             N_SUM_PRM,
             C_PRM_CUR,
             N_NCLM_PROP,
             N_APPNT_PROP,
             N_CMM_PROP,
             N_BRK_PROP,
             T_NEXT_EDR_UNDR_TM,
             C_EDR_PRJ_NO,
             C_OPR_TYP,
             C_UDR_MRK,
             C_CEPRN_MRK,
             C_INSRNT_CDE,
             C_INSRNT_NME,
             C_APP_CDE,
             C_APP_NME,
             C_CMPNY_AGT_CDE,
             C_BRKR_CDE,
             C_UNDR_PRSN,
             T_SIGN_TM,
             T_UNDR_TM,
             C_CRT_PRSN,
             T_INPUT_TM,
             C_UNDR_DPT,
             C_APP_TYP,
             C_OPERTYP_CDE,
             N_SALARY_PROP,
             C_SALE_SYS,
             n_prm_var,
             c_edr_typ,
             c_app_prsn_cde,
             c_app_prsn_nme,
             c_edr_rsn,
             c_edr_ctnt,
             n_amt_var,
             n_nclm_amt,
             n_appnt_amt,
             n_ndis_amt,
             n_nnclm_amt,
             c_agElong_mrk,
             C_COINSRNC_MRK,
             N_OUR_PROP,
             C_ORIG_PLY_NO, --续保保单号
             t_crt_tm)
          values
            ('admin',
             'pharos',
             a.C_edr_NO,
             a.C_PLY_NO,
             a.C_INSRNC_CDE,
             a.C_DPT_CDE,
             a.C_BSNS_TYP,
             a.C_SLS_CDE,
             a.T_INSRNC_BGN_TM - 1,
             a.T_INSRNC_BGN_TM,
             a.T_INSRNC_END_TM,
             a.C_AMT_CUR,
             a.n_amt,
             a.n_amt,
             a.n_prm, --decode(a.N_CO_MRK,'1',a.n_prm*a.N_CO_PROP,a.n_prm),
             0,
             a.N_SUM_PRM,
             a.C_PRM_CUR,
             a.N_NCLM_PROP,
             0,
             a.N_CMM_PROP,
             a.N_BRK_PROP,
             to_date('29991231', 'yyyymmdd'),
             n + 1,
             '1',
             '1',
             '1',
             a.C_INSRNT_CDE,
             a.C_INSRNT_nme,
             a.C_APP_CDE,
             a.C_APP_NME,
             a.C_CMPNY_AGT_CDE,
             a.C_BRKR_CDE,
             'admin', --a.C_UNDR_PRSN,
             a.T_INSRNC_BGN_TM - 1,
             a.T_UNDR_TM,
             a.C_CRT_PRSN,
             a.T_INSRNC_BGN_TM - 1,
             a.C_DPT_CDE,
             '0',
             '1',
             a.N_SALARY_PROP,
             a.C_SALE_SYS,
             a.n_prm_var,
             a.C_EDR_TYP, --批改类型
             a.C_APP_CDE,
             a.C_APP_NME,
             '04',
             '新系统转回',
             a.N_AMT_VAR,
             0,
             0,
             0,
             0,
             '0',
             a.N_CO_MRK,
             nvl(a.N_CO_PROP, 0),
             a.C_ORIG_PLY_NO, --续保保单号
             sysdate);
        
        end if;
      end if;
      commit;
      update t_policy t
         set C_EDR_PRJ_NO =
             (SELECT COUNT(*) FROM t_edrsmt e WHERE e.c_ply_no = a.c_ply_no)
       WHERE t.c_ply_no = a.c_ply_no;
      commit;
    end loop;
  end;

EXCEPTION
  WHEN OTHERS THEN
    errtext := '错误' || SQLERRM;
    INSERT INTO htyw_paods.stg_run_log
      (log_id,error_code, error_message, time_stamp)
    VALUES
      (to_char(sysdate, 'yyyymmddhh24miss'),
       '0',
       errtext,
       SYSDATE);
  
end;
/
