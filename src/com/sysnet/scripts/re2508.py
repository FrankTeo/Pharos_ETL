import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2508','2502','2508','1',rootdir)
CloneDefine.RenameFile('2508','SX500121','SX500181','2',rootdir)

