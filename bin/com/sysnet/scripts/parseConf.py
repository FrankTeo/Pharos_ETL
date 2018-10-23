#!/usr/bin/python
# -*- coding: utf8 -*-

import sys
import re
import os.path
sys.path.append(u'F:/DCMS/workspace/HuaTai_ODS_GA_NOWEB/src/com/sysnet/scripts')
import commonutil
import string


    
class OdsMapping:
    def __init__(self):
        self.isCommentary=''
        self.parameterType=''
        self.dataProvider=''
        self.parameter=''
        self.method=''
        self.value=''

    def parseMapFeild(self, str):
        
        str = commonutil.trim(str,'\n')
        if(str == '') :
            self.isCommentary='Y'
            return
        
        self.isCommentary='N'
        list = str.split('|')
        #parameterType 由于配置文件的篇幅有限, 采取简写, 在这里转换, 对应关系如下
        if( list[0] == 'OB'):   #当前对象
            self.parameterType = 'OBJECT'
        elif(list[0] == 'GL'):  #全局
            self.parameterType = 'GLOBAL'
        #这个处理一个节点上有超过一个的DF,又必须要抽到一张数据库表的情况.Unfortunately, 还没验证,需求就消失了
        elif(list[0] == 'DF1' or list[0] == 'DF2' or list[0] == 'DF3' or list[0] == 'DF4'):
            self.parameterType = 'DECLARATIONAL' + list[0][2:3]
        elif(list[0] == 'DFL'): #DFL
            self.parameterType = 'DECLARATIONFORMLIST'
        elif(list[0] == 'DF'):  #party的DFL
            self.parameterType = 'DFL'
        elif(list[0] == 'CN'):  #ContractNode合同节点
            self.parameterType = 'CSNODE'
        elif(list[0] == 'RL'):  #Role角色
            self.parameterType = 'ROLE'
        elif(list[0] == 'SD'):  #销售费用
            self.parameterType = 'SALESFEEDETAIL'
        elif(list[0] == 'MC'):  #多经济多代理
            self.parameterType = 'MULTICHANNELINFO'
        elif(list[0] == 'IS'):  #缴费计划
            self.parameterType = 'INSTALLMENT'
        elif(list[0] == 'PS'):  #缴费计划明细
            self.parameterType = 'PAYMENTSCHEDULE'
        elif(list[0] == 'CI'):  #共保
            self.parameterType = 'COINSURANCE'
        elif(list[0] == 'JI'):  #联保
            self.parameterType = 'JOINSURANCE'
        elif(list[0] == 'UW'):  #核保记录
            self.parameterType = 'UWHISTORY'
        elif(list[0] == 'NC'):  #非寿险责任
            self.parameterType = 'NONCONDITION'
        else:
            print '错误的Mapping配置['+str+'],忽略!'
        
        # dataProvider
        if( list[1] == 'OB'):
            self.dataProvider = 'OBJECT'
        elif(list[1] == 'GL'):
            self.dataProvider = 'GLOBAL'
        elif(list[1] == ''):
            pass
        else:
            self.dataProvider = list[1]
            
        if( list[0] != 'DF'):
            #数据项的配置为了方便,一般写法是写在method位.
            #数据项有其特殊的编码规则, 新商险共两种 
            #1. 新商险定义的以SX开头, 中间有一位字符,最后是5位编码
            #2. 个险延续下来的数据项则全是数字
            #Mint:也可以采取通用的配法,将数据项编码配置到parameter位上,method位写getElementByCode.getValue
            #Mint:也可以采取个险的配法,将数据项名称配置到parameter位上,method位写getElementByName.getValue
            if(re.match('^SX[\w]{1,1}[0-9]{5,5}$', list[3]) or re.match('^[0-9]{1,10}$', list[3])):
                self.parameter=list[3]
                self.method='getElementByCode.getValue'
                
            #GETDAMOUNT--取money类的金额
            elif(list[3] == 'GETDAMOUNT'):
                self.parameter=list[2]
                self.method='getElementByCode.getValue.getAmount'
            
            #GETDCURRENCY--取money类的币种
            elif(list[3] == 'GETDCURRENCY'):
                self.parameter=list[2]
                self.method='getElementByCode.getValue.getCurrency'
                
            #GETTERMSTART--取term类的起期
            elif(list[3] == 'GETTERMSTART'):
                self.parameter=list[2]
                self.method='getElementByCode.getValue.getStartdate'
                
            #GETTERMEND--取term类的止期
            elif(list[3] == 'GETTERMEND'):
                self.parameter=list[2]
                self.method='getElementByCode.getValue.getEnddate'
            
            #固定值
            elif(list[3].startswith('pass:')):
                self.value=list[3]
                    
            #否则就是一个method. method支持'.',但所有的方法只可以传一个参数,写在parameter位上
            else:
                if(list[2] != ''):
                    self.parameter=list[2]
                if(list[3] != ''):
                    self.method=list[3]
        
        #DFL的配制方法    
        else:
            self.parameter=list[3]

