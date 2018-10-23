import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2301','2101','2301','1',rootdir)
CloneDefine.RenameFile('2301','SX200101','SX200201','2',rootdir)
CloneDefine.RenameFile('2301','SX200102','SX200202','2',rootdir)
CloneDefine.RenameFile('2301','SX200103','SX200203','2',rootdir)