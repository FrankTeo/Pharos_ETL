import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3005','3001','3005','1',rootdir)
CloneDefine.RenameFile('3005','SX300411','SX300421','2',rootdir)
