import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2532','2530','2532','1',rootdir)
CloneDefine.RenameFile('2532','SX500471','SX500291','2',rootdir)
