create or replace procedure saveliability(product_code        in VARCHAR2,
                                          v_SOURCE_ELEMENT_ID in VARCHAR2) as
  --保存 liability
  v_ccode      varchar2(30);
  v_cstandcode varchar2(100);
  v_cname      varchar2(200);
  v_counts     number(3);
begin

  for z in (select id, SOURCE_ELEMENT_ID
              from htbiz_new.t_assignedelement
             Where assignment_code = 1010
               and target_element_id = v_SOURCE_ELEMENT_ID) loop
  
    Select t.Code,
           Nvl((Select e.String_Value
                 from htbiz_new.t_Redefinitionitem e
                Where e.Attribute_Name = 'localName'
                  And e.Assigned_Element_Id = z.id),
               getmls(t.extendeddescription,2)) 
    
      into v_ccode,  v_cname
    
      from htbiz_new.t_Pelement t
     Where t.Id = z.SOURCE_ELEMENT_ID;
  
    select count(1)
      into v_counts
      from htbiz_ods.t_productLiability
     where ProductCode = product_code
       and LiabilityCode = v_ccode;
  
    if v_counts = 0 then
    
      insert into htbiz_ods.t_productLiability
        (ProductCode, LiabilityCode, LIABILITYNAME)
      values
        (product_code, v_ccode, v_cname);
    
    end if;
    --dbms_output.put_line(p.code || v_ccode || v_cname);
  
  end loop;

end;
