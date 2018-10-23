import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3681','3601','3681','1',rootdir)
CloneDefine.RenameFile('3681','SX300501','SX300511','2',rootdir)
