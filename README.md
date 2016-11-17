
# Peers

__This project is work in progress. Don't expect something to work alreeady__

Peers simplifies communication between clients via REST and WebSockets via a central node.

It makes it easy to build REST-REST, REST-WebSocket and WebSocket-WebSocket communication.

I've created it to simplify desktop-browser to mobile-browser communication.

# Instructions

# WebSocket

Connect to `/websocket`. You should receive a response immediately. It will look like the following.

~~~.json
{"address":"5623tgasgagasgds","secret":"adgasgd4asgag4asgasd"}
~~~

Now, you can already populate the queue with messages, even though no other peer is already present.

~~~.json
{"action":"post","secret":"","message":{"content":"here your message"}}
~~~

If you know the address of the receiver, use the following:

~~~.json
{"content":"here your message","address":"53gragwra"}
~~~

# REST

~~~.txt
GET /rest/message

{"content":"5623tgasgagasgds","asdgasgs"}
~~~

Now you receive the oldest available message with.
If no messages are available you receive a HTTP 204 No Content.

~~~
GET /rest/message/asgdasdgasd424adgasdg

~~~

Post a message with

~~~
POST /rest/reply

Message:  {"content":{},"replyTo":"5623tgasgagasgds"}
~~~