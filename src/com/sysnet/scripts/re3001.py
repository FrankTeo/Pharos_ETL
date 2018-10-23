import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3001','3401','3001','1',rootdir)
CloneDefine.RenameFile('3001','SX300101','SX300401','2',rootdir)
