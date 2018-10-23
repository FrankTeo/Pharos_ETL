import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2271','2201','2271','1',rootdir)
CloneDefine.RenameFile('2271','SX400101','SX400111','2',rootdir)
