# CommandLineAutomation
The start of a command line automation project; a concept auxilliary application for an AI network administrator.

There is actually two projects in here; a client and a server that need to be built and run individually. The server is all in one folder, the client includes everything else. 

Note: Due to the nature of some of the example commands, you will have to run the client jar file with administrative privledges. Don't try to run any of the provided commands or functions if you do not fully understand them... You could end up formatting your computer or worse!

Network communications with datagram sockets make it possible to send administration commands over a network as well as to execute scripts remotely. Static commands are stored in an SQLite database for easy modification and reference.

Access a list of commands with descriptions in the DB file with 'cmd <computer host name> listcommands'
  
Search for command details based on a keyword with 'cmd <computer host name> commandslike <keyword>'
  
Translate a command into it's original Microsoft format with 'cmd <computer host name> commandtranslation <tagName>'
  

There are a few things that still need to be developed into this project to make it a little more robust. I may add a scheduling system and a trigger event system.
I have already provided a simple alert notification option that would compliment and event trigger situation nicely. A simple example of this can be seen in CommandLineExecutor.java with the DISM based health scan function. The tagName is healthcheck.

Side notes:
I have been wanting to add full command line automation for a distributed, NLP based project. This is typically done in Powershell but I want to see if I can streamline a few things. However, over time as I get this fleshed out, I will be adding PS scripts along with batch scripts and at some point, linux support with bash. From what I have seen, it won't take long to pick up PS syntax if I am using it regularly.

I will probably not be making a GUI for this as for my own use, I can plug it in directly to a natural language interface. (Administation via voice command!)

For this project, it's mostly a demo of Java socket usage and some basic network administration goodies with automation in mind.

When it's all done, I will probably upload a video on YouTube with a demo of an AI network administrator managing files and processing tasks across a network. Updates will depend on how busy I am. Sometimes there will be a lot, other times it may take a while.
