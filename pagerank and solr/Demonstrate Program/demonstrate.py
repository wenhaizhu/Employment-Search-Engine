import sys
import urllib
import urllib2
import ast


if __name__ == '__main__':
	while True:
		print '-------------------------'
		print '0. quit'
		print '1. show 1st question answer'
		print '2. show 2nd question answer'
		print '3. show 3rd question answer'
		print '4. show 4th question answer'
		print 'Please input a number from 1 to 4:'
		print '-------------------------'
		str_input = raw_input()
		if str_input.isdigit():
			int_input = int(str_input)
			if int_input < 0 or int_input > 4:
				print >> sys.stderr, '%s input out of range!' % str_input
		else:
			print >> sys.stderr, '%s cant conve to int!' % str_input
                firsturl = "http://localhost:8983/solr/select?wt=python&indent=true&q=*:*&group=true&group.limit=1&group.field=location&fl=title&group.sort=pagerank%20desc"
        secondurl = "http://localhost:8983/solr/select?wt=python&indent=true&q=*:*&fq=interval:[1%20TO%20*]&group=true&group.limit=1&group.field=location&fl=title,interval&group.sort=interval%20asc,pagerank%20desc"
		thirdurl = "http://localhost:8983/solr/select/?q=*%3A*&rows=0&facet=true&facet.pivot=location,category&wt=python&indent=on"
		foururl = "http://localhost:8983/solr/select/?q=*%3A*&rows=0&facet=true&facet.pivot=firstSeenData,jobtype&wt=json&indent=on&facet.limit=20&fq=firstSeenData:[0000-00-00%20TO%202014-12-31]&facet.limit=100"
		if int_input == 1:
			print 'answer:'
			req = urllib2.Request(firsturl)
			res_data = urllib2.urlopen(req)
			res = res_data.read()
			res = ast.literal_eval(res)
			groups = res.get('grouped').get('location').get('groups')
			print 'location : title'
			for group in groups:
				location = group.get('groupValue')
				titles = group.get('doclist').get('docs')
				title = titles[0].get('title')
				print location, ' : ', title
		elif int_input == 2:
			req = urllib2.Request(secondurl)
			res_data = urllib2.urlopen(req)
			res = res_data.read()
			res = ast.literal_eval(res)
			groups = res.get('grouped').get('location').get('groups')
			print 'location : title : days'
			for group in groups:
				location = group.get('groupValue')
				titles = group.get('doclist').get('docs')
				title = titles[0].get('title')
				interval = titles[0].get('interval')
				print location, ' : ', title, ' : ', interval
		elif int_input == 3:
			print 'answer:'
			req = urllib2.Request(thirdurl)
			res_data = urllib2.urlopen(req)
			res = res_data.read()
			res = ast.literal_eval(res)
			loc_cats = res.get('facet_counts').get('facet_pivot').get('location,category')
			i = 0
			for loc_cat in loc_cats:
				loc = loc_cat.get('value')
				cats = loc_cat.get('pivot')
				cat = cats[0].get('value')
				print loc, ' : ', cat
				i += 1
				if i == 10:
					break
		elif int_input == 4:
			print 'answer'
			print 'date : full time : part time'
			req = urllib2.Request(foururl)
			res_data = urllib2.urlopen(req)
			res = res_data.read()
			res = ast.literal_eval(res)
			jobtypes = res.get('facet_counts').get('facet_pivot').get('firstSeenData,jobtype')
			dic = {}
			for jobtype in jobtypes:
				date = jobtype.get('value')
				pivots = jobtype.get('pivot')
				for pivot in pivots:
					if pivot.get('value') == 'Tiempo Completo':
						countt = pivot.get('count')
					elif pivot.get('value') == 'Medio Tiempo':
						countm = pivot.get('count')
				dic[date] = [countt, countm]
			i = 0
			for (key, value) in sorted(dic.items(),reverse=True):
				i += 1
				print key, ' : ', value[0], ' : ', value[1]
				if i == 20:
					break
		elif int_input == 0:
			break
