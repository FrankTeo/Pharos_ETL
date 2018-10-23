#!/usr/bin/python
# -*- coding: utf-8 -*-


import wx
import wx.gizmos
import sys
import zlib
import os
import cx_Oracle
import struct
import string
import pprint
import commonutil
import copy
import xml.dom.minidom as minidom
import xml.sax 
import xml.sax.handler

class XMLHandler(xml.sax.handler.ContentHandler): 
    def __init__(self): 
        self.buffer = ""                   
        self.mapping = {}                 
 
    def startElement(self, name, attributes): 
        self.buffer = ""                   
 
    def characters(self, data): 
        self.buffer += data                     
 
    def endElement(self, name): 
        self.mapping[name] = self.buffer          
 
    def getDict(self): 
        return self.mapping 

__author__ = {'name' : 'Frank Zhang',
              'mail' : 'zhvngfvn@gmail.com',
              'blog' : 'nothing',
              'QQ': '454166988',
              'created' : '2014-05-02'}

# strOracleConnect = u'''bizcore_test/bizcore_test@10.2.22.25:1521/rac'''
# strOracleConnect = u'''bizcore/bizcore@10.2.19.40:1521/pharos40'''
# strOracleConnect = u'''bizcore_test/bizcore_test@localhost:1521/myorcl'''
# strOracleConnect = u'''biztest/biztest1@10.2.22.25:1521/rac'''
strOracleConnect = u'''htbiz_new/htbiz_new@10.2.22.19:1521/rac'''


SqlDic = {
          u'NodeChld': u'''select a.id, a.policy_level_flag, a.risk_level_flag 
          from t_bcontractnode a, t_bcontractnodexml b
          where a.id = b.contract_node_id and b.contract_id = :strCntrctId''',
          # 取保单合同ID
          u'getPolicyContractId': u'''select unique contract_id from t_bcontractnode 
          where policy_no = :strPlyNo and endorse_no is null''',
          # 取批单合同ID
          u'getEndorseContractId': u'''select unique contract_id from t_bcontractnode 
          where endorse_no = :strEdrNo''',
          # 取节点树
          u'getNodeTree': u'''select a.id, a.policy_level_flag, a.risk_level_flag, 
          a.node_no, a.display_name, a.dcs_node_code 
          from t_bcontractnode a, t_bcontractnodexml b 
          where 1 = 1 and a.id = b.contract_node_id and b.contract_id = :contract_id
          and regexp_like(node_no, :regexp)''',
          # 更新Xml
          u'updNodeXml':u'''update t_bcontractnodexml set xml=:xml where contract_node_id=:id''',
          u'updPartyXml':u'''update t_partyvalue set party_value=:xml where party_id=:id''',
          u'updPartyLogXml':u'''update t_partyvalue_log set party_value=:xml where party_id=:id''',
          u'NodeXml': u'''select xml from t_bcontractnodexml where contract_node_id = :nId''',
          u'getPartyChildren': u'''select id, code, version_no from t_party where code = :strCode union all
          select id, code, version_no from t_party_log where code = :strVCode''', 
          u'getPartyXml':u'''select party_value from t_partyvalue where party_id=:nid''',
          u'getPartyLogXml':'''select party_value from t_partyvalue_log where party_id=:nid''',
          u'testPartyExist':'''select id from t_party where id = :nid''',
          u'testPartyLogExist':'''select id from t_partylog where id = :nid''',
          u'getClaim': u'', 
          u'getClaimVersion': u'',
          u'CrtBckTbl': u'''create table bck_xml (
          operator varchar(20), operatetime date, 
          tablename varchar2(50), columnname varchar2(50),
          id char(19), key varchar2(50),
          prev_xml blob, post_xml blob, remark varchar2(2000))''',
          u'InsertBckXml': u'''insert into bck_xml (
          operator, operatetime, tablename, columnname,
          id, key, prev_xml, post_xml, remark) 
          values ( :operator,sysdate,:tablename,:columnname,
          :id,:key,:prev_xml,:post_xml,:remark)''' ,
          u'CreateSequence':u''''''
          }

class Personality:
    dbUser=''
    dbPossword=''
    dbAddress=''
    dbPort=0
    dbKey='orcl'
    InitinalSize=[]
    def __init__(self):
        pass
        
    

