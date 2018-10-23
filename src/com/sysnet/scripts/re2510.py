import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2510','2501','2510','1',rootdir)
CloneDefine.RenameFile('2510','SX500101','SX500101','2',rootdir)

