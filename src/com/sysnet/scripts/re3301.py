import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('3301','3501','3301','1',rootdir)
CloneDefine.RenameFile('3301','SX300301','SX300201','2',rootdir)