class cprsstru:
    prefileheadflag = 0
    pkwarever = 0
    globwayflag = 0
    compressway = 0
    finaldate = 0
    finaltime = 0
    precrc32 = 0
    precprsedsize = 0
    preuncprsedsize = 0
    filenamelen = 0
    extendlen = 0
    filename = ''
    extendfield = ''
    filecontent = ''
    fileheadflag = 0
    crc32 = 0
    cprsedsize = 0
    uncprsedsize = 0
    # pharos的头, 很固定, 38位, 文件名NodeClob
    pharoshead = ''
    ucprs_xml = ''
    prev_xml = ''
    post_xml = ''
    
    def __init__(self):
        self.prefileheadflag = 0
        self.pkwarever = 0
        self.globwayflag = 0
        self.compressway = 0
        self.finaldate = 0
        self.finaltime = 0
        self.precrc32 = 0
        # 压缩后大小
        self.precprsedsize = 0
        # 压缩前大小
        self.preuncprsedsize = 0
        self.filenamelen = 0
        self.extendlen = 0
        self.filename = ''
        self.extendfield = ''
        self.filecontent = ''
        self.fileheadflag = 0
        self.crc32 = 0
        self.cprsedsize = 0
        self.uncprsedsize = 0
        self.pharoshead = ''
        self.ucprs_xml = ''
        self.prev_xml = ''
        self.post_xml = ''
        
    def compress(self,s):
        try:
            self.uncprsedsize=len(s.encode('utf-8'))
            self.crc32 = zlib.crc32(s)
            self.filecontent=zlib.compress(s)
            self.cprsedsize=len(self.filecontent)-6

            strNew = struct.pack('38s', self.pharoshead)
            # compress 的压缩与pharos的压缩区别: 字符头多出两位, 尾多出4位(求原因)
            strNew = strNew + struct.pack('%d'%self.cprsedsize+'s', self.filecontent[2:-4:1])

            strNew = strNew + struct.pack('4s', self.fileheadflag)
            strNew = strNew + struct.pack('<3l', self.crc32, self.cprsedsize, self.uncprsedsize)
            file('c:\\in.bin', 'w').write(strNew)
            file('c:\\out.bin', 'w').write(self.prev_xml)
            self.post_xml = strNew

        except Exception, ex:
            print ex
            return None
    
    def Txt2Xml(self, str):
        self.DomObj = minidom.parseString(str, None)
    
    def Xml2Txt(self, str):
        self.ucprs_xml = self.DomObj.toxml()

    def recompress(self, xml):
        self.prev_xml = str(xml)

        self.prefileheadflag = xml.read(1,4)
        self.pkwarever = xml.read(4 + 1,2)
        self.globwayflag = xml.read(6 + 1,2)
        self.compressway = xml.read(8 + 1,2)
        self.finaldate = xml.read(10+ 1,2)
        self.finaltime = xml.read(12+ 1,2)
        self.precrc32 = xml.read(14+ 1,4)
        self.precprsedsize = xml.read(18+ 1,4)
        self.preuncprsedsize = xml.read(22+ 1,4)
        self.filenamelen = struct.unpack('h', xml.read(26+ 1,2))[0]
        self.extendlen = struct.unpack('h', xml.read(28+ 1,2))[0]
        self.filename = xml.read(30+ 1,self.filenamelen)
        # pharos 的头, 38位, 文件名NodeClob, 无扩展区
        self.pharoshead = xml.read(1,38)
        # Pharos 的XML前文件大小,文件压缩后大小没有设置
        self.cprsedsize = struct.unpack('l', xml.read(xml.size()-8+1,4))[0]
        self.uncprsedsize = struct.unpack('l', xml.read(xml.size()-4+1,4))[0]
        if( self.extendlen > 0 ):
            self.extendfield = xml.read(30+self.filenamelen+1,self.extendlen)
        self.filecontent = xml.read(30+self.filenamelen+self.extendlen+1,self.cprsedsize)
        self.fileheadflag = xml.read(xml.size()-16+1,4)
        self.crc32 = struct.unpack('l', xml.read(xml.size()-12+1,4))

        try:
            self.ucprs_xml=zlib.decompress(self.filecontent,-zlib.MAX_WBITS)
        except Exception, ex:
            print ex
            return None
        

