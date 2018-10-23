import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2371','2101','2371','1',rootdir)
CloneDefine.RenameFile('2371','SX200101','SX200211','2',rootdir)
CloneDefine.RenameFile('2371','SX200102','SX200212','2',rootdir)
CloneDefine.RenameFile('2371','SX200103','SX200213','2',rootdir)