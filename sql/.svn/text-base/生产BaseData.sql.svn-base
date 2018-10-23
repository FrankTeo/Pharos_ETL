drop database link PHAROSLINK;
create database link PHAROSLINK connect to "htbiz_new" identified by "neshtbizp" using 
'(DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.2.72)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVICE_NAME = rac)
      (INSTANCE_NAME = rac1)
    )
)';


--同步旧机构表
delete from t_branchold where 1=1;
insert into t_branchold (branchno, name, shortName, address, phone, fax, opendate, closedate, upcode, partycode, reportphone, reportaddress, zipcode, brhtype, brhlevel, crttime )
select branchno, name, shortName, address, phone, fax, opendate, closedate, upcode, partycode, reportphone, reportaddress, zipcode, brhtype, brhlevel, crttime
from v_branchold;
commit;

--同步编码类型
delete from t_parameter where 1=1;
insert into t_parameter (codetype, name, englishname, localname,description, startdate,enddate,parentcode,haschild,extenddescription, secutity, standardcode, crttime )
select codetype, name, englishname, localname,description, startdate,enddate,parentcode,haschild,extenddescription, secutity, standardcode, crttime 
from v_parameter;
commit;

--同步编码
delete from t_parametervalue where 1=1;
insert into t_parametervalue (codetype, code, name, englishname, localname,description,startdate, enddate,extenddescription,standardcode,secutity,crttime )
select codetype, code, name, englishname, localname,description,startdate, enddate,extenddescription,standardcode,secutity,crttime 
from v_parametervalue;
commit;

--同步编码属性
delete from t_parameterattributevalue where 1=1;
insert into t_parameterattributevalue (codetype, code, attributecode, name, Datatype, intvalue, stringvalue, longvalue, 
datetimevalue, datetimevalue2, booleanvalue, doublevalue, crttime )
select codetype, code, attributecode, name, Datatype, intvalue, stringvalue, longvalue, 
datetimevalue, datetimevalue2, booleanvalue, doublevalue, crttime
from v_parameterattributevalue;
commit;


--同步User
delete from t_user where 1=1;
insert into t_user (usercode, userid, firstname, lastname, branchno, relationcode, partycode, crttime ) 
select usercode, userid, firstname, lastname, branchno, relationcode, partycode, crttime from v_user;
commit;

--同步险种代码
delete from t_insurancecode where 1=1;
insert into t_insurancecode (insurancecode, insurancename, isinward, crttime, InsuranceType, InsureSubType )
select insurancecode, insurancename, isinward, crttime, InsuranceType, InsureSubType from v_insurancecode;
commit;

--同步产品代码
delete from T_ProductCode where 1=1;
insert into T_ProductCode (ProductCode, ProductName) 
select insurancecode, insurancename from t_insurancecode where 1=1;
commit;

--同步产品与险种关系
delete from T_ProductInsurance where 1=1;
insert into T_ProductInsurance( InsuranceCode, ProductCode )
select insurancecode, insurancecode from t_insurancecode where 1 = 1;
commit;

--同步责任代码
delete from t_liabilitycode where 1=1;
insert into t_liabilitycode ( liabilitycode, liabilityname, abbreviation, crttime ) 
select liabilitycode, liabilityname, abbreviation, crttime from v_liabilitycode;
commit;

--汇率表从老系统同步
delete from t_exchangeRate where 1=1;
insert into t_exchangeRate ( c_cur_no_1, c_cur_no_2, t_effc_tm, t_expd_tm, n_chg_rte, c_crt_cde, t_crt_tm, c_upd_cde, t_upd_tm) 
select c_cur_no_1, c_cur_no_2, t_effc_tm, t_expd_tm, n_chg_rte, c_crt_cde, t_crt_tm, c_upd_cde, t_upd_tm
from v_exchangeRate where 1=1;
commit;


delete from t_productliability where 1=1;
insert into t_productliability (ProductCode, LiabilityCode, LiabilityName, LiabilityEngName, CrtTime) 
select ProductCode, LiabilityCode, LiabilityName, LiabilityEngName, CrtTime from v_productliability;
commit ;

/* 老汇率表
select currency_id, '01', rate_date, lead(rate_date,1, to_date('2099-12-31','yyyy-mm-dd')) over (partition by currency_id order by currency_id, rate_date) enddate, 
purchase_rate, sale_rate, change_rate, removed, systimestamp
from t_exchangeRate@pharoslink
where 1=1;
*/