class PharosBizRetrieve(cprsstru):
    def __init__(self):
        self.connDatabase()
        cprsstru.__init__(self)
    
    def connDatabase(self):
        try:
            self.conn=cx_Oracle.connect(strOracleConnect)
            if(self.conn is None):
                print 'connection is unreachable'
                return None
            self.cursor = self.conn.cursor()
            if(self.cursor is None):
                print 'Get cursor failure'
        except Exception, ex:
            print ex
            return None
    
    def BackNodeXml(self, id, key, pxml, lxml):
        self.BackXml('t_bcontractnodexml', 'xml', id, key, pxml, lxml)
        
    def BackPartyXml(self, id, key, pxml, lxml):
        self.cursor.execute(SqlDic['testPartyExist'], {'nid':id})
        items = self.cursor.fetchall()

        if( len(items) != 0):
            self.BackXml('t_partyvalue', 'party_value', id, key, pxml, lxml)
        else:
            self.BackXml('t_partyvalue_log', 'party_value', id, key, pxml, lxml)
        
    
    def BackXml(self, tname, cname, id, key, pxml, lxml, rmk='', oper='SYSTEM'):
        if self.conn == None:
            self.connDatabase()     
        
        try:
            self.cursor.setinputsizes(prev_xml=cx_Oracle.BLOB, post_xml=cx_Oracle.BLOB)
            dic={u'operator':oper, u'tablename':tname, u'columnname':cname, 
                 u'id':id, u'key':key, u'prev_xml':pxml, u'post_xml':lxml, u'remark':rmk}
            self.cursor.execute(SqlDic[u'InsertBckXml'], dic)
            self.cursor.execute('commit')

        except Exception, ex:
            print ex
            return None
        
    def setBizXml(self, id, bizNo, prev_xml, post_xml, bizCategory, bizType):
        if(bizCategory == u'保单' or bizCategory == u'批单'):
            self.BackNodeXml(id, bizNo, prev_xml, post_xml)
            self.setNodeXml(id, post_xml)
        elif(bizCategory == u'当事方'):
            self.BackPartyXml(id, bizNo, prev_xml, post_xml)
            self.setPartyXml(id, post_xml)
        elif(bizCategory == u'赔案'):
            print 'claim'
        else:
            print 'other'
        
        
    def setNodeXml(self, id, xml):
        if self.conn == None:
            self.connDatabase()
        try:
            self.cursor.setinputsizes(xml=cx_Oracle.BLOB)

            self.cursor.execute(SqlDic[u'updNodeXml'], {'xml':xml, 'id':id})
            self.cursor.execute('commit')
        except Exception, ex:
            print ex
            return None
    
    def setPartyXml(self,id, xml):
        if self.conn == None:
            self.connDatabase()
        try:
            self.cursor.execute(SqlDic['testPartyExist'], {'nid':id})
            items = self.cursor.fetchall()
            
            self.cursor.setinputsizes(xml=cx_Oracle.BLOB)

            if( len(items) != 0):
                self.cursor.execute(SqlDic[u'updPartyXml'], {'xml':xml, 'id':id})
            else:
                self.cursor.execute(SqlDic[u'updPartyLogXml'], {'xml':xml, 'id':id})

            self.cursor.execute('commit')    

        except Exception, ex:
            print ex
            return None
            
    def getChildren(self, bizNo, bizCategory, bizType ):
        if self.conn == None:
            self.connDatabase()
            
        if(bizCategory == u'保单'):
            tmpList=self.getPolicyChildren(bizNo)
            return tmpList
        elif(bizCategory == u'批单'):
            tmpList=self.getEndorseChildren(bizNo)
            return tmpList
        elif(bizCategory == u'当事方'):
            return self.getPartyChildren(bizNo)
        elif(bizCategory == u'赔案' and bizType != '当前'):
            return self.getClaimVersion(bizNo)
        elif(bizCategory == u'赔案' and bizType == '当前'):
            print 'claim present'
        else:
            print 'other'
    
    def getXml(self, nId, bizCategory, bizType=''):

        self.connDatabase()
        
        if(bizCategory == u'保单'):
            return self.getNodeXml(nId)
        elif(bizCategory == u'批单'):
            return self.getNodeXml(nId)
        elif(bizCategory == u'当事方'):
            return self.getPartyXml(nId)
        elif(bizCategory == u'赔案'):
            print 'claim'
        else:
            print 'other'
    
    # 取节点树, 递归, 返回列表树
    def getNodeTree(self, contract_id, regexp = '^[0-9]+$'):
        try:
            self.cursor.execute(SqlDic['getNodeTree'], (contract_id, regexp))
        except Exception, ex:
            print ex
        items = self.cursor.fetchall()
        
        if( len(items) == 0) :
            return None
        
        resultList = []
        tmptuple=()
        for item in items:
            tmptuple = (item[0], item[3],  item[5] +' ' + unicode(item[4],'gbk'))
            resultList.append(tmptuple)
            regexp = '^'+item[3]+'-[0-9]+$'
            result = self.getNodeTree(contract_id, regexp)
            if( result == None ):
                continue
            else:
                resultList.append(result)
 
        return resultList
    
    def getEndorContractId(self, bizNo):
        if self.conn is None:
            self.connDatabase()
        
        try:
            self.cursor.execute(SqlDic['getEndorseContractId'], strEdrNo=bizNo)
            contract_id, = self.cursor.fetchone()
            return contract_id
        except Exception, ex:
            print ex
            return None
    
    def getPolicyContractId(self, bizNo):
        if self.conn is None:
            self.connDatabase()
        
        try:
            self.cursor.execute(SqlDic['getPolicyContractId'], strPlyNo=bizNo)
            contract_id, = self.cursor.fetchone()
            return contract_id
        except Exception, ex:
            print ex
            return None
    
    def getClaimVersion(self,bizNo):
        if self.conn is None:
            self.connDatabase()

        resultItem=[]
        self.cursor.execute(u'''select id, version_no, biz_point from t_claim_version where claim_id = 2009051911394580475''')
        claimItems = self.cursor.fetchall()
        for claimItem in claimItems:
            tmpItem=[]
            
            self.cursor.execute(u'''select id, version_no, 'part'
                from t_partofclaim_version where claim_id = :id and version_no=:versionno''', 
                                {'id':2009051911394580475L, 'versionno':claimItem[1]})
            claimPartItems = self.cursor.fetchall()
            if(len(claimPartItems)==0):
                continue
            for claimPartItem in claimPartItems:
                tmpItem2=[claimPartItem,[(claimPartItem[0],claimPartItem[1],u'PART_XML'),
                                         (claimPartItem[0],claimPartItem[1],u'PROCEDURE_XML'),
                                         (claimPartItem[0],claimPartItem[1],u'MESSAGE')]]
                tmpItem.append(tmpItem2)
                
            self.cursor.execute(u'''select id,version_no,'prp'
                from t_prp_version where claim_id = :id and claim_version_no=:versionno''', 
                                {'id':2009051911394580475L, 'versionno':claimItem[1]})
            claimPrpItems = self.cursor.fetchall()
            if(len(claimPrpItems)==0):
                continue
            for claimPrpItem in claimPrpItems:
                tmpItem2=[claimPrpItem,[(claimPrpItem[0],claimPrpItem[1],u'PRP_XML'),
                                        (claimPrpItem[0],claimPrpItem[1],u'CP_XML'),
                                        (claimPrpItem[0],claimPrpItem[1],u'TS_XML'),
                                        (claimPrpItem[0],claimPrpItem[1],u'MESSAGE')]]
                tmpItem.append(tmpItem2)
            
            resultItem.append([claimItem, tmpItem])
            print resultItem
        return resultItem
    
    

    def getPartyChildren(self, bizNo):
        if self.conn is None:
            self.connDatabase()
        
        result=[]
        tmptuple=()
        try:
            self.cursor.execute(SqlDic[u'getPartyChildren'], {'strCode':bizNo,'strVCode':bizNo} )
            items = self.cursor.fetchall()
            
            for item in items:
                tmptuple=(item[0], item[2], '')
                result.append(tmptuple)

        except Exception, ex:
            print ex
            return None
        
        return result
            
    def getPolicyChildren(self, bizNo):
        if self.conn is None:
            self.connDatabase()
        
        try:
            contract_id = self.getPolicyContractId(bizNo)
            return [self.getNodeTree(contract_id)]

        except Exception, ex:
            print ex
            return None
    
    def getEndorseChildren(self, bizNo):
        if self.conn is None:
            self.connDatabase()
        
        try:
            contract_id = self.getEndorseContractId(bizNo)
            return [self.getNodeTree(contract_id)]

        except Exception, ex:
            print ex
            return None
    
    def getClaimVersionXml(self, nId, version):
        pass
    
    def getClaimPartVersionXml(self, nId, version):
        pass
    
    def getClaimPrpVersionXml(self, nId, version):
        pass
        
    def getNodeXml(self, nId):
        if self.conn is None:
            self.connDatabase()
        try:
            self.cursor.execute(SqlDic['NodeXml'], nId=nId)
            xmlBuff, = self.cursor.fetchone()
            return self.recompress(xmlBuff)
        except Exception, ex:
                print ex
                return None
    
    def getPartyXml(self, nid):
        try:

            self.cursor.execute(SqlDic[u'getPartyXml'], {'nid':nid})
            items = self.cursor.fetchone()
            
            if( items == None):
                self.cursor.execute(SqlDic[u'getPartyLogXml'], {'nid':nid})
                items = self.cursor.fetchone()
                if(len(items) == 0):
                    return None
                
            return self.recompress(items[0])
        except Exception, ex:
            print ex
            return None
        
    def getClaimXml(self, bizNo, bizCategory):
        print bizNo, bizCategory
  

