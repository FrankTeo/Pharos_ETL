create or replace function GETMLSNEW(mls in Varchar2, lang in Varchar2) return varchar2 is
  Result varchar2(4000);
/******************************************************************************
   NAME:       getMLSnew
   PURPOSE:    获取语言描述
******************************************************************************/
  i_pos1 NUMBER;
  s_tmp  Varchar2(4000);
  olang varchar2(10);
  s_search varchar2(200);
begin
  --先把所有的空格回车去掉
  s_tmp := replace(replace(replace(mls, '<ls c=', '<lsc='),chr(10)), chr(13));
  -- 先按给定的取,
  s_search := '<lsc="' || lang || '">';
  i_pos1 := instr(s_tmp, s_search);
  --如果没有取到,中文取英文，英文取中文
  if(lang = '2') then
    olang := '1';
  elsif(lang = '1' ) then
    olang :='2';
  else
    return( '');
  end if;
  if (i_pos1 <= 0) then
     s_search := '<lsc="' || olang || '">';
     i_pos1 := instr(s_tmp, s_search);
  end if;
  --把前边的字符串去掉
  s_tmp := substr(s_tmp, i_pos1);
  
  --在找结尾标志
  i_pos1 := instr(s_tmp, '</ls>' );
  if(i_pos1 <= 0 ) then
     return ('');
  end if;
  
  s_tmp := replace(substr(s_tmp,1, i_pos1-1), s_search);
  Result := s_tmp;

  return(Result);
end GETMLSNEW;