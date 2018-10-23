import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2786','2701','2786','1',rootdir)
CloneDefine.RenameFile('2786','SX700101','SX708601','2',rootdir)
