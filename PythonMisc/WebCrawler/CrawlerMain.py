import heapq
import re
import robotparser
import urllib
import urllib2
import time

def Crawler():

	start = time.time()
	
	#Initialize counters to record data

	error_counter = 0
	restricted_link_counter = 0
	no_content_tag = 0
	total_links = 0

	#Set up robot file
	
	#enter website root
	
	root = ""
	rp = robotparser.RobotFileParser()
	rp.set_url(root + "/robots.txt")
	rp.read()
	
	#Regular expressions for finding links within a page
	
	page_Check = re.compile('<!-- CONTENT -->(.*?)<!-- /CONTENT -->')
	link_finder = re.compile('<a.*?href="(.*?)".*?/a>')
	
	#Prepare info for requests
	
	user_agent = 'DMCC'
	values = { }
	headers = { 'User-Agent' : user_agent }

	#Create heap, add seed
	
	frontier = []
	
	#enter initial page to search
	
	heapq.heappush(frontier, "")
	heap_front = ""
	
	#Create list for tracking visited links and for tracking which sites are restricted
	
	used_link_list = []
	used_link_restricted_list = []
	
	#While heap isn't empty
	
	while len(frontier) > 0:
	
		#Printing the current length of the frontier helps 
		#give an idea of how long the program has left to go
		
		print len(frontier)
		
		#Take highest priority link from frontier (while also adding it to used list)
		
		link_suffix = heapq.heappop(frontier)
		url = heap_front + link_suffix
		
		used_link_list.append(link_suffix)
		
		#Request then pull page (and check for 404's), afterwards, sleep for 1 second to satisfy the crawl delay/request rate
		
		data = urllib.urlencode(values)
		req = urllib2.Request(url, data, headers)
		time.sleep(1)
		
		#If no response when requesting page, mark page as a 404
		
		try:
			response = urllib2.urlopen(req)
				
			#Read page, check for <!-- CONTENT --> tags (recording if
			#there aren't any), check for links in CONTENT section
			
			current_page = response.read()
			content_section = re.findall(page_Check, current_page)
			if (len(content_section) == 0):
				no_content_tag = no_content_tag + 1
			links = re.findall(link_finder, str(content_section))
		
			#Loop through the links retrieved from the page, check
			#if robots allows access and if the link isn't in either 
			#the frontier or the used link list. If it passes these
			#checks, add it to the frontier. If it fails the robot 
			#check, increment restricted counter
		
			for link in links:		
				if rp.can_fetch("DMCC", heap_front + link) and (link not in used_link_list) and (link not in frontier):
					heapq.heappush(frontier, link)
					total_links = total_links + 1
				
				elif (not rp.can_fetch("DMCC", heap_front + link)) and (link not in used_link_restricted_list):
					restricted_link_counter = restricted_link_counter + 1
					used_link_restricted_list.append(link)
					total_links = total_links + 1
		
			#Sort the frontier list to ensure the highest priority 
			#links are chosen
			
			frontier.sort(reverse = True)
		
		except urllib2.HTTPError:
			error_counter = error_counter + 1
			total_links = total_links + 1
			pass

	#Sort then return the list of all links visited
	
	used_link_list.sort(reverse = True)
	
	end = time.time()
	
	#Print various statistics
	
	print used_link_list
	
	print "Total pages fetched: " + str(len(used_link_list))
	print "Pages that returned 404's: "+ str(error_counter)
	print "Total restricted links: " + str(restricted_link_counter)
	print "Pages with no <!-- CONTENT --> tags: " + str(no_content_tag)
	print "Total pages found: " + str(total_links)
	
	print "Total time taken (seconds): " + str(end - start)