class BizCtrlFrame(wx.Frame, PharosBizRetrieve):
    DefWindowSize = [1024,768]
    DefPanelSize = [1008, 686]
    CurrWindowSize = [0,0]
    CurrPanelSize = [0,0]
    StartSpot = [10,10]
    LeftDefultWidth=490
    DefaultInterval=10

    def __init__(self):
        self.CurrWindowSize = self.DefWindowSize
        self.LeftDefultWidth=450
        self.DefaultInterval = 10
        self.StartSpot=[10,10]
        
        wx.Frame.__init__(self,None,-1,u'Pharos XML 查看修改器',size=tuple(self.CurrWindowSize) )
        PharosBizRetrieve.__init__(self)
        #窗口的最小值1024*768
        self.SetMinSize(tuple(self.DefWindowSize))
        
        self.panel = wx.Panel(self, -1, size=self.DefPanelSize)
        self.CurrPanelSize = list(self.panel.GetSize())
        #画板的最小值是1008*686
        self.panel.SetMinSize(tuple(self.CurrPanelSize))
        
        
        # 创建一个菜单栏
        self.menuBar = wx.MenuBar()
        self.menu = wx.Menu()
        # 添加菜单到菜单栏
        self.menuBar.Append(self.menu, u'菜单')
        self.SetMenuBar(self.menuBar)
        # 创建状态栏
        self.statusBar = self.CreateStatusBar() 
