drop database link PHAROSLINK;
create database link PHAROSLINK connect to "htbiz_new" identified by "htbiz_new" using 
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
truncate table t_branchold;
insert into t_branchold (branchno, name, shortName, address, phone, fax, opendate, closedate, upcode, partycode, reportphone, reportaddress, zipcode, brhtype, brhlevel, crttime )
select node_code, name, shortName, 
getmlsnew(address, '2' ) address,
brhtel, fax, start_date, end_date, up_code, party_code,reporttel, 
getmlsnew(reportaddress, '2' ),
zipcode, brhtype, brhlevel, systimestamp 
from t_companynode@pharoslink;
commit;

--同步编码类型
truncate table t_parameter;
insert into t_parameter (codetype, name, englishname, localname,description, startdate,enddate,parentcode,haschild,extenddescription, secutity, standardcode, crttime )
select  a.code, a.name, 
getmlsnew(a.description, '1') englishname, 
a.local_name, 
getmlsnew(a.description, '2' ) description,
a.start_date,a.end_date,
a.parent_code, a.haschild,
getmlsnew(a.extendeddescription, '2' ) extendeddescription,
a.security, 
a.standardcode, systimestamp 
from t_parameter@pharoslink a;
commit;

--同步编码
truncate table t_parametervalue;
insert into t_parametervalue (codetype, code, name, englishname, localname,description,startdate, enddate,extenddescription,standardcode,secutity,crttime )
select a.code, b.code, b.name, 
getmlsnew(b.description, '1') englishname, 
b.chinesedescription, 
getmlsnew(b.description, '2' ) description,
b.start_date, b.end_date,
getmlsnew(b.extendeddescription, '2' ) extendeddescription,
b.standardcode,
b.security, systimestamp 
from t_parameter@pharoslink a, t_parametervalue@pharoslink b 
where a.id = b.parent_id
;
commit;

--同步编码属性
truncate table t_parameterattributevalue;
insert into t_parameterattributevalue (codetype, code, attributecode, name, Datatype, intvalue, stringvalue, longvalue, 
datetimevalue, datetimevalue2, booleanvalue, doublevalue, crttime )
select a.code, b.code, c.attribute_code, c.attribute_name, c.data_type, c.int_value, c.string_value, c.long_value, c.datetime_value, c.datetime_value2, c.boolean_value,
c.double_value, systimestamp
from t_parameter@pharoslink a, t_parametervalue@pharoslink b, t_parameterattributevalue@pharoslink c
where a.id = b.parent_id
and b.id = c.parent_id
;
commit;

--同步User
truncate table t_user;
insert into t_user (usercode, firstname, lastname, branchno, relationcode, partycode, crttime ) 
select user_code, first_name, last_name, usercompanynode_id, relation_code, thirdparty_code, systimestamp from t_secuser@pharoslink;
commit;

--同步险种代码
truncate table t_insurancecode;
create table tmp_pelementattribute as select * from t_pelementattribute@pharoslink where attribute_name in ('isInWard','productType','productSubType') ;
insert into t_insurancecode (insurancecode, insurancename, isinward, crttime, InsuranceType, InsureSubType )
select code,
       getmlsnew(description, '2') description,
       case
         when b.boolean_value is null then
          '0'
         else
          to_char(b.boolean_value)
       end isinward,
       systimestamp, c.string_value, d.string_value
  from t_pelement@pharoslink a left join tmp_pelementattribute b on a.id = b.element_id and b.attribute_name = 'isInWard'
  	left join tmp_pelementattribute c on a.id = c.element_id and c.attribute_name = 'productType'
    left join tmp_pelementattribute d on a.id = d.element_id and d.attribute_name = 'productSubType'
 where 1 = 1
   and element_type = '1015'
   and code not like '1%' and code not like '4%' and code not like '24%' and code not like '29%'
   and regexp_like(code, '[[:digit:]]{4,4}');
drop table tmp_pelementattribute;
commit;

--同步产品代码
truncate table T_ProductCode;
insert into T_ProductCode (ProductCode, ProductName) 
select insurancecode, insurancename
from t_insurancecode 
where 1=1;
commit;

--同步产品与险种关系
truncate table T_ProductInsurance;
insert into T_ProductInsurance( InsuranceCode, ProductCode )
select insurancecode, insurancecode from t_insurancecode where 1 = 1;
commit;

--同步责任代码
truncate table t_liabilitycode;
insert into t_liabilitycode ( liabilitycode, liabilityname, abbreviation, crttime ) 
select code,
getmlsnew(description, '2' ) description,
local_name, systimestamp
from t_pelement@pharoslink
where element_type ='1021';
commit;

--汇率表从老系统同步
truncate table t_exchangeRate;
insert into t_exchangeRate ( c_cur_no_1, c_cur_no_2, t_effc_tm, t_expd_tm, n_chg_rte, c_crt_cde, t_crt_tm, c_upd_cde, t_upd_tm) 
select c_cur_no_1, c_cur_no_2, t_effc_tm, t_expd_tm, n_chg_rte, c_crt_cde, t_crt_tm, c_upd_cde, t_upd_tm
from t_chg_rate@LINK_72YW
where 1=1;
commit;

/* 老汇率表
select currency_id, '01', rate_date, lead(rate_date,1, to_date('2099-12-31','yyyy-mm-dd')) over (partition by currency_id order by currency_id, rate_date) enddate, 
purchase_rate, sale_rate, change_rate, removed, systimestamp
from t_exchangeRate@pharoslink
where 1=1;
*/
