import string
import heapq
import copy


incoming = {}
outgoing = {}
pagerank = {}
d = 0.8

def preprocess():

	#preprocessing to prepare for the algorithm

	doc = open("graph.txt")
	docLines = doc.readlines()

	for counter in range(0,len(docLines)):

		docLines[counter] = [(docLines[counter]).split()]
		docLines[counter] = docLines[counter][0]
		for word in range(0, len(docLines[counter])):

			docLines[counter][word] = docLines[counter][word].rstrip('\n')

	for email in range(0, len(docLines)):

		incoming[docLines[email][2]] = []
		incoming[docLines[email][1]] = []
		outgoing[docLines[email][1]] = []
		outgoing[docLines[email][2]] = []
		pagerank[docLines[email][2]] = []
		pagerank[docLines[email][1]] = []

	for email in range(0, len(docLines)):

		if docLines[email][1] != docLines[email][2]:

			incoming[docLines[email][2]].append(docLines[email][1])
			outgoing[docLines[email][1]].append(docLines[email][2])

def PageRank():

	Output = open("pr.txt", "w")
	print "Preprocessing"
	preprocess()

	"Preprocessing complete"

	#calculate initial pagerank for all the links (as 1/N)

	for node in pagerank:

		pagerank[node] = (1.0/float(len(pagerank)))

	#iterate 10 times through the algorithm

	for iteration in range(1, 11):

		sinkScore = 0.0

		#work out the sum of all the pageranks of the sinks

		for node in pagerank:
			if not outgoing[node]:
				sinkScore += pagerank[node]

		for node in pagerank:
			newRank = 0.0

		#calculate the pagerank

			try:
				for email in incoming[node]:
					newRank = newRank + float(pagerank[email])/float(len(outgoing[email]))

				newRank = ((1.0-d + d*float(sinkScore))/len(pagerank)) + d*newRank

			except KeyError:
				newRank = ((1.0-d + d*float(sinkScore))/len(pagerank)) + d*newRank

			pagerank[node] = newRank

		#to normalise, sum all current pageranks then divide each by the sum

		sum = 0
		for node in pagerank:
			sum += pagerank[node]

		for node in pagerank:
			pagerank[node] = pagerank[node]/sum

	#take top ten and output them to txt file

	topten = (heapq.nlargest(10, pagerank, key=pagerank.get))

	for counter in range(0, 10):
		Output.write(str(pagerank[topten[counter]]) + " " + str(topten[counter]) + "\n")



