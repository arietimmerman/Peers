
# Peers

> For a authentication solution, I wanted my desktop web browser to communicate with my smartphone's web browser. To facilitate this use case, as well as many others, I created Peers.

Peers simplifies communication between clients via REST and WebSockets via a central node.

It makes it easy to build REST-REST, REST-WebSocket and WebSocket-WebSocket communication.

# Design principle

Communications between nodes depends on the inboxes and outboxes of both.

Every node can read messages from everyone's outbox. Also, every node can post messages to everyone's inbox.

For reading message's from someone's inbox or for posting messages to someon'e outbox, you need to know the secret.

# Instructions

# WebSocket

Connect to `/websocket`. You should receive a response immediately. It will look like the following.

~~~.json
{"address":"0USTX0fpOlZ3K6eA2kqsMVoVxOeLFxjPlM01o6dFcjI","secret":"TJNNDjvwuClrLdQrrh1usP0fZxk4fbiBGsQkWr_s9lo"}
~~~

Now, you can already populate the queue with messages, even though no other peer is already present.

~~~.json
{"action":"postToOutbox", "address":"0USTX0fpOlZ3K6eA2kqsMVoVxOeLFxjPlM01o6dFcjI", "secret":"TJNNDjvwuClrLdQrrh1usP0fZxk4fbiBGsQkWr_s9lo", "message":{"content":"Hello World"}}
~~~

If you happen to know the address of the receiver, use the following.

~~~.json
{"action":"postToInbox","address":"0USTX0fpOlZ3K6eA2kqsMVoVxOeLFxjPlM01o6dFcjI","message":{"content":"hallo daar dan"}}
~~~

Although in most cases you should not want to poll from messages via WebSockets, it is possible by doing the following.

~~~.json
{"action":"readOutbox","address":"0USTX0fpOlZ3K6eA2kqsMVoVxOeLFxjPlM01o6dFcjI"}
~~~

And the following.

~~~.json
{"action":"readInbox","address":"0USTX0fpOlZ3K6eA2kqsMVoVxOeLFxjPlM01o6dFcjI", "secret":"TJNNDjvwuClrLdQrrh1usP0fZxk4fbiBGsQkWr_s9lo"}
~~~

# REST

With REST, the creation of an in- and outbox is optional. If you want other nodes to send messages to you, please use the following.

~~~.txt
GET /rest/message/start
~~~

For reading messages from someone's outbox, use the following.

~~~.txt
GET /rest/message/readInbox/eRMftk64_Fqh4LCjyHo7dr0Gyyf1qX3NYQl47pfDkoI
~~~

For reading messages from someone's inbox, use the following.

~~~.txt
/rest/message/readInbox/eRMftk64_Fqh4LCjyHo7dr0Gyyf1qX3NYQl47pfDkoI/4BSIyAo8xdvC7jaws0TPBXVL_f-hrJ4m-roTZ-MeP_E
~~~

For posting messages to someone's outbox, use the following

~~~.txt
POST /rest/message/postToOutbox/eRMftk64_Fqh4LCjyHo7dr0Gyyf1qX3NYQl47pfDkoI/4BSIyAo8xdvC7jaws0TPBXVL_f-hrJ4m-roTZ-MeP_E
Content-Type: application/json

{"content":"Hello World"}
~~~

For posting messages to someone's inbox, use the following

~~~.txt
POST /rest/message/postToInbox/eRMftk64_Fqh4LCjyHo7dr0Gyyf1qX3NYQl47pfDkoI
Content-Type: application/json

{"content":"Hello World"}
~~~
