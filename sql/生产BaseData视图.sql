drop database link PHAROSLINK;
create database link PHAROSLINK connect to "htbiz_new" identified by "neshtbizp" using 
'(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.1.2.72)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = rac)(INSTANCE_NAME = rac1)))';

--同步旧机构表
create or replace view v_branchold as
select node_code branchno, name name, shortName shortName, 
getmlsnew(address, '2' ) address,
brhtel phone, fax fax, start_date opendate, end_date closedate, up_code upcode, party_code partycode,reporttel reportphone, 
getmlsnew(reportaddress, '2' ) reportaddress,
zipcode zipcode, brhtype brhtype, brhlevel brhlevel, systimestamp crttime
from t_companynode@pharoslink;

--同步编码类型
create or replace view v_parameter as
select  a.code codetype, a.name name, 
getmlsnew(a.description, '1') englishname, 
a.local_name localname, 
getmlsnew(a.description, '2' ) description,
a.start_date startdate,a.end_date enddate,
a.parent_code parentcode, a.haschild haschild,
getmlsnew(a.extendeddescription, '2' ) extenddescription,
a.security secutity, 
a.standardcode standardcode, systimestamp crttime
from t_parameter@pharoslink a;

--同步编码
create or replace view v_parametervalue as
select a.code codetype, b.code code, b.name name, 
getmlsnew(b.description, '1') englishname, 
b.chinesedescription localname, 
getmlsnew(b.description, '2' ) description,
b.start_date startdate, b.end_date enddate,
getmlsnew(b.extendeddescription, '2' ) extenddescription,
b.standardcode standardcode,
b.security secutity, systimestamp crttime
from t_parameter@pharoslink a, t_parametervalue@pharoslink b 
where a.id = b.parent_id
;

--同步编码属性
create or replace view v_parameterattributevalue as
select a.code codetype, b.code code, c.attribute_code attributecode, 
c.attribute_name name, c.data_type Datatype, c.int_value intvalue, c.string_value stringvalue, 
c.long_value longvalue, c.datetime_value datetimevalue, c.datetime_value2 datetimevalue2, c.boolean_value booleanvalue,
c.double_value doublevalue, systimestamp crttime
from t_parameter@pharoslink a, t_parametervalue@pharoslink b, t_parameterattributevalue@pharoslink c
where a.id = b.parent_id
and b.id = c.parent_id;

--同步User
create or replace view v_user as
select user_code usercode, id userid, first_name firstname, 
last_name lastname, usercompanynode_id branchno, relation_code relationcode, 
thirdparty_code partycode, systimestamp crttime
from t_secuser@pharoslink;

--同步险种代码
create or replace view v_insurancecode as
select code insurancecode,
       getmlsnew(description, '2') insurancename,
       case
         when b.boolean_value is null then
          '0'
         else
          to_char(b.boolean_value)
       end isinward,
       systimestamp crttime, c.string_value InsuranceType, d.string_value InsureSubType
  from t_pelement@pharoslink a left join v_ods_pelementattribute@pharoslink b on a.id = b.element_id and b.attribute_name = 'isInWard'
    left join v_ods_pelementattribute@pharoslink c on a.id = c.element_id and c.attribute_name = 'productType'
    left join v_ods_pelementattribute@pharoslink d on a.id = d.element_id and d.attribute_name = 'productSubType'
 where 1 = 1
   and element_type = '1015'
   and code not like '1%' and code not like '4%' and code not like '24%' and code not like '29%'
   and regexp_like(code, '[[:digit:]]{4,4}');


--同步责任代码
create or replace view v_liabilitycode as
select code liabilitycode,
getmlsnew(description, '2' ) liabilityname,
local_name abbreviation, systimestamp crttime
from t_pelement@pharoslink
where element_type ='1021';

--汇率表从老系统同步
create or replace view v_exchangerate as
select c_cur_no_1, c_cur_no_2, t_effc_tm, t_expd_tm, n_chg_rte, c_crt_cde, t_crt_tm, c_upd_cde, t_upd_tm
from t_chg_rate@link75
where 1=1;

--险种与责任关系
create or replace view v_productliability as
select insr.code ProductCode, cond.code LiabilityCode, 
getmlsnew(cond.description,'2') LiabilityName, getmlsnew(cond.description,'1') LiabilityEngName, sysdate CrtTime
from t_pelement@pharoslink risk, t_pelement@pharoslink cond, t_assignedelement@pharoslink rcrelate, 
  t_pelement@pharoslink prod, t_assignedelement@pharoslink prrelate , t_pelement@pharoslink insr, t_assignedelement@pharoslink iprelate
where risk.id = rcrelate.target_element_id and cond.id = rcrelate.source_element_id and cond.element_type = '1021' and risk.element_type = '1013'
and prod.id = prrelate.target_element_id and risk.id = prrelate.source_element_id and prod.element_type = '1014'
and insr.id = iprelate.target_element_id and prod.id = iprelate.source_element_id and insr.element_type = '1015'
union
select insr.code ProductCode, cond.code LiabilityCode, 
getmlsnew(cond.description,'2') LiabilityName, getmlsnew(cond.description,'1') LiabilityEngName, sysdate CrtTime
from t_pelement@pharoslink risk, t_pelement@pharoslink cond, t_pelement@pharoslink csnode, t_assignedelement@pharoslink carelate,
     t_assignedelement@pharoslink rcrelate, 
     t_pelement@pharoslink prod, t_assignedelement@pharoslink prrelate , t_pelement@pharoslink insr, t_assignedelement@pharoslink iprelate
where risk.id = rcrelate.target_element_id and cond.id = rcrelate.source_element_id and cond.element_type = '1021' and risk.element_type = '1013'
and csnode.id = carelate.target_element_id and risk.id = carelate.source_element_id and csnode.element_type = '1013'
and prod.id = prrelate.target_element_id and csnode.id = prrelate.source_element_id and prod.element_type = '1014'
and insr.id = iprelate.target_element_id and prod.id = iprelate.source_element_id and insr.element_type = '1015';
commit ;

/* 老汇率表
select currency_id, '01', rate_date, lead(rate_date,1, to_date('2099-12-31','yyyy-mm-dd')) over (partition by currency_id order by currency_id, rate_date) enddate, 
purchase_rate, sale_rate, change_rate, removed, systimestamp
from t_exchangeRate@pharoslink
where 1=1;
*/
