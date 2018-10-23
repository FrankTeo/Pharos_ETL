CREATE OR REPLACE PACKAGE BODY UPDATEODS AS
  --更新T_policy表
  PROCEDURE updatePolicy(v_policyNo IN VARCHAR2) IS
    --上次批单号
    --v_lastendorseno T_policy.endorseno%TYPE;
    --本次核保时间
    --v_thizuwdate T_policy.underwritedate%TYPE;
		
  BEGIN
 --更新本保单号下的所有下次批改时间
 /*
     update t_policy p
        set nextunderwritedate =
            (nvl((select UnderWriteDate
                   from t_policy a
                  WHERE a.UnderWriteDate = (SELECT MIN(m.UnderWriteDate)
                                       FROM t_policy m
                                      WHERE m.endorseindex > p.endorseindex
                                        AND m.policyno = p.policyno)
                    and A.POLICYNO = p.policyno
                    and rownum = 1),
                 TO_DATE('3000/12/31', 'YYYY/MM/DD')))
                 */
	  update t_policy t
   set nextunderwritedate = nvl((select min(underwritedate)
                                  from t_policy t2
                                 where t2.policyno = t.policyno
                                   and t2.endorseindex > t.endorseindex
                                 ),
                                to_date('3000-12-31', 'yyyy-mm-dd'))
      where policyno = v_policyno;
  
  END;

  --更新T_endorse表
  PROCEDURE updateEndorse(v_policyNo IN VARCHAR2, v_endorseNo IN VARCHAR2) IS
    --上次批单号
    v_lastendorseno T_policy.endorseno%TYPE;
    --本次核保时间
    v_thizuwdate T_policy.underwritedate%TYPE;
		
  BEGIN
    --nextUnderWriteDate 下次批改时间 由存储过程生成
    --批单
    
	  /*
    SELECT lastendorseno, underwritedate
      INTO v_lastendorseno, v_thizuwdate
      FROM t_policy t
     WHERE t.policyno = v_policyno
       AND t.endorseno = v_endorseNo;
  	*/
    --1.更新上次保单、批单的下次核保时间为本次批单的核保时期
  /**  UPDATE T_policy p
       SET nextUnderWriteDate = v_thizuwdate
     WHERE p.policyno = v_policyno
       AND p.endorseno = v_lastendorseno;
  
    --2.设置本次批单的下次核保时间为最大时间
    UPDATE T_policy
       SET nextUnderWriteDate =
           (TO_DATE('3000/12/31', 'YYYY/MM/DD'))
     WHERE policyno = v_policyno
       AND endorseno = v_endorseno;**/
    --更新本保单号下的所有
    /*
     update t_policy p
        set nextunderwritedate =
            (nvl((select UnderWriteDate
                   from t_policy a
                  WHERE a.UnderWriteDate = (SELECT MIN(m.UnderWriteDate)
                                       FROM t_policy m
                                      WHERE m.endorseindex > p.endorseindex
                                        AND m.policyno = p.policyno)
                    and A.POLICYNO = p.policyno
                    and rownum = 1),
                 TO_DATE('3000/12/31', 'YYYY/MM/DD')))
                 */
       update t_policy t
   set nextunderwritedate = nvl((select min(underwritedate)
                                  from t_policy t2
                                 where t2.policyno = t.policyno
                                   and t2.endorseindex > t.endorseindex
                                 ),
                                to_date('3000-12-31', 'yyyy-mm-dd'))
      where policyno = v_policyno;

  END;
  
  --更新Party表
  procedure updateParty(v_partyCode in varchar2) is
  begin
    --更新Party表的Fax，Tel字段
    update T_bank t
       set remark = remark
     where v_partycode = '3.1415926';
  end;
  
  --更新Claim表
  procedure updateClaim(v_reportNo in varchar2) is
  begin
	  
	/*
    update t_regist t
       set t.nextestimatetime =
           (TO_DATE('3000/12/31', 'YYYY/MM/DD'))
     where t.reportno = v_reportNo;
     
    update t_regist t
       set t.nextestimatetime =
           (select a.estimatetime
              from t_regist a
             where a.estimatetimes =
                   (select min(estimatetimes)
                      from t_regist a
                     where a.estimatetimes > t.estimatetimes
                       and a.reportno = t.reportno)
               and a.reportno = t.reportno)
     where exists (select 1
              from t_regist a
             where a.reportno = t.reportno
               and a.estimatetimes > t.estimatetimes)
       and t.reportno = v_reportNo;
  */
	update t_regist t
       set t.nextestimatetime =
           nvl((select min(a.estimatetime)
              from t_regist a
             where a.reportno = t.reportno 
             and a.estimatetimes > t.estimatetimes), to_date('3000-12-31', 'yyyy-mm-dd'))
     where 1=1
       and t.reportno = v_reportNo;  
  end;
END UPDATEODS;
