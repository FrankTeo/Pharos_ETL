import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2782','2701','2782','1',rootdir)
CloneDefine.RenameFile('2782','SX708101','SX708201','2',rootdir)
