create or replace function getMLS(mls in Varchar2, lang in Varchar2) return varchar2 is
  Result varchar2(1000);
/******************************************************************************
   NAME:       getMLS
   PURPOSE:    获取多语言的中文描述
******************************************************************************/
  i_pos1 NUMBER;
  s_tmp  Varchar2(4000);
begin
     -- 先取中文,如果中文没有取道,取英文
   s_tmp := mls;
   i_pos1 := instr(mls, '<ls c="' || lang || '">');
   if (i_pos1 <= 0) then
       i_pos1 := instr(mls, '<ls c="1">');
   end if;
   if (length(s_tmp) < 10) then
      s_tmp := s_tmp || '            ';
   end if;
   s_tmp := substr(s_tmp, i_pos1 + 10);
   s_tmp := substr(s_tmp, 0, instr(s_tmp, '</ls>') - 1);

   if (s_tmp is null or trim(s_tmp) = '') then
       s_tmp := mls;
       i_pos1 := instr(mls, '<ls c="1">');
       if (length(s_tmp) < 10) then
          s_tmp := s_tmp || '            ';
       end if;
       s_tmp := substr(s_tmp, i_pos1 + 10);
       s_tmp := substr(s_tmp, 0, instr(s_tmp, '</ls>') - 1);
   end if;

  Result := s_tmp;

  return(Result);
end getMLS;
