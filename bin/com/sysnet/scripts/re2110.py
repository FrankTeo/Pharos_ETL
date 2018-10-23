import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2110','2101','2110','1',rootdir)
CloneDefine.RenameFile('2110','SX200101','SX200131','2',rootdir)
CloneDefine.RenameFile('2110','SX200102','SX200132','2',rootdir)
CloneDefine.RenameFile('2110','SX200103','SX200133','2',rootdir)