#         self.toolbar = self.CreateToolBar() #2 创建工具栏
#         self.toolbar.AddSimpleTool(wx.NewId(), images.getNewBitmap(), 'New', '''Long help for New''')
        #业务号文本框
        self.bizLabelPos = self.StartSpot
        self.bizLabel = wx.StaticText(self.panel, -1, u'业务ID', pos=tuple(self.bizLabelPos))

        self.bizNoPos = [self.bizLabelPos[0]+self.bizLabel.GetSize()[0]+self.DefaultInterval, self.bizLabelPos[1]]
        self.bizNoSize = [200,25]
        self.bizNo = wx.TextCtrl(self.panel, -1, value = u'17259008170659053', 
                                 pos=tuple(self.bizNoPos), 
                                 size=tuple(self.bizNoSize),
                                 name=u'')
        self.bizNo.SetInsertionPoint(0)
        #查询树的按钮
        self.SearchButton   = wx.Button(self.panel, -1, u'查询', 
                                        pos=(self.bizNoPos[0] + self.bizNoSize[0]+10,self.bizNoPos[1]))
        self.Bind(wx.EVT_BUTTON, self.OnSearchClick, self.SearchButton)
        self.SearchButton.SetDefault()
        
        #业务类型复选框
        bizRBType = [u'保单', u'批单', u'当事方', u'赔案']
        self.bizRBTypePos = [self.bizLabelPos[0], self.bizNoPos[1] + self.bizNoSize[1]+ 10]
        self.bizRBTypeSize = [self.LeftDefultWidth, 50]
        self.bizRBType = wx.RadioBox(self.panel, -1, 
                                     u'业务类型', 
                                     pos=tuple(self.bizRBTypePos), 
                                     size=tuple(self.bizRBTypeSize), 
                                     choices = bizRBType, 
                                     majorDimension = len(bizRBType)/8, 
                                     style=wx.RA_SPECIFY_COLS)
        self.bizRBType.SetStringSelection(u'保单')
        #赔案版本复选框
        ClaimRBType = [u'全部', u'报案', u'查勘', u'立案', u'理算', u'当前']
        self.ClaimRBTypePos = [self.bizRBTypePos[0], self.bizRBTypePos[1] + self.bizRBTypeSize[1]+ 10]
        self.ClaimRBTypeSize = [self.LeftDefultWidth, 50]
        self.ClaimRBType = wx.RadioBox(self.panel, -1, 
                                       u'赔案版本', 
                                       pos=tuple(self.ClaimRBTypePos), 
                                       size=tuple(self.ClaimRBTypeSize), 
                                       choices = ClaimRBType, 
                                       majorDimension = len(ClaimRBType)/8, 
                                       style=wx.RA_SPECIFY_COLS)

        self.bizTreePos = [10, self.bizNoSize[1] + 10 + self.bizRBTypeSize[1] + 10 + self.ClaimRBTypeSize[1] + 20]
        #计算树框的高度 = 当前画板高度-树框顶位置-留一行按钮高度-状态栏高度
        self.bizTreeSize = [self.LeftDefultWidth, 
                            self.panel.GetSize()[1]-self.bizTreePos[1]-30-30]
        self.bizTree = wx.gizmos.TreeListCtrl(self.panel, -1, 
                                   pos=tuple(self.bizTreePos),
                                   size=tuple(self.bizTreeSize), 
                                   style=wx.TR_DEFAULT_STYLE|wx.TR_FULL_ROW_HIGHLIGHT)
        self.bizTree.AddColumn(u'ID')
        self.bizTree.SetColumnWidth(0,240)
        self.bizTree.AddColumn(u'类型')
        self.bizTree.SetColumnWidth(1,50)
        self.bizTree.AddColumn(u'描述')
        self.bizTree.SetColumnWidth(2,200)
        
        self.bizTree.SetMainColumn(0)
#         self.bizTree.GetMainWindow().Bind(wx.EVT_LEFT_DCLICK, self.OnActivated)

        #详情按钮, 点击树枝也可以显示详情
        self.DetailButtonPos = [self.bizTreePos[0], self.bizTreePos[1]+self.bizTreeSize[1]+10]
        self.DetailButton   = wx.Button(self.panel, -1, u'详情', pos=tuple(self.DetailButtonPos))
        self.Bind(wx.EVT_BUTTON, self.OnDetailClick, self.DetailButton)
        
        
        # 源XML 
        self.XmlViewStaticPos = [self.bizTreeSize[0]+20,10]
        self.XmlViewStatic = wx.StaticText(self.panel, -1, pos=tuple(self.XmlViewStaticPos), label=u'XML')
        
        # 树形查看复选框
        self.IsViewXmlTree = wx.CheckBox(self.panel, -1, u'树形', 
                                      pos=(self.XmlViewStaticPos[0]+self.XmlViewStatic.GetSize()[0] + 10,
                                           self.XmlViewStaticPos[1])
                                      )
        self.IsViewXmlTree.SetValue(True)
        self.Bind(wx.EVT_CHECKBOX, self.OnIsViewXmlTreeChanged, self.IsViewXmlTree)
        
        # XmlViewTextPos不变, 是计算的基础
        self.XmlViewTextPos = [self.XmlViewStaticPos[0], 30]
        self.XmlViewTextSize = [
                                # 计算宽度, 源XML和当前XML等宽, 中间留20, 当前XML右边留20
                                (self.panel.GetSize()[0]-self.XmlViewTextPos[0]-20-20)/2,
                                # 计算宽度, 画板高度-起始位置-下面留60
                                self.panel.GetSize()[1]-self.XmlViewTextPos[1]-60
                                ]
        self.XmlViewText = wx.TextCtrl(self.panel, -1, 
                                       value = u'', 
                                       pos=tuple(self.XmlViewTextPos), 
                                       # 计算宽度, 源XML和当前XML等宽, 中间留20, 当前XML右边留5
                                       size=tuple(self.XmlViewTextSize), 
                                       name=u'XML详情', 
                                       style = wx.TE_READONLY|wx.TE_LINEWRAP|wx.TE_MULTILINE|wx.TE_PROCESS_TAB)
        self.MySetTextViewDisable()
