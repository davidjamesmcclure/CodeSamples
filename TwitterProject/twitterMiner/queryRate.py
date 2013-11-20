import string, json, pprint
import oauth2 as oauth

#insert keys here

CONSUMER_KEY = ""
CONSUMER_SECRET = ""
ACCESS_KEY = ""
ACCESS_SECRET = ""

query = "Obama"
returnCount = 1
pagesReturned = 1

countStatement = "&count=" + str(returnCount)

searchURL = "https://api.twitter.com/1.1/search/tweets.json?q=" + query + countStatement

consumer = oauth.Consumer(key=CONSUMER_KEY, secret=CONSUMER_SECRET)
access_token = oauth.Token(key=ACCESS_KEY, secret=ACCESS_SECRET)
client = oauth.Client(consumer, access_token)

response, data = client.request(searchURL)

tweets = json.loads(data)

print "\n"
print "Total responses allowed every 15 minutes: " + str(response["x-rate-limit-limit"]) + "\n"
print "Responses left in this 15 minute block: " + str(response["x-rate-limit-remaining"]) + "\n"
print "Time until reset: " + str(response["x-rate-limit-reset"])

if int(response["x-rate-limit-remaining"]) < pagesReturned*2:

	print "WARNING: APPROACHING RATE LIMIT. THIS IS RESET EVERY 15 MINUTES, PLEASE TAKE A BREAK."
