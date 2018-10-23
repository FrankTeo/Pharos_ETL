import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2584','2525','2584','1',rootdir)
CloneDefine.RenameFile('2584','SX500441','SX500521','2',rootdir)

