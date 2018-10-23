import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2701','2701','2781','1',rootdir)
CloneDefine.RenameFile('2701','SX700101','SX708101','2',rootdir)