#         self.XmlViewText.Bind(wx.EVT_CHAR, self.OnKeyHit)

#         # 当前XML
#         self.XmlModiStaticPos = [
#                                  # 计算位置, 当前XMLStatic=源XML的起始位置+宽度+中间缝隙-滚动条宽度
#                                  self.XmlViewTextPos[0]+self.XmlViewTextSize[0]+20,
#                                  10]
#         
#         self.XmlModiStatic = wx.StaticText(self.panel, -1, 
#                                            pos=tuple(self.XmlModiStaticPos), 
#                                            label=u'当前XML' )
#         
#         self.XmlModiTextPos = [self.XmlModiStaticPos[0], 30]
#         # 大小与源相同
#         self.XmlModiTextSize = self.XmlViewTextSize
#         
#         self.XmlModiText = wx.TextCtrl(self.panel, -1, 
#                                        value = u'', 
#                                        pos=tuple(self.XmlModiTextPos), 
#                                        size=tuple(self.XmlModiTextSize), 
#                                        name=u'XML详情', 
#                                        style = wx.TE_LINEWRAP|wx.TE_MULTILINE)
        #最下面一行按钮
        self.ModifyButtonPos = [self.XmlViewTextPos[0],
                                self.XmlViewTextPos[1]+self.XmlViewTextSize[1]+10]
        
        self.ModifyButton = wx.Button(self.panel, -1, u'修改', pos=tuple(self.ModifyButtonPos))
        self.Bind(wx.EVT_BUTTON, self.OnModifyClick, self.ModifyButton)
#         self.ScreenLButtonPos = [self.ModifyButtonPos[0]+self.ModifyButton.GetSize()[0], 
#                                  self.ModifyButtonPos[1]]
#         self.ScreenLButton = wx.Button(self.panel, -1, u'全屏', pos=tuple(self.ScreenLButtonPos))
        
        self.SaveButtonPos = [self.ModifyButtonPos[0]+self.ModifyButton.GetSize()[0],
                              self.ModifyButtonPos[1]]
        self.SaveButton   = wx.Button(self.panel, -1, u'保存', pos=tuple(self.SaveButtonPos))
        self.Bind(wx.EVT_BUTTON, self.OnSaveClick, self.SaveButton)
        self.SaveButton.Show(False)
        
        self.ReloadButtonPos = [self.SaveButtonPos[0]+self.SaveButton.GetSize()[0],
                                self.SaveButtonPos[1]]
        self.ReloadButton = wx.Button(self.panel, -1, u'重载', pos=tuple(self.ReloadButtonPos))
#         self.ScreenRButtonPos = [self.ReloadButtonPos[0]+self.ReloadButton.GetSize()[0],
#                                 self.ReloadButtonPos[1]]
#         self.ScreenRButton = wx.Button(self.panel, -1, u'全屏', pos=tuple(self.ScreenRButtonPos))
        self.SearchButton.SetFocus()

        # Bind 窗口改变事件
        self.Bind(wx.EVT_SIZE, self.OnSizeChange)
        
        #初始化一些变量
        
    def OnKeyHit(self, event):
        return
        keycode = event.GetKeyCode()
        ctrlDown = event.ControlDown()
        shiftDown = event.ShiftDown()
        
        # 查找替换 CTRL+F
        if((keycode==0x46 or keycode==0x66) and ctrlDown ):
            print 'i get it ', keycode
        # 全选 CTRL+A
        elif((keycode==0x41 or keycode==0x61) and ctrlDown):
            print 'i get it ', keycode
        # 保存 CTRL+S
        elif((keycode==0x53 or keycode==0x73) and ctrlDown):
            print 'i get it ', keycode
        # 删除当前行CTRL+D
        elif((keycode==0x52 or keycode==0x72) and ctrlDown):
            print 'i get it ', keycode
        
    def OnIsViewXmlTreeChanged(self, event):
        if self.IsViewXmlTree.GetValue() == True:
            pass; #self.IsViewXmlTree.SetValue(False)


    def OnModifyClick(self, event):
        if( self.filename == '' ):
            bizDlg = wx.MessageDialog(None, u"尚未得到XML, 请先查询", u"Warning", wx.YES_DEFAULT|wx.ICON_QUESTION)
            if(bizDlg.ShowModal() == wx.ID_YES):
                pass
            bizDlg.Destroy()
            return

        self.MySetTextViewEnable()
        self.SaveButton.Show(True)