# ODS字段
class OdsColumn:
    def __init__(self):
        self.originalLine=''
        self.serial=''
        self.chineseName=''
        self.englishName=''
        self.typeLength=''
        self.isKey=''
        self.nullable=''
        self.default=''
        self.remark=''
        self.mapping=''
        self.om = OdsMapping()

    def parseLine(self, strLine):
        strLine=commonutil.trim(strLine,'\n')
        self.originalLine = strLine
        if(strLine.endswith('\t')):
            strLine=strLine + '\t1'
        list=strLine.split('\t', 11)
        print list
        newlist=list[0].split('.');
        self.serial=newlist[0]
        self.chineseName=list[2]
        self.englishName=list[3]
        self.typeLength=list[4]
        self.isKey=list[5]
        self.nullable=list[6]
        if( list[7] == 'SD' ):
            self.default='sysdate'
        elif(list[7] == 'INS'):
            self.default='INSERT'
        else:
            self.default=list[7]
        self.remark=list[8]
        self.mapping=list[9]
        
        self.parseMapping()
            
    def parseMapping(self):
        self.om.parseMapFeild(self.mapping)

# ODS表
class OdsTable:
    def __init__(self):
        self.originalName=''
        self.chineseName=''
        self.englishName=''
        self.nodeTypeNo=''
        self.type=''
        self.foreignKey=[]
        self.indexKey=[]
        self.TableCol={}
        self.crtTbl=''
        self.dropTbl=''
        self.addPk=''
        self.dropPk=''
        self.delete=''
        self.select=''
        self.addFk=''
        self.dropFk=''
        self.addIdx=''
        self.dropIdx=''
        self.comment=''
        self.pgCrtTbl=''
        self.grantHtdw=''
        self.columnNum=0

    def setType(self, str):
        if(re.match('ETLReplicate', str)):
            self.type = 'FakeTable'
        else:
            self.type = 'Table'
    
    def parseTableNameLine(self, strLine):
        self.originalName = strLine
        self.setType(strLine)
        list=commonutil.tsplit(strLine, '\t():\n')
        
        if( self.type == 'Table'):
            self.chineseName = list[1]
            self.englishName = list[2]
            self.nodeTypeNo = 'common'
        elif( self.type == 'FakeTable'):
            self.chineseName = string.replace(list[0], 'ETLReplicate', '')
            self.englishName = list[1]
            self.nodeTypeNo = list[3]
            
    def parseColumnLine(self, strLine):
        col=OdsColumn()
        col.parseLine(strLine)
        self.TableCol.update({col.serial:col})
        
    def parseLine(self, strLine):
        strLine = commonutil.trim(strLine, '\n')
        if(re.match('ETLReplicate', strLine) or re.match('^[4]\.[0-9]\.[0-9]\t*', strLine)):
            self.parseTableNameLine(strLine)
        elif(re.match(u'^险种[.]{,}', strLine)):
            pass
        elif(re.match('^[0-9]{1,}\.', strLine)):
            self.columnNum += 1;
            self.parseColumnLine(strLine)
        elif(re.match(u'^外键[.]{,}', strLine)):
            list=strLine.split('\t')
            if(list[1] != u'无' and list[1] != ''):
                self.foreignKey.append(list[1])
        elif(re.match(u'^索引[.]{,}', strLine)):
            list=strLine.split('\t')
            if(list[1] != u'无' and list[1] != ''):                   
                self.indexKey.append(list[1])
        elif(re.match('\s*', strLine)): #空行
            pass
        else:
            print 'undefined line'
            
    def Orcl2Pg(self, tl=''):
        new = tl.upper()
        if( new.startswith('VARCHAR2') ):
            new = 'character varying' + new.replace('VARCHAR2', '')
        elif( new.startswith('NUMBER')):
            new = 'numeric' + new.replace('NUMBER', '')
        elif(new.startswith('DECIMAL')):
            new = 'numeric' + new.replace('DECIMAL', '')
        elif(new.startswith('DATE')):
            new = 'timestamp(0) without time zone'
        elif( new.startswith('TIMESTAMP')):
            new = 'timestamp(6) without time zone'
        return new
      
    # 组装各种SQL
    def combineSql(self):
        if(self.type == 'Table'):
            self.crtTbl = u'--创建' + self.chineseName + '\n'        
            self.crtTbl = self.crtTbl + 'CREATE TABLE ' + self.englishName + '(\n'
            keyTmp=''
            self.comment += u'''COMMENT ON TABLE ''' + self.englishName + ' IS ' + '\'' + self.chineseName + '\';\n'
            self.select += u'SELECT '
            self.pgCrtTbl = u'--创建' + self.chineseName + '\n'        
            self.pgCrtTbl = self.pgCrtTbl + 'CREATE TABLE ' + 'ods_prh_'+self.englishName[2:] + '(\n'
            self.grantHtdw = self.grantHtdw + 'grant select on ' + self.englishName + ' to htdw;\n'
            nReturnNum = 0
            for nkey in range(1, self.columnNum+1, 1):
                strKey = str(nkey)
                
                self.comment += u'''COMMENT ON COLUMN ''' \
                    + self.englishName + u'''.''' + self.TableCol[strKey].englishName + u' IS '
                self.crtTbl = self.crtTbl + '\t' + self.TableCol[strKey].englishName + ' ' + self.TableCol[strKey].typeLength
                if( self.TableCol[strKey].default != '' ):
                    if( self.TableCol[strKey].typeLength.startswith('VARCHAR') or self.TableCol[strKey].typeLength.startswith('CHAR')):
                        self.crtTbl = self.crtTbl + ' DEFAULT \'' + self.TableCol[strKey].default + '\''
                    else:
                        self.crtTbl = self.crtTbl + ' DEFAULT ' + self.TableCol[strKey].default
                if( self.TableCol[strKey].nullable != ''):
                    self.crtTbl = self.crtTbl + ' NOT NULL '
                if( self.TableCol[strKey].englishName != 'MdfTime'):
                    self.crtTbl = self.crtTbl + ','
                self.crtTbl += '\t--' + self.TableCol[strKey].chineseName
                self.comment += '\'' + self.TableCol[strKey].chineseName
                if(self.TableCol[strKey].remark != ''):
                    self.crtTbl += ',' + self.TableCol[strKey].remark
                    self.comment += ',' + self.TableCol[strKey].remark
                self.comment += '\';\n'
                self.select += self.TableCol[strKey].englishName + ','
                self.pgCrtTbl = self.pgCrtTbl + '\t' + self.TableCol[strKey].englishName + ' ' + self.Orcl2Pg(self.TableCol[strKey].typeLength)

                if( self.TableCol[strKey].nullable != ''):
                    self.crtTbl = self.crtTbl + ' NOT NULL '
                if( self.TableCol[strKey].englishName != 'MdfTime'):
                    self.crtTbl = self.crtTbl + ','
                    self.pgCrtTbl = self.pgCrtTbl + ','
                self.crtTbl += '\t--' + self.TableCol[strKey].chineseName
                self.pgCrtTbl += '\t--' + self.TableCol[strKey].chineseName
                nReturnNum += 1
                if( nReturnNum % 5 == 0 ):
                    self.select += '\n'
                    
                self.crtTbl = self.crtTbl + '\n' 
                self.pgCrtTbl = self.pgCrtTbl + '\n' 
                # 处理主键
                if( self.TableCol[strKey].isKey.lower() == 'PK'.lower()):
                    keyTmp += self.TableCol[strKey].englishName + ','
                    
            self.crtTbl = self.crtTbl + ');\n'
            self.pgCrtTbl = self.pgCrtTbl + ');\n'
            self.dropTbl = 'DROP TABLE ' + self.englishName + ';\n'  
            self.delete = 'TRUNCATE TABLE ' + self.englishName + ';\n'
            if( keyTmp != None and keyTmp != '' ):
                self.addPk = 'ALTER TABLE ' + \
                    self.englishName + ' ADD CONSTRAINT pk_' + self.englishName + \
                    ' PRIMARY KEY (' + keyTmp[0:len(keyTmp)-1] + ');\n'
                self.dropPk = 'ALTER TABLE ' + \
                    self.englishName + ' DROP CONSTRAINT pk_' + self.englishName + ';\n'
            self.select += 'rowid\nFROM ' + self.englishName + '\nWHERE 1=1;\n'

            for fk in self.foreignKey:
                self.addFk += 'ALTER TABLE ' + self.englishName + ' ADD CONSTRAINT ' + fk + ';\n'
                list = fk.split(' ')
                self.dropFk += 'ALTER TABLE ' + self.englishName + ' DROP CONSTRAINT' + list[0] + ';\n'
            for idx in self.indexKey:
                if(idx.lower().startswith('unique')):
                    key = 'UNIQUE INDEX'
                else:
                    key = 'INDEX'
                idx = commonutil.trim(string.replace(idx.upper(), 'UNIQUE', ''), ' ')
                list = idx.split(' ')
                
                self.addIdx += 'CREATE ' + key + ' ' + list[1] + ' ON ' + self.englishName + ' ' + list[2] + ';\n'
                self.dropIdx += 'DROP INDEX ' + list[1] + ';\n' 
        else:
            self.select += u'SELECT '
            nReturnNum = 0
            print self.TableCol
            for nkey in range(1, self.columnNum+1, 1):
                strKey = str(nkey)
                
                self.select += self.TableCol[strKey].englishName + ','
                nReturnNum += 1
                if( nReturnNum % 5 == 0 ):
                    self.select += '\n'
            
            self.select += 'rowid\nFROM ' + self.englishName + '\nWHERE 1=1;\n'
    
        self.outFileKey = {'ODS_CRTTBL':self.crtTbl, #建表
                           'ODS_DROPTBL':self.dropTbl, #删表
                           'ODS_DELETE':self.delete, #删数据
                           'ODS_SELECT':self.select,  #查数据
                           'ODS_ADDPK':self.addPk,  #加主键
                           'ODS_DROPPK':self.dropPk, #删主键
                           'ODS_COMMENT':self.comment,#加注释
                           'ODS_ADDFK':self.addFk,  #加外键
                           'ODS_DROPFK':self.dropFk, #删外键
                           'ODS_ADDIDX':self.addIdx, #加索引
                           'ODS_DROPIDX':self.dropIdx,#删索引
                           'ODS_PGCRTTBL':self.pgCrtTbl,#生成PG建表
                           'ODS_GRANTHTDW':self.grantHtdw,#grant
                           }
        
    def outputSql(self):
        backDir = rootdir + ur'/' + self.batch
        if(not os.path.exists(backDir) ):
            os.mkdir(backDir)
            
        for s in self.outFileKey:
            fOut = open(backDir + '/' + s + u'.txt', 'a')
            fOut.write(self.outFileKey[s])
            fOut.close()
        
    #组装mappingXml
    def combineXml(self):
        strTmp = ''
        strTmp += u'''<?xml version="1.0" encoding="UTF-8"?>\n'''
        strTmp += u'''<definition description="''' + self.chineseName + u'''">\n'''
        strTmp += u'''\t<mapping>\n'''
        strTmp += u'''\t\t<area description="'''+ self.chineseName + u'''">\n'''
        strTmp += u'''\t\t\t<table tableName="'''+ self.englishName + u'''" dataProviderType="CSNODE" as="Contract">\n'''
        for nkey in range(1, self.columnNum+1, 1):
            strKey = str(nkey)
            strTmp += u'''\t\t\t\t'''
            if( self.TableCol[strKey].om.isCommentary == 'Y'):
                strTmp += u'''<!--'''
            strTmp += u'''<item fieldName="''' + self.TableCol[strKey].englishName + u'''" fieldType="'''
            

            if(re.match('VARCHAR|CHAR', self.TableCol[strKey].typeLength.upper())): 
                strTmp += u'''String'''
            elif(re.match('DECIMAL|FLOAT', self.TableCol[strKey].typeLength.upper())):
                strTmp += u'''Float'''
            elif(re.match('INTEGER|LONG', self.TableCol[strKey].typeLength.upper())):
                strTmp += u'''Long'''
            elif(re.match('DATE|TIMESTAMP|DATETIME', self.TableCol[strKey].typeLength.upper())):
                strTmp += u'''Date'''
            else:
                print '未知的数据类型['+self.TableCol[strKey].typeLength+'],忽略'
            strTmp += u'"'
            
            if(self.TableCol[strKey].om.parameterType!=''):
                strTmp += u''' parameterType="''' + self.TableCol[strKey].om.parameterType + u'''"'''
                if(self.TableCol[strKey].om.parameterType =='GLOBAL'):
                    strTmp += u''' dataProvider="GLOBAL"'''
            if(self.TableCol[strKey].om.parameter!=''):
                strTmp += u''' parameter="''' + self.TableCol[strKey].om.parameter + u'''"'''
            if(self.TableCol[strKey].om.method!=''):
                strTmp += u''' method="''' + self.TableCol[strKey].om.method + u'''"'''
            if(self.TableCol[strKey].om.value!=''):
                strTmp += u''' value="''' + self.TableCol[strKey].om.value + u'''"'''
            
            strTmp += u''' />'''
            
            if( self.TableCol[strKey].om.isCommentary == 'Y'):
                strTmp += u'''-->'''
            strTmp += u''' <!-- ''' + self.TableCol[strKey].chineseName + u''' -->\n'''
                
        strTmp += u'''\t\t\t</table>\n\t\t</area>\n\t</mapping>\n</definition>\n'''
        self.mappingXml = strTmp
        self.mappingName = 'huatai_' + self.englishName.lower() + '_' + self.nodeTypeNo.upper() + '.xml'
