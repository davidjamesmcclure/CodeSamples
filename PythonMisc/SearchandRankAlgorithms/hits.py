import string
import math
import heapq
import os

incoming = {}
outgoing = {}
authorities = {}
hubs = {}
pagecount = {}
d = 0.8

def generateGraph():

	OutputGraph = open("graph.dot", "w")

	graphInfo = {}
	graphInfo2 = {}

	print "Filtering names"

	#take the top ten authority and top ten hub scores

	toptenauth = (heapq.nlargest(10, authorities, key=authorities.get))
	toptenhub = (heapq.nlargest(10, hubs, key=hubs.get))

	for node in toptenauth:

		graphInfo[node] = []

	for node in toptenhub:

		graphInfo[node] = []

	OutputGraph.write("digraph G {\n")
	OutputGraph.write("overlap = false;\n")

	outputList = []

	#for each of the top ten, check whether there are any large exchanges with other nodes
	#(that is, remove any that don't have a number of emails going in both directions)

	print "Checking remaining names for large exchanges with other nodes"

	for node in graphInfo:
		if outgoing[node]:
			for incnode in outgoing[node]:

				try:
					numbersent = outgoing[node].count(incnode)

				except KeyError:
					numbersent = 0

				try:
					numberreceived = outgoing[incnode].count(node)

				except KeyError:
					numberreceived = 0

				totalnum = numbersent + numberreceived

				if numbersent > 50:
					if numberreceived > 20:
						if totalnum > 120:

							outputList.append("\"" + node + "\"" + " -> " + "\"" + incnode + "\"" + " [label = " + str(numbersent) + "];\n")
							outputList.append("\"" + incnode + "\"" + " -> " + "\"" + node + "\"" + " [label = " + str(numberreceived) + "];\n")

	outputList = list(set(outputList))

	print "Adding remaining nodes to graph"

	for line in outputList:
		OutputGraph.write(line)

	OutputGraph.write("}")
	OutputGraph.close()

	try:
		os.remove("graph.png")

	except OSError:
		"no file present to delete"

	os.system("dot -Tpng -Ktwopi graph.dot > graph.png")

def preprocess():

	#preprocess code for algorithm

	print "Preprocessing"

	doc = open("graph.txt")
	docLines = doc.readlines()

	for counter in range(0,len(docLines)):

		docLines[counter] = [(docLines[counter]).split()]
		docLines[counter] = docLines[counter][0]
		for word in range(0, len(docLines[counter])):

			docLines[counter][word] = docLines[counter][word].rstrip('\n')

	for email in range(0, len(docLines)):

		incoming[docLines[email][2]] = []
		outgoing[docLines[email][1]] = []
		authorities[docLines[email][2]] = 1
		authorities[docLines[email][1]] = 1
		hubs[docLines[email][2]] = 1
		hubs[docLines[email][1]] = 1
		pagecount[docLines[email][2]] = []
		pagecount[docLines[email][1]] = []

	for email in range(0, len(docLines)):

		if docLines[email][1] != docLines[email][2]:

			incoming[docLines[email][2]].append(docLines[email][1])
			outgoing[docLines[email][1]].append(docLines[email][2])

	print "Preprocessing complete"

def hits():

	#implemenation of the hits algorithm 

	Output = open("hubs.txt", "w")
	Output2 = open("auth.txt", "w")

	preprocess()

	#iterate 10 times

	print "Iterating 10 times"

	for iteration in range(1, 11):

		print "Iteration " + str(iteration)
		normalisation = 0

		for email in hubs:

			hubs[email] = 0
			try:
				for sent in outgoing[email]:
					hubs[email] += authorities[sent]

			except KeyError:
				"no incoming links"

			normalisation += (hubs[email]*hubs[email])

		normalisation = math.sqrt(normalisation)

		for email in hubs:
			hubs[email] = (hubs[email]/normalisation)

		normalisation = 0

		for email in authorities:

			authorities[email] = 0
			try:
				for received in incoming[email]:
					authorities[email] += hubs[received]

			except KeyError:
				"no incoming links"

			normalisation += (authorities[email]*authorities[email])

		normalisation = math.sqrt(normalisation)

		for email in authorities:
			authorities[email] = (authorities[email]/normalisation)

	#find top ten authority and hub scores from the dictionaries of each

	print "Finding and outputting top ten hubs and authorities"

	topten = (heapq.nlargest(10, hubs, key=hubs.get))

	for counter in range(0, 10):
		Output.write(str(hubs[topten[counter]]) + " " + str(topten[counter]) + "\n")

	topten = (heapq.nlargest(10, authorities, key=authorities.get))

	for counter in range(0, 10):
		Output2.write(str(authorities[topten[counter]]) + " " + str(topten[counter]) + "\n")