#         self.SaveButton.SetWindowStyle(self.SaveButton.SetWindowStyle|wx.ALIGN_INVALID)

    def OnLostFocus(self, event):
        wx.MessageBox('%s'%(self.bizNo.GetValue()))
    
    # 保存按钮
    def OnSaveClick(self, event):
        if( self.filename == '' ):
            bizDlg = wx.MessageDialog(None, u"尚未得到XML, 请先查询", u"Warning", wx.YES_DEFAULT|wx.ICON_QUESTION)
            if(bizDlg.ShowModal() == wx.ID_YES):
                pass
            bizDlg.Destroy()
            return
            
        # 先和源比较, 有改变才保存
        if( self.XmlViewText.GetValue() == self.ucprs_xml ):
            bizDlg = wx.MessageDialog(None, u"没有任何改变, 无需保存", u"Warning", wx.YES_DEFAULT|wx.ICON_QUESTION)
            if(bizDlg.ShowModal() == wx.ID_OK):
                #return
                bizDlg.Destroy()
        
        # 检查修改后的XML的合法性
        try:
            tmpDom = minidom.parseString(self.XmlViewText.GetValue())
            xh = XMLHandler() 
            xml.sax.parseString(self.XmlViewText.GetValue(), xh) 
            
        except Exception, ex:
            tmpDom = None
            bizDlg = wx.MessageDialog(None, u"验证XML合法性失败, 请检查", u"Error", wx.YES_DEFAULT|wx.ICON_QUESTION)
            #保存XML,更新数据库
            if(bizDlg.ShowModal() == wx.ID_OK): 
                bizDlg.Destroy()
                return

        bizDlg = wx.MessageDialog(None, u"确定保存吗,此操作不可撤销", u"Confirm", wx.YES_NO|wx.ICON_QUESTION)
        #保存XML,更新数据库
        if(bizDlg.ShowModal() == wx.ID_YES):
            print u'更新数据库'
            
            self.compress(self.XmlViewText.GetValue())
            
            self.setBizXml(self.bizId, self.bizNo.GetValue(), self.prev_xml, self.post_xml, self.CateSelected, self.TypeSelected)
            
        else:
            return
        bizDlg.Destroy()
        
        self.MySetTextViewDisable()
        self.SaveButton.Show(False)
    
    #当窗口大小改变时
    def OnSizeChange(self, event):
        # 重新计算画板大小
        self.CurrWindowSize = list(self.GetSize())
        self.CurrPanelSize = [self.CurrWindowSize[0]-16, self.CurrWindowSize[1]-82]
        self.panel.SetSize(tuple(self.CurrPanelSize))
        
        # 一切都要做相应的调整, 不会sizer确实悲催
        self.bizTreeSize = [self.LeftDefultWidth, 
                            self.CurrPanelSize[1]-self.bizTreePos[1]-30-30]
        self.bizTree.SetSize(tuple(self.bizTreeSize))
        self.DetailButtonPos = [self.bizTreePos[0], self.bizTreePos[1]+self.bizTreeSize[1]+10]
        self.DetailButton.SetPosition(tuple(self.DetailButtonPos))
        
        # ViewXml, ModiXml不光有调整高度的问题, 还有调整宽度的问题
        self.XmlViewTextSize = [
                                # 计算宽度, XML右边留20
                                self.CurrPanelSize[0]-self.XmlViewTextPos[0]-20,
                                # 计算宽度, 画板高度-起始位置-下面留60
                                self.CurrPanelSize[1]-self.XmlViewTextPos[1]-60
                                ]
        self.XmlViewText.SetSize(tuple(self.XmlViewTextSize))
        # ModiStatic 位置调整
#         self.XmlModiStaticPos = [
#                                  # 计算位置, 当前XMLStatic=源XML的起始位置+宽度+中间缝隙-滚动条宽度
#                                  self.XmlViewTextPos[0]+self.XmlViewTextSize[0]+20,
#                                  10]
#         self.XmlModiStatic.SetPosition(tuple(self.XmlModiStaticPos))
#         self.XmlModiTextPos = [self.XmlModiStaticPos[0], 30]
#         self.XmlModiTextSize = self.XmlViewTextSize
#         self.XmlModiText.SetPosition(tuple(self.XmlModiTextPos))
#         self.XmlModiText.SetSize(tuple(self.XmlModiTextSize))
#         
        self.ModifyButtonPos = [self.XmlViewTextPos[0],
                                self.XmlViewTextPos[1]+self.XmlViewTextSize[1]+10]
        self.ModifyButton.SetPosition(tuple(self.ModifyButtonPos))
        
