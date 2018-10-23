import CloneDefine
import sys

ins_list = ("3301","3302","3303")


s_ins_code = '3301'
if len(sys.argv) >= 2 :
    basepath=sys.argv[1]
else:
    basepath='F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'

for t_ins_code in ins_list:
    print t_ins_code
    if( t_ins_code != s_ins_code ):
        CloneDefine.clone_define(s_ins_code, t_ins_code, basepath)
    
    