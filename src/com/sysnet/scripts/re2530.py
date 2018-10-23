import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2530','2522','2530','1',rootdir)
CloneDefine.RenameFile('2530','SX500251','SX500471','2',rootdir)
