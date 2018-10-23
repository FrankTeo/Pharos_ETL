create or replace procedure createLiability as

  --查询产品对应的责任保存到 ODS中

  v_id                varchar2(30);
  v_SOURCE_ELEMENT_ID varchar2(30);
  v_count             number(2);

begin
  --查询产品
  for p in (Select t.id, t.description, t.code from htbiz_new.t_pelement t Where t.element_type = 1016) loop
  
    --debug
    v_id := p.id;
  
    --dbms_output.put_line(v_id);
  
    --PN
    select SOURCE_ELEMENT_ID
      into v_SOURCE_ELEMENT_ID
      from htbiz_new.t_assignedelement
     Where assignment_code = 1017
    CONNECT BY PRIOR source_element_id = target_element_id
     START WITH target_element_id = p.id;
  
    --计算PN数量
    select count(*)
      into v_count
      from htbiz_new.t_assignedelement
     Where assignment_code = 1016
       and target_element_id = v_SOURCE_ELEMENT_ID;
  
    -- ProductNode To ProductNode
    -- 0 个 PN
    if v_count = 0 then
    
      select count(*)
        into v_count
        from htbiz_new.t_assignedelement
       Where assignment_code = 1015
         and target_element_id = v_SOURCE_ELEMENT_ID;
    
      --1015 
      if v_count = 0 then
        dbms_output.put_line('1015 counts is 0!');
      
        --1015 
      elsif v_count = 1 then
      
        select SOURCE_ELEMENT_ID
          into v_SOURCE_ELEMENT_ID
          from htbiz_new.t_assignedelement
         Where assignment_code = 1015
           and target_element_id = v_SOURCE_ELEMENT_ID;
      
        saveliability(p.code, v_SOURCE_ELEMENT_ID);
      
        --CSNODE
        select SOURCE_ELEMENT_ID
          into v_SOURCE_ELEMENT_ID
          from htbiz_new.t_assignedelement
         Where assignment_code = 1014
           and target_element_id = v_SOURCE_ELEMENT_ID;
      
        saveliability(p.code, v_SOURCE_ELEMENT_ID);
      
        --计算下层 CSNODE 数量
      
        select count(*)
          into v_count
          from htbiz_new.t_assignedelement
         Where assignment_code = 1014
           and target_element_id = v_SOURCE_ELEMENT_ID;
      
        --有一层 CSNODE
        if v_count = 0 then
        
          --计算 RCSNode To CSNode 数量
          select count(*)
            into v_count
            from htbiz_new.t_assignedelement
           Where assignment_code = 1013
             and target_element_id = v_SOURCE_ELEMENT_ID;
        
          if v_count = 1 then
            select SOURCE_ELEMENT_ID
              into v_SOURCE_ELEMENT_ID
              from htbiz_new.t_assignedelement
             Where assignment_code = 1013
               and target_element_id = v_SOURCE_ELEMENT_ID;
          
            saveliability(p.code, v_SOURCE_ELEMENT_ID);
          
          else
          
            for z in (select SOURCE_ELEMENT_ID
                        from htbiz_new.t_assignedelement
                       Where assignment_code = 1013
                         and target_element_id = v_SOURCE_ELEMENT_ID) loop
            
              saveliability(p.code, z.source_element_id);
            
              --计算 RCSNode To CSNode 数量
            
              select count(*)
                into v_count
                from htbiz_new.t_assignedelement
               Where assignment_code = 1013
                 and target_element_id = z.source_element_id;
            
              if v_count > 0 then
              
                for y in (select SOURCE_ELEMENT_ID
                            from htbiz_new.t_assignedelement
                           Where assignment_code = 1013
                             and target_element_id = z.source_element_id) loop
                
                  saveliability(p.code, y.source_element_id);
                end loop;
              
              end if;
            
            end loop;
          
          end if;
        
          --有多层 CSNODE
        elsif v_count > 0 then
        
          for z in (select SOURCE_ELEMENT_ID
                      from htbiz_new.t_assignedelement
                     Where assignment_code = 1014
                    CONNECT BY PRIOR source_element_id = target_element_id
                     START WITH target_element_id = v_SOURCE_ELEMENT_ID) loop
          
            --计算下层 CSNODE 数量
          
            select count(*)
              into v_count
              from htbiz_new.t_assignedelement
             Where assignment_code = 1014
               and target_element_id = z.source_element_id;
          
            --有一层 CSNODE
            if v_count = 0 then
            
              --计算 RCSNode To CSNode 数量
              select count(*)
                into v_count
                from htbiz_new.t_assignedelement
               Where assignment_code = 1013
                 and target_element_id = v_SOURCE_ELEMENT_ID;
            
              if v_count = 0 then
              
                saveliability(p.code, z.source_element_id);
              
              else
              
                for x in (select SOURCE_ELEMENT_ID
                            from htbiz_new.t_assignedelement
                           Where assignment_code = 1013
                             and target_element_id = v_SOURCE_ELEMENT_ID) loop
                
                  saveliability(p.code, x.source_element_id);
                
                  --计算 RCSNode To CSNode 数量
                
                  select count(*)
                    into v_count
                    from htbiz_new.t_assignedelement
                   Where assignment_code = 1013
                     and target_element_id = x.source_element_id;
                
                  if v_count > 0 then
                  
                    for y in (select SOURCE_ELEMENT_ID
                                from htbiz_new.t_assignedelement
                               Where assignment_code = 1013
                                 and target_element_id = x.source_element_id) loop
                    
                      saveliability(p.code, y.source_element_id);
                    end loop;
                  
                  end if;
                
                end loop;
              
              end if;
            
            end if;
          
          end loop;
        
        end if;
        --1015
      end if;
      --有多个 PN
    else
    
      saveliability(p.code, v_SOURCE_ELEMENT_ID);
      --PN
      for w in (select SOURCE_ELEMENT_ID
                  from htbiz_new.t_assignedelement
                 Where assignment_code = 1016
                CONNECT BY PRIOR source_element_id = target_element_id
                 START WITH target_element_id = v_SOURCE_ELEMENT_ID) loop
      
        select SOURCE_ELEMENT_ID
          into v_SOURCE_ELEMENT_ID
          from htbiz_new.t_assignedelement
         Where assignment_code = 1015
           and target_element_id = w.source_element_id;
      
        saveliability(p.code, v_SOURCE_ELEMENT_ID);
      
        --CSNODE
        select SOURCE_ELEMENT_ID
          into v_SOURCE_ELEMENT_ID
          from htbiz_new.t_assignedelement
         Where assignment_code = 1014
           and target_element_id = v_SOURCE_ELEMENT_ID;
      
        saveliability(p.code, v_SOURCE_ELEMENT_ID);
      
        --计算下层 CSNODE 数量
      
        select count(*)
          into v_count
          from htbiz_new.t_assignedelement
         Where assignment_code = 1014
           and target_element_id = v_SOURCE_ELEMENT_ID;
      
        --有一层 CSNODE
        if v_count = 0 then
        
          saveliability(p.code, v_SOURCE_ELEMENT_ID);
        
          select count(*)
            into v_count
            from htbiz_new.t_assignedelement
           Where assignment_code = 1013
             and target_element_id = v_SOURCE_ELEMENT_ID;
        
          if v_count = 0 then
          
            saveliability(p.code, v_SOURCE_ELEMENT_ID);
          
          else
          
            for x in (select SOURCE_ELEMENT_ID
                        from htbiz_new.t_assignedelement
                       Where assignment_code = 1013
                         and target_element_id = v_SOURCE_ELEMENT_ID) loop
            
              saveliability(p.code, x.source_element_id);
            
              --计算 RCSNode To CSNode 数量
            
              select count(*)
                into v_count
                from htbiz_new.t_assignedelement
               Where assignment_code = 1013
                 and target_element_id = x.source_element_id;
            
              if v_count > 0 then
              
                for y in (select SOURCE_ELEMENT_ID
                            from htbiz_new.t_assignedelement
                           Where assignment_code = 1013
                             and target_element_id = x.source_element_id) loop
                
                  saveliability(p.code, y.source_element_id);
                end loop;
              
              end if;
            
            end loop;
          
          end if;
        
        else
          for x in (select SOURCE_ELEMENT_ID
                      from htbiz_new.t_assignedelement
                     Where assignment_code = 1014
                    CONNECT BY PRIOR source_element_id = target_element_id
                     START WITH target_element_id = v_SOURCE_ELEMENT_ID) loop
          
            saveliability(p.code, x.source_element_id);
          
            select count(*)
              into v_count
              from htbiz_new.t_assignedelement
             Where assignment_code = 1014
               and target_element_id = x.source_element_id;
          
            if v_count = 0 then
            
              saveliability(p.code, x.source_element_id);
            
            else
            
              --有多层 CSNODE
            
              for u in (select SOURCE_ELEMENT_ID
                          from htbiz_new.t_assignedelement
                         Where assignment_code = 1014
                        CONNECT BY PRIOR source_element_id = target_element_id
                         START WITH target_element_id = v_SOURCE_ELEMENT_ID) loop
              
                --计算下层 CSNODE 数量
              
                select count(*)
                  into v_count
                  from htbiz_new.t_assignedelement
                 Where assignment_code = 1014
                   and target_element_id = u.source_element_id;
              
                --有一层 CSNODE
                if v_count = 0 then
                
                  saveliability(p.code, u.source_element_id);
                
                else
                
                  for z in (select SOURCE_ELEMENT_ID
                              from htbiz_new.t_assignedelement
                             Where assignment_code = 1014
                            CONNECT BY PRIOR source_element_id = target_element_id
                             START WITH target_element_id = u.source_element_id) loop
                  
                    --计算下层 CSNODE 数量
                  
                    select count(*)
                      into v_count
                      from htbiz_new.t_assignedelement
                     Where assignment_code = 1014
                       and target_element_id = z.source_element_id;
                  
                    --有一层 CSNODE
                    if v_count = 0 then
                    
                      --计算 RCSNode To CSNode 数量
                      select count(*)
                        into v_count
                        from htbiz_new.t_assignedelement
                       Where assignment_code = 1013
                         and target_element_id = v_SOURCE_ELEMENT_ID;
                    
                      if v_count = 0 then
                      
                        saveliability(p.code, z.source_element_id);
                      
                      else
                      
                        for x in (select SOURCE_ELEMENT_ID
                                    from htbiz_new.t_assignedelement
                                   Where assignment_code = 1013
                                     and target_element_id = v_SOURCE_ELEMENT_ID) loop
                        
                          saveliability(p.code, x.source_element_id);
                        
                          --计算 RCSNode To CSNode 数量
                        
                          select count(*)
                            into v_count
                            from htbiz_new.t_assignedelement
                           Where assignment_code = 1013
                             and target_element_id = x.source_element_id;
                        
                          if v_count > 0 then
                          
                            for y in (select SOURCE_ELEMENT_ID
                                        from htbiz_new.t_assignedelement
                                       Where assignment_code = 1013
                                         and target_element_id = x.source_element_id) loop
                            
                              saveliability(p.code, y.source_element_id);
                            end loop;
                          
                          end if;
                        
                        end loop;
                      
                      end if;
                    
                    end if;
                  
                  end loop;
                
                end if;
              
              end loop;
            end if;
          
          end loop;
        
        end if;
      
      end loop;
    
    end if;
  
  end loop;

exception
  WHEN OTHERS THEN
    dbms_output.put_line(v_id || ':' || SQLCODE || SQLERRM);
  
end;
