from simhash import simhash #implement by third party
import json
import glob
import sys

reload(sys)
sys.setdefaultencoding('utf-8')

if __name__ == '__main__':

	exactString_hash = {}
	fileNum = 0   #total files amoutnt count
	fileCount = 0 #non duplicate files count
	failFile = 0  #files that cannot be open successfully
	path ="./json/*.json"
	files=glob.glob(path)

	for fname in files:

		fingerPrint_hash = {}
		try:
			json_data=open(fname,'r')
			data = json.load(json_data,strict=False)
		except ValueError:
			failFile += 1
			continue

		nearString = data['applications'] #job fields that needed to be similar 
		exactString = data['location']+' '+data['department']+' '+data['company']+' '+data['title']+' '+data['jobtype'] #job fields that needed to be match
		json_data.close()
		sh = simhash()
		sh.create_hash(str(nearString))
		sh2 = simhash()
		sh2.create_hash(str(exactString))
		eStrHash = sh2.hash

		'''
		if all job fields needed to be matched have fully matched, then compare similarity in subset.
		if similarity > threshold, then regard as same job. otherwise, add this job in subset.
		'''
		if eStrHash in exactString_hash:
			hasSimilarOne = False
			for fp, fn in exactString_hash[eStrHash].items() :
				if sh.similarity(fp) > 0.8:
					hasSimilarOne = True
					#print str(fname)+" is the same as "+str(fn) #output two same files being found 
					break
			if hasSimilarOne == False:
				exactString_hash[eStrHash][sh] = fname
				fileCount += 1


		else:
			fingerPrint_hash[sh] = fname
			exactString_hash[eStrHash] = fingerPrint_hash
			fileCount += 1

		fileNum += 1
		print str(fileCount)+"/"+str(fileNum)
	
	print "non deplicate files count: "+str(fileCount)+"/"+str(fileNum)
	