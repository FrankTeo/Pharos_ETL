import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2502','2501','2502','1',rootdir)
CloneDefine.RenameFile('2502','SX500101','SX500121','2',rootdir)

