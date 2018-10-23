import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2106','2101','2106','1',rootdir)
CloneDefine.RenameFile('2106','SX200101','SX200161','2',rootdir)
CloneDefine.RenameFile('2106','SX200102','SX200162','2',rootdir)
CloneDefine.RenameFile('2106','SX200103','SX200163','2',rootdir)