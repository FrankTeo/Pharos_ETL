import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3002','3001','3002','1',rootdir)
CloneDefine.RenameFile('3002','SX300401','SX300411','2',rootdir)