#         self.ScreenLButtonPos = [self.ModifyButtonPos[0]+self.ModifyButton.GetSize()[0], 
#                                  self.ModifyButtonPos[1]]
#         self.ScreenLButton.SetPosition(tuple(self.ScreenLButtonPos))
        
        self.SaveButtonPos = [self.ModifyButtonPos[0]+self.ModifyButton.GetSize()[0],
                              self.ModifyButtonPos[1]]
        self.SaveButton.SetPosition(tuple(self.SaveButtonPos))
        self.ReloadButtonPos = [self.SaveButtonPos[0]+self.SaveButton.GetSize()[0],
                                self.SaveButtonPos[1]]
        self.ReloadButton.SetPosition(tuple(self.ReloadButtonPos))
#         self.ScreenRButtonPos = [self.ReloadButtonPos[0]+self.ReloadButton.GetSize()[0],
#                                 self.ReloadButtonPos[1]]
#         self.ScreenRButton.SetPosition(tuple(self.ScreenRButtonPos))
    
    def MySetTextViewDisable(self):
        self.XmlViewText.SetBackgroundColour(u'black')
        self.XmlViewText.SetForegroundColour(u'green')
        self.XmlViewText.SetEditable(False)
        
    def MySetTextViewEnable(self):
        self.XmlViewText.SetBackgroundColour(u'white')
        self.XmlViewText.SetForegroundColour(u'black')
        self.XmlViewText.SetEditable(True)
            
    # 当点击查询时, 直接显示数据
    def OnSearchClick(self, event):
        if self.bizNo.Value == '':
            bizDlg = wx.MessageDialog(None, u"业务ID不能为空", u"ERROR", wx.YES_DEFAULT|wx.ICON_QUESTION)
            if(bizDlg.ShowModal() == wx.ID_YES):
                pass
            bizDlg.Destroy()
        else:
            self.bizInfo = PharosBizRetrieve()
            #选中的业务类
            self.CateSelected = self.bizRBType.GetStringSelection()
            #选中的子类
            self.TypeSelected = self.ClaimRBType.GetStringSelection()

            try:
                self.bizTree.DeleteAllItems()
                self.bizRoot = self.bizTree.AddRoot(self.bizNo.Value)
            except Exception, ex:
                print ex
            
            self.getXml(self.bizNo.Value, self.CateSelected, self.TypeSelected)
            self.XmlViewText.SetValue(self.ucprs_xml);
    
    def AddTreeNodes(self, parentItem, items):
        if( items == None):
            return
        print items
        for item in items:
            if(type(item) == tuple):
                newItem = self.bizTree.AppendItem(parentItem, str(item[0]))
                self.bizTree.SetItemText(newItem, str(item[1]), 1)
                self.bizTree.SetItemText(newItem, str(item[2]), 2)      
            elif(type(item)==list):
                newItem = self.bizTree.AppendItem(parentItem, str(item[0][0]))
                self.bizTree.SetItemText(newItem, str(item[0][1]), 1)
                self.bizTree.SetItemText(newItem, str(item[0][2]), 2)
                if( len(item) > 1):
                    self.AddTreeNodes(newItem, item[1])
            else:
                print '树拼装出错,请检查程序'
    
    def GetItemText(self, item):
        if item:
            return self.bizTree.GetItemText(item)
        else:
            return ''
    
    def OnItemExpanded(self, event):
        print 'OnItemExpanded: ', self.GetItemText(event.GetItem())
        
    def OnItemCollapsed(self, event):
        print 'OnItemCollapsed:', self.GetItemText(event.GetItem())
        
    def OnSelChanged(self, event):
        print 'OnSelChanged: ', self.GetItemText(event.GetItem())
    
    #当节点树的节点变化时   
    def OnActivated(self, event):
#         pos = event.GetPosition()
#         item, flags, col = self.bizTree.HitTest(pos)
#         SelectedId = self.bizTree.GetSelection()
#         print 'OnActivated: ',item, flags, col
#         print SelectedId
        # 忽略根节点
        if (self.bizTree.GetRootItem() == event.GetItem()):
            return
        
#         pos = event.GetPosition()
#         item, flags, col = self.bizTree.HitTest(pos)
#         if item:
#             self.log.write('Flags: %s, Col:%s, Text: %s' %
#                            (flags, col, self.bizTree.GetItemText(item, col)))

        # 根据节点ID, 业务类型, 子类取XML
#         print event.GetItem()
        self.bizId = self.GetItemText(event.GetItem())

        self.getXml(self.GetItemText(event.GetItem()), self.CateSelected, self.TypeSelected)
        # 然后显示在XmlViewText
        self.XmlViewText.SetValue(self.ucprs_xml)
        # 保存选中的节点
        self.CurrNode = event.GetItem()
        self.MySetTextViewDisable()
    
    def OnDetailClick(self, event):
        pass
    
    def OnClaimRadioSelected(self, event):
#         self.claimClaimRadio    = wx.RadioButton(wx.Panel(self,-1), -1, u'CLAIM', pos=(200,200))
        pass
        
def main():
    
    app = wx.PySimpleApp()
    
    bizFrame = BizCtrlFrame()
    bizFrame.Show()
    
    app.MainLoop()

if(__name__ == '__main__' ):
    main()
    