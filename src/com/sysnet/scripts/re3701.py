import CloneDefine
import sys

if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

CloneDefine.RenameFile('2180','2382','2180','1',rootdir)
CloneDefine.RenameFile('2180','SX200211','SX200241','2',rootdir)
CloneDefine.RenameFile('2180','SX200212','SX200242','2',rootdir)
CloneDefine.RenameFile('2180','SX200213','SX200243','2',rootdir)
