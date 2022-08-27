# CommandLineAutomation
The start of a command line automation project. An auxilliary application for an AI network administrator.

Note: Due to the nature of some of these commands, you may have to run the jar file with administrative provledges.

So far, it's really just a list of commands that can be run directly from Java via main method. These commands by default execute like Windows command prompt but can also be sent to Powershell.

I have been wanting to add full command line automation for a distributed network, NLP based project. This is typically done in Powershell but my understanding of PS is still limmited. However, over time as I get this fleshed out, I will be adding PS scripts along with batch scripts and at some point, linux support with bash. From what I have seen, it won't take long to pick up PS syntax if I am using it regularly.

I will probably not be making a central control interface for this as for my own use, I can plug it in directly to a natural language interface. Instead, I will create the means to send and receive commands over a network with TCP sockets in java and leave the rest open to customization. This means there will be a client side and a server side included.

When it's all done, I will probably upload a video on YouTube with a demo of an AI network administrator. Updates will depend on how busy I am. Sometimes there will be a lot, othertimes it may take a while.
