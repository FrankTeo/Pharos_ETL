import CloneDefine
import sys

ins_list = ("3401","3402","3403","3404","3405","3406","3407","3408","3409","3410","3411","3412","3499")


s_ins_code = '3401'
if len(sys.argv) >= 2 :
    basepath=sys.argv[1]
else:
    basepath='F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'

for t_ins_code in ins_list:
    print t_ins_code
    if( t_ins_code != s_ins_code ):
        CloneDefine.clone_define(s_ins_code, t_ins_code, basepath)
    
    