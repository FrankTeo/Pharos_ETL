import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2511','2504','2511','1',rootdir)
CloneDefine.RenameFile('2511','SX500141','SX500221','2',rootdir)

