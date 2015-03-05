import os  
import string
from etl import tsvtojson
from etl import repackage

#datadir is the original tsv files
datadir = '../data'
#intermediatedir is the json files that generated when use etl--tsvtojson
intermediatedir = '../intermediate'  

def tsv2json():
    print 'Begin:'
    #first delete all files in intermediate dir, so that when call etl--tsvtojson, we don't need to delete file manually.
    print 'Cleaning files'
    for f in os.listdir(intermediatedir):
        filepath = os.path.join(intermediatedir, f )   
        os.remove(filepath)  
    #then change all tsv files to json files use tsvtojson, each json file contains multiple employment data
    
    i = 0
    for f in os.listdir(datadir):
        if not f.endswith('.tsv') or f.startswith('.'):
            print f + ' is not a tsv file'
            continue
        datafile = os.path.join(datadir, f)
        i = i + 1
        intermediatefile = '../intermediate/intermediate' + str(i) + '.json'
        print 'Processing ' + datafile
        tsvtojson.main(['','-t', datafile, '-j', intermediatefile,'-c','../header/header.txt','-o','employmentdata', '-e', '../encoding/encoding.txt'])
        print 'Processing ' + intermediatefile
        repackage.main(['', '-j', intermediatefile, '-o', 'employmentdata'])
        print intermediatefile + ' finished!'
    print 'Finished!'  

tsv2json()  
