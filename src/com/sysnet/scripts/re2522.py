import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2522','2511','2522','1',rootdir)
CloneDefine.RenameFile('2522','SX500221','SX500251','2',rootdir)
