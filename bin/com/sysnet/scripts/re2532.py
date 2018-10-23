import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2533','2532','2533','1',rootdir)
CloneDefine.RenameFile('2533','SX500471','SX500231','2',rootdir)
