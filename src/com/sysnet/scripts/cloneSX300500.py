import CloneDefine
import sys

ins_list = ("3601","3602","3603","3604","3605","3606","3607","3608","3609","3610","3611","3612","3613","3616","3617","3699")


s_ins_code = '3601'
if len(sys.argv) >= 2 :
    basepath=sys.argv[1]
else:
    basepath='F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'

for t_ins_code in ins_list:
    print t_ins_code
    if( t_ins_code != s_ins_code ):
        CloneDefine.clone_define(s_ins_code, t_ins_code, basepath)
    
    