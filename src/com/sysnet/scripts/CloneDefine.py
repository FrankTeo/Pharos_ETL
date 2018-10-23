import sys
import os
import string

def clone_define(s_ins_code, t_ins_code, basepath):
    s_path=basepath + u'\\P' + s_ins_code
    t_path=basepath + u'\\P' + t_ins_code

    try:
        os.mkdir(t_path)
    except:
        pass;
        
    li=os.listdir(s_path)
    
    for s_file in li:
        if os.path.isfile(s_path + u'\\' +s_file):
            t_file=s_file.replace(s_ins_code, t_ins_code)
            f_s = file(s_path+u'\\'+s_file, 'r')
            f_t = file(t_path+u'\\'+t_file, 'w')
            f_t.write(f_s.read())
            f_s.close(); f_t.close()
#cpins end

def RenameFile(strDirect,strSource,strTarget,strType,rootdir):
    strPath=rootdir + u'\\P'+ strDirect
    try:

        liContent=os.listdir(strPath)
        
        for strFileName in liContent:
            
            if( os.path.isdir(strPath+u'\\'+strFileName)):
                continue
            
            liFileName = string.split(strFileName, '_', 8)
            if( strType == '1' ):
                liFileName[3] = strTarget;
                liFileName[4] = strTarget;
            elif(strType == '2'):
                if(len(liFileName)>=7):
                    if(liFileName[7] == strSource+'.xml'):
                        liFileName[7] = strTarget+'.xml';
                else:
                    pass
            else:
                print 'Type ' + strType + ' is not defined!'
            
            strFileNameNew = '_'.join(liFileName)
            os.rename(strPath+u'\\'+strFileName, strPath+u'\\'+strFileNameNew)

    except:
        print 'occur error!'
        pass;
