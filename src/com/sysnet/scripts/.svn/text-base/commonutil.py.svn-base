import string
import sys
import re

def ltrim(strSource, strTrim=' \t'):
    iPos = 0
    for str in strSource:
        #if(str == strTrim):
        if( re.match('['+strTrim+']', str) ):
            iPos += 1
        else:
            break
    
    return strSource[iPos:len(strSource):1]

def rtrim(strSource, strTrim=' \t'):
    if ( strSource == '' ):
        return strSource
    
    for iPos in range(len(strSource), -1, -1) :
        #print '['+strTrim+']', '['+strSource[iPos-1:iPos:1]+']', iPos
        if( re.match('['+strTrim+']', strSource[iPos-1:iPos:1]) == None ):
            break
            
    return strSource[0:iPos:1]

def trim(strSource, strTrim=' \t'):
    return rtrim(ltrim(strSource, strTrim), strTrim)



def tsplit(string, delimiters):
    """Behaves str.split but supports multiple delimiters."""
    delimiters = tuple(delimiters)
    stack = [string,]
    for delimiter in delimiters:
        for i, substring in enumerate(stack):
            substack = substring.split(delimiter)
            stack.pop(i)
            for j, _substring in enumerate(substack):
                stack.insert(i+j, _substring)
    return stack