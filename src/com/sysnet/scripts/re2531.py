import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2531','2530','2531','1',rootdir)
CloneDefine.RenameFile('2531','SX500471','SX500281','2',rootdir)
