import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3501','3401','3501','1',rootdir)
CloneDefine.RenameFile('3501','SX300101','SX300301','2',rootdir)
