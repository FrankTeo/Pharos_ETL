import CloneDefine
import sys

inslist=[
        '2112','SX200151',
        '2113','SX200151',
        '2114','SX200171',
        '2204','SX200171',
        '2303','SX200181',
        '2304','SX200191',
        '3614','SX300521',
        '3615','SX300531',
        '2542','SX500106',
        '2585','SX500106',
        '2508','SX500181',
        '2509','SX500190',
        '2650','SX500201',
        '2601','SX500211',
        '2535','SX500241',
        '2521','SX500260',
        '2513','SX500300',
        '2537','SX500310',
        '2538','SX500320',
        '2514','SX500341',
        '2515','SX500350',
        '2520','SX500381',
        '2560','SX500401',
        '2523','SX500421',
        '2563','SX500430',
        '2582','SX500491',
        '2590','SX500491',
        '2599','SX500531',
        '2550','SX500540',
        '2570','SX500550',
        '2551','SX500560',
        '2562','SX500570',
        '2575','SX500580',
        '2001','SX500590',
        '2051','SX500600',
        '2571','SX500610',
        '2581','SX500620',
        '2603','SX506101',
        '2604','SX506101',
        '2605','SX506201',
        '2610','SX506301',
        '2541','SX506401',
        '2681','SX506501',
        '2611','SX506601',
        '2708','SX600101',
        '2788','SX600111',
        '2709','SX600121',
        '2789','SX600131',
        '2801','SX806101',
        '2802','SX806201',
        '2881','SX806301',
        '2882','SX806401',
        '2803','SX806501'
        ]
if len(sys.argv) < 2:
    rootdir = u'''F:\DCMS\workspace\HuaTai_ODS_GA_NOWEB\src\composite'''
else:
    rootdir = sys.argv[1]

for ins in range(0, len(inslist)-1, 2):
    CloneDefine.clone_define('3616', inslist[ins], rootdir)
    f = open(rootdir+'\P'+inslist[ins]+'\undefined.txt','w+')
#     f.writelines(u'xxxxx')
    f.close()
    CloneDefine.RenameFile(inslist[ins],'3616',inslist[ins],'1',rootdir)
    CloneDefine.RenameFile(inslist[ins],'SX300501',inslist[ins+1],'2',rootdir)
    print inslist[ins], inslist[ins+1]

# CloneDefine.RenameFile('2180','2382','2180','1',rootdir)
# CloneDefine.RenameFile('2180','SX200211','SX200241','2',rootdir)
# CloneDefine.RenameFile('2180','SX200212','SX200242','2',rootdir)
# CloneDefine.RenameFile('2180','SX200213','SX200243','2',rootdir)
