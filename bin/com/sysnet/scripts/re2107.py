import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2107','2101','2107','1',rootdir)
CloneDefine.RenameFile('2107','SX200101','SX200121','2',rootdir)
CloneDefine.RenameFile('2107','SX200102','SX200122','2',rootdir)
CloneDefine.RenameFile('2107','SX200103','SX200123','2',rootdir)