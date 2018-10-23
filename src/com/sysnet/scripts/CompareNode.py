import cx_Oracle
import string
import sys
import re
import pprint
from xml.dom.minidom import *

dbstr=u'bizcore_test/bizcore_test@localhost:1521/myorcl'

def diffNodeDecls(liSource, liTarget):
    for liSs in liSource:
        for liTs in liTarget:
            if( liSs[2] == liTs[2] ):
                liSs.append('Y'); liTs.append('Y')
                break
            
    print u'-----declaration which is in ', s_string, u', not in ', t_string,u'------'
    
    for liSs in liSource:
        if(len(liSs) <= 4):
            print '%-10s'%(liSs[2]),'%-25s'%(liSs[0]),'%-40s'%(liSs[1]),'%-40s'%(liSs[3])
    
    print u'\n\n\n'
    print u'-----declaration which is in ', t_string, u', not in ', s_string,u'------'
    
    for liTs in liTarget:
        if(len(liTs) <= 4):
            print '%-10s'%(liTs[2]),'%-25s'%(liTs[0]),'%-40s'%(liTs[1]),'%-40s'%(liTs[3])
    
    print u'\n\n\n'
    print u'-----declaration which is in both------'
    for liTs in liTarget:
        if(len(liTs) > 4):
            print '%-10s'%(liTs[2]),'%-25s'%(liTs[0]),'%-40s'%(liTs[1]),'%-40s'%(liTs[3])
         

def getNodeDeclsByCodeTrail(cursor, strSource):
    def_explaination='''
        get declarations which beneath contract node.
        The user must to provide a holonomic trail about the node begin contract-node-code
    '''
    iLoop=1
    cursor.execute(firstlevel_sql, p2code=strSource[0], p1code=strSource[1])
    tId, =cursor.fetchall()
    for iLoop in range(1,len(strSource)-1,1):
        cursor.execute(commonlevel_sql, p2id=tId[0], p2code=strSource[iLoop])
        tId, = cursor.fetchall()
        iLoop += 1

    cursor.execute(finallevel_sql,p2id=tId[0], p2code=strSource[iLoop])
    tSet = cursor.fetchall()
    liRet = []
    for tOne in tSet:
        liOne = []
        liOne.append(unicode(string.replace(tOne[0],'\n',''), 'gbk'))
        #deal with description
        dom_desc =  parseString(unicode(tOne[1],'gbk'))
        domRoot = dom_desc.documentElement
        pLs = domRoot.getElementsByTagName("ls")
        for pE in pLs:
            if( pE.getAttribute('c') == '2' ) :
                liOne.append(pE.childNodes[0].nodeValue)
        #give a default value when out of exception
        if(len(liOne) < 2):
            liOne.append('None')
        liOne.append(unicode(string.replace(tOne[2],'\n',''), 'gbk'))
        liOne.append(unicode(string.replace(tOne[3],'\n',''), 'gbk'))                
        liRet.append(liOne)
    
    return liRet
    

if len(sys.argv) < 3:
    s_string=u'SX500120.SX500121'    #source
#     s_string=u'SX200100.SX200101'    #source
    t_string=u'SX500490.SX500491'    #target
else:
    s_string=sys.argv[1]    #source
    t_string=sys.argv[2]    #target

strSource=s_string.split(u'''.''')
strTarget=t_string.split(u'''.''')

#procduct_node <==> common node
firstlevel_sql=u'''
    select p1.id from t_pelement p1, t_assignedelement a, t_pelement p2
    where p1.id = a.source_element_id and p2.id = a.target_element_id
    and p2.code = :p2code and p1.code = :p1code
    and p2.element_type = '1014' and p1.element_type = '1013'
    '''

#final node <==> declaration
finallevel_sql=u'''
    select p1.name,p1.description,p1.code,p1.local_name
    from t_pelement p1, t_assignedelement a, t_pelement p2
    where p1.id = a.source_element_id and p2.id=a.target_element_id
    and p2.id = :p2id
    and p2.code = :p2code
    and p1.element_type = '1003'
    '''

#common node <==> common node
commonlevel_sql=u'''
    select unique p2.id
    from t_pelement p1, t_assignedelement a, t_pelement p2
    where p1.id = a.source_element_id and p2.id=a.target_element_id
    and p2.id = :p2id and p2.code = :p2code
    and p1.element_type = '1013'
    '''
       
if len(strSource) <=1 or len(strTarget) <=1 :
    print u'error'

try:
    
    conn=cx_Oracle.connect(dbstr)
    cursor = conn.cursor()
    liRetSource = getNodeDeclsByCodeTrail(cursor,strSource)
    liRetTarget = getNodeDeclsByCodeTrail(cursor,strTarget)
    #compare the two lists
    diffNodeDecls(liRetSource, liRetTarget)

except Exception as err:
    print err
finally:
    cursor.close()
    conn.close()