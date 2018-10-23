import cx_Oracle
import string
import sys
import re
import pprint
from xml.dom.minidom import *

reload(sys)
sys.setdefaultencoding('utf-8')

def diffNodeDecls(liSource, liTarget):
    #p1.name,p1.description,p1.code,p1.local_name
    program_file=open( u'./program_file.txt', u'w' )
    program_file.write( u'else if (insrncCde.equals("' + t_string + u'") ) { \n' )
    program_file.write( u'\tif (false) {} \n' )
    for liSs in liSource:
        for liTs in liTarget:
            if( string.replace(liSs[1], u' ', u'') == string.replace(liTs[1],u' ', u'') ):
                program_file.write( u'\t/*' + liSs[1] + u'*/\n')
                program_file.write( u'\telse if(rdrCode.equals("' + liTs[2] + u'")) {\n' )
                program_file.write( u'\t\tnonlife = node.getConditionNonLife("' + liSs[2] + u'");\n' )
                program_file.write( u'\t\tnonlife.setInclude(true);\n' )
                program_file.write( u'\t\treturn nonlife;\n' )
                program_file.write( u'\t}\n')
                liSs.append('Y'); liTs.append('Y')
                #break

    program_file.write( u'\telse{\n' )
    program_file.write( u'\t\tthrowrdrCode(rdrCode,node);\n' )
    program_file.write( u'\t}\n' )
    program_file.write( u'}\n' )

    
    false_file=open( u'./false_file.txt', u'w' )
    print u'-----condition which is in ', s_string, u', not in ', t_string, u' ------'
    
    false_file.write(u'-----condition which is in ' + s_string + u' ------\n')
    for liSs in liSource:
        if(len(liSs) <= 4):
            false_file.write('%-10s'%(liSs[2]) + '%-25s'%(liSs[1]) + u'\n')
            print '%-10s'%(liSs[2]),'%-25s'%(liSs[1])
    
    print u'\n\n\n'
    print u'-----condition which is in ', t_string, u', not in ', s_string, u' ------'
    
    false_file.write(u'\n\n\n-----condition which is in ' + t_string + u' ------\n')
    for liTs in liTarget:
        if(len(liTs) <= 4):
            false_file.write('%-10s'%(liTs[2]) + '%-25s'%(liTs[1]) + u'\n')
            print '%-10s'%(liTs[2]),'%-25s'%(liTs[1])
            program_file.write( u'//\t/*' + liSs[1] + u'*/\n')
            program_file.write( u'//\telse if(rdrCode.equals("' + liTs[2] + u'")) {\n' )
            program_file.write( u'//\t\tnonlife = node.getConditionNonLife("undefined");\n' )
            program_file.write( u'//\t\tnonlife.setInclude(true);\n' )
            program_file.write( u'//\t\treturn nonlife;\n' )
            program_file.write( u'//\t}\r\n')
    false_file.close()    
    program_file.close() 

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
    
def getOldSystemCondition(cursor,strTarget):
    iLoop=1
    cursor.execute(oldcondition_sql, icde=strTarget)
    tSet =cursor.fetchall()

    liRet = []
    for tOne in tSet:
        liOne = []
        #p1.name,p1.description,p1.code,p1.local_name
        liOne.append(unicode(string.replace(tOne[1],'\n',''), 'gbk'))
        #deal with description
        liOne.append(unicode(string.replace(tOne[1],'\n',''), 'gbk'))
        liOne.append(unicode(string.replace(tOne[0],'\n',''), 'gbk'))
        liOne.append(unicode(string.replace(tOne[1],'\n',''), 'gbk'))                
        liRet.append(liOne)
        
    return liRet

if len(sys.argv) < 3:
    s_string=u'SX500101.SX500101'    #source
    t_string=u'2501'    #target
else:
    s_string=sys.argv[1]    #source
    t_string=sys.argv[2]    #target

strSource=s_string.split(u'''.''')
strTarget=t_string

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
    and p1.element_type = '1021'
    '''

#common node <==> common node
commonlevel_sql=u'''
    select unique p2.id
    from t_pelement p1, t_assignedelement a, t_pelement p2
    where p1.id = a.source_element_id and p2.id=a.target_element_id
    and p2.id = :p2id and p2.code = :p2code
    and p1.element_type = '1013'
    '''

#old system
oldcondition_sql=u'''
    select c_rdr_cde, c_rdr_cnm from t_insrnc_typ where c_insrnc_cde = :icde
    '''
       
if len(strSource) <=1 or len(strTarget) <1 :
    print u'error'

try:
    #retrieve new system
    conn=cx_Oracle.connect(u'bizcore_test/bizcore_test@10.2.22.25/rac')
    cursor = conn.cursor()
    liRetSource = getNodeDeclsByCodeTrail(cursor,strSource)
    cursor.close()
    conn.close()
    
    #retrieve old system
    conn=cx_Oracle.connect(u'htywdb/123456@10.2.19.40/pharos40')
    cursor = conn.cursor()
    liRetTarget = getOldSystemCondition(cursor,strTarget)
    #compare the two lists
    diffNodeDecls(liRetSource, liRetTarget)

except Exception as err:
    print err
finally:
    cursor.close()
    conn.close()