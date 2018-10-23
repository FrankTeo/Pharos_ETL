import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2507','2501','2507','1',rootdir)
CloneDefine.RenameFile('2507','SX500101','SX500170','2',rootdir)
