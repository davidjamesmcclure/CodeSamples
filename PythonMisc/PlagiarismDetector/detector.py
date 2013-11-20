import string
import os
import hashlib
from nltk.corpus import stopwords

def preprocess(importedFile):

	#take file and preprocess (removing stopwords, punctuation, empty strings)

	doc = open(importedFile)
	docLines = doc.readlines()
	docName = docLines.pop(0)

	for counter in range(0,len(docLines)):

		docLines[counter] = [(docLines[counter]).translate(None, string.punctuation)]
		docLines[counter] = docLines[counter][0]
		docLines[counter] = docLines[counter].rstrip('\n')
		docLines[counter] = [w for w in docLines[counter].split() if not w in stopwords.words('english')]

	docLines = filter(None, docLines)
	return docLines

def hammingCalc(simHashValue1, simHashValue2):

	#calculate the hamming value of two hashes

	hamming = 0

	for number in range(0, len(simHashValue1)):

		if simHashValue1[number] != simHashValue2[number]:

			hamming += 1

	return hamming

def exact():

	#find any exact matches in a set of documents

	docCount = 0.0
	Output = open("exact.txt", "w")
	fileList = os.listdir("./0927596")
	hashDict = {}
	length = len(fileList)
	dupList = []

	previousPercent = 0

	for fileName in fileList:

		docCount = docCount + 1.0
		percentageComplete = int((float(docCount)/float(length))*100.0)

		if percentageComplete != previousPercent:

			print "Processing " + str(percentageComplete) + "% complete"		
			previousPercent = percentageComplete

		fullfileName = "./0927596/" + fileName
		doc = preprocess(fullfileName)
		hashDoc = hashlib.md5()

		#hash each word and add together, adding the final result to a dictionary

		for counter in range(0,len(doc)):
			for counter2 in range(0,len(doc[counter])):
				hashDoc.update(doc[counter][counter2])

		hashDict[fileName] = hashDoc

	#check all the hashes against each other, return exact duplicates

	for fileName in fileList:
		for fileName2 in fileList:
			if fileName != fileName2:
				if hashDict[fileName].digest() == hashDict[fileName2].digest():
					dup = (fileName[:-4] + "-" + fileName2[:-4] + "\n")
					dup2 = (fileName2[:-4] + "-" + fileName[:-4] + "\n")
					if (dup not in dupList) and (dup2 not in dupList):
						dupList.append(fileName[:-4] + "-" + fileName2[:-4] + "\n")
						Output.write(fileName[:-4] + "-" + fileName2[:-4] + "\n")

def near():

	#find similar documents

	Output = open("near.txt", "w")
	fileList = os.listdir("./0927596")
	docCount = 0
	hashDict = {}
	length = len(fileList)
	dupList = []
	
	previousPercent = 0

	for fileName in fileList:
		docCount = docCount + 1.0
		fullfileName = "./0927596/" + fileName
		doc = preprocess(fullfileName)

		percentageComplete = int((float(docCount)/float(length))*100.0)

		if percentageComplete != previousPercent:

			print "Processing " + str(percentageComplete) + "% complete"
			previousPercent = percentageComplete

		V = ([0]*32)

		#hash each word and update V (list of length 32)

		for sentence in range(0,len(doc)):

			for word in range(0,len(doc[sentence])):
				hashWord = doc[sentence][word].__hash__()

				for n in range(0, 32):
					hashMask = 1 << n

					if (hashWord & hashMask):
						V[n] = V[n] + 1

					else:
						V[n] = V[n] - 1

		if V[0] > 0:
			simHash = "1"

		elif V[0] <= 0:
			simHash = "0"

		for value in range(1, len(V)):

			if V[value] > 0:
				simHash = simHash + "1"

			elif V[value] <= 0:
				simHash = simHash + "0"

		hashDict[fileName] = simHash

	#calculate hamming distance of entries and return any with low enough values

	for keys1 in hashDict:
		for keys2 in hashDict:
			if (keys1 != keys2):
				hamming = (hammingCalc(hashDict[keys1], hashDict[keys2]))
				if (hamming < 2):

					dup = str(keys1) + "-" + str(keys2)
					dup2 = str(keys2) + "-" + str(keys1)

					if (dup not in dupList) and (dup2 not in dupList):

						dupList.append(dup)
						Output.write(str(keys1)[:-4] + "-" + str(keys2)[:-4] + "\n")


def listConcat(doc):

	mainDocList = []

	for line in range(0, len(doc)):

		mainDocList += doc[line]

	return mainDocList

def finn():

	#find document plateaus and return similar plateaus

	Output = open("finn.txt", "w")
	fileList = os.listdir("./0927596")
	docCount = 0
	length = len(fileList)
	previousPercent = 0
	hashDict = {}
	dupList = []

	for fileName in fileList:

		docCount = docCount + 1.0
		fullfileName = "./0927596/" + fileName
		doc = preprocess(fullfileName)

		percentageComplete = int((float(docCount)/float(length))*100.0)

		if percentageComplete != previousPercent:

			print "Processing " + str(percentageComplete) + "% complete"

			previousPercent = percentageComplete

		doc = listConcat(doc)
		wordList = []

		for word in range(0, len(doc)):

			try:
				int(doc[word])
				wordList += ['0']
			except ValueError:
				wordList += ['1']

		L = 0.0
		besta = 0.0
		bestb = 0.0
		currentFnt = 0.0


		for a in range(0, len(wordList) - 1):

			R = 0.0

			L += int(wordList[a])

			M = 0.0

			for b in range(a + 1, len(wordList)):

				R += float(wordList[b])
				M += (1.0 - float(wordList[b]))

				newFnt = L + (100.0*M) + R

				if (newFnt > currentFnt):

					besta = a
					bestb = b
					currentFnt = newFnt


		if (bestb - besta) > 4:

			V = ([0]*32)
			doc = doc[besta:bestb]

			for word in range(0,len(doc)):

				hashWord = doc[word].__hash__()
				for n in range(0, 32):
					hashMask = 1 << n

					if (hashWord & hashMask):
						V[n] = V[n] + 1

					else:
						V[n] = V[n] - 1

			if V[0] > 0:
				simHash = "1"

			elif V[0] <= 0:
				simHash = "0"

			for value in range(1, len(V)):

				if V[value] > 0:
					simHash = simHash + "1"

				elif V[value] <= 0:
					simHash = simHash + "0"

			hashDict[fileName] = simHash

	for keys1 in hashDict:
		for keys2 in hashDict:
			if (keys1 != keys2):
				hamming = (hammingCalc(hashDict[keys1], hashDict[keys2]))

				if (hamming < 2):

					dup = str(keys1) + "-" + str(keys2)
					dup2 = str(keys2) + "-" + str(keys1)

					if (dup not in dupList) and (dup2 not in dupList):

						dupList.append(dup)
						Output.write(str(keys1)[:-4] + "-" + str(keys2)[:-4] + "\n")
