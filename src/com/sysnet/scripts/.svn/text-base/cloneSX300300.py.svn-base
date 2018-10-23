import CloneDefine
import sys

ins_list = ("3501","3502","3503","3504","3505","3506","3507","3508","3509","3510","3599")


s_ins_code = '3501'
if len(sys.argv) >= 2 :
    basepath=sys.argv[1]
else:
    basepath='F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'

for t_ins_code in ins_list:
    print t_ins_code
    if( t_ins_code != s_ins_code ):
        CloneDefine.clone_define(s_ins_code, t_ins_code, basepath)
    
    