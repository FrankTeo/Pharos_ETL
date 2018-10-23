import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3601','3401','3601','1',rootdir)
CloneDefine.RenameFile('3601','SX300101','SX300501','2',rootdir)