#         fOut = open(rootdir + '/' + mappingName, 'w')
#         fOut.write(strTmp)
#         fOut.close()
    
    def outputXml(self):
        backDir = rootdir + ur'/' + self.batch
        if(not os.path.exists(backDir) ):
            os.mkdir(backDir)

        fOut = open(backDir + ur'/' + self.mappingName, 'w')
        fOut.write(self.mappingXml)
        fOut.close()

    def setBatch(self, strTime):
        self.batch = strTime
                
class OdsConfig:
    def __init__(self):
        self.tbl=OdsTable()
        import time
        self.batch=time.strftime("%Y%m%d%H%M%S",time.localtime(time.time()))
        self.tbl.setBatch(self.batch)
        self.Tables={}
    
    def parseLine(self, strLine):
        strLine = commonutil.trim(strLine, '\n')
        print '------------',strLine
        if(re.match('^ETLReplicate', strLine) or re.match('^4\.[0-9]\.[0-9]\t*', strLine) or re.match('^ENDOFFILE', strLine)):
            if( self.tbl.englishName != '' ):
                self.tbl.combineSql()
                self.tbl.combineXml()
                self.Tables.update({self.tbl.englishName+'_'+self.tbl.nodeTypeNo:self.tbl})                
                self.tbl = OdsTable()
                self.tbl.setBatch(self.batch)
                
            self.tbl.parseLine(strLine)
        elif(strLine.startswith('4.2\t保单') 
             or strLine.startswith('4.1\t当事方') 
             or strLine.startswith('4.3\t理赔')
             or strLine.startswith('4.4\t收付费')
             or strLine.startswith('4.5\t其它')):
            pass
        else:
            self.tbl.parseLine(strLine)


if len(sys.argv) < 2:
    rootdir = os.path.normcase(ur'D:\temp')
else:
    rootdir = sys.argv[1]
    
fIn = open( rootdir+u'\\line.txt', u'r' )

strLine=''
oc = OdsConfig()
for strLine in fIn:
    strLine = unicode(strLine, 'gbk')
    
    oc.parseLine(strLine)

oc.parseLine('ENDOFFILE')

for tbl in oc.Tables:
    oc.Tables[tbl].outputXml()
    oc.Tables[tbl].outputSql()