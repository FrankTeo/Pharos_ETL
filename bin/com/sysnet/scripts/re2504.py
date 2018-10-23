import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2504','2503','2504','1',rootdir)
CloneDefine.RenameFile('2504','SX500131','SX500141','2',rootdir)
CloneDefine.RenameFile('2504','SX500132','SX500142','2',rootdir)
