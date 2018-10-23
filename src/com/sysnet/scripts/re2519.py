import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2519','2503','2519','1',rootdir)
CloneDefine.RenameFile('2519','SX500131','SX500371','2',rootdir)
CloneDefine.RenameFile('2519','SX500132','SX500372','2',rootdir)
