#Battlement

This application is meant to import data from AuditEvent Repositories sent through
a queue and storage them in a MongoDb or Redis, so we can plug Spring XD to manage the data generated.
An audit event contains a data map with properties such as user-agent, requested url, instance name
among other security properties

#SomeCoolName

Capture and send metrics of spring boot apps through a queue including application name, instance name,
maybe the url here it's hosted like Vectors.
The default configuration binds the queue to exchange. We could need a expiration time of a message so not related
messages should be not delivered.
The client application should receive messages and check if they are of the required instance, if it is, return to
a websocket endpoint


#Meister

the search engine
query example: `lang:java tags:[foo, bar, ab c]`

