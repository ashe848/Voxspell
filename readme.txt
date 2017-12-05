Note: Final Release has since been made and all features completed and available on the Master branch.
Execution instructions unchanged from Beta below:

October 2016
SE206 Assignment 4 Voxspell Beta Release
Target audience: elderly users on Healthbot Robot

Abby Shen
ashe848
269481021

Tested on Remote Linux 64 in UG4
Eclipse Mars 2
Java SE 1.7


=============================================
PLEASE EXECUTE THE JAR FILE FROM COMMAND LINE
=============================================

Open terminal and change to the directory containing Voxspell.jar 
enter the following command:

>>>>>>>>>>>>>
java -jar Voxspell.jar &> /dev/null
<<<<<<<<<<<<<

Note: double-clicking the jar file will not work as it can't find the .resources/ folder (on Ubuntu)


=============================================
!!! IMPORTANT INFORMATION BELOW !!!
=============================================

1. Note the spelling aid is case sensitive. Because "I" should never be spelt with lower case, as with proper nouns, etc.

2. When the video runs, the following lines will show on the command line
They are not actually errors that affect the application
(This was also the case when VLCJ was demonstrated in class)

SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Fontconfig warning: FcPattern object size does not accept value "0"
Fontconfig warning: FcPattern object size does not accept value "0"
Fontconfig warning: FcPattern object size does not accept value "0"
Fontconfig warning: FcPattern object size does not accept value "0"

=============================================

* Help screens are not yet complete


* Blocks of code with JavaDoc @author Abby S were written by me for Assignment4

* All other code was pair programmed for Assignment3 with tosw164 (unless stated otherwise)
   There are also slight modifications, but not large amounts of rewriting
   
* Sample sentences for NZCER-spelling-lists.txt were also created by myself and tosw164.
   But due to the lack of time, it was not implemented in Assignment3.

=============================================
REQUIREMENTS

Please do not remove the hidden .resources/ folder in the directory containing Voxspell.jar, on the same level
This folder contains necessary files used in the running of Voxspell

Also do not remove NZCER-spelling-lists.txt in spellinglists/ and ffmpeg_reward_video.avi in rewardvideos/
They are the defaults

On any launch, if the any of the above don't exist, Voxspell sees this as a fatal error and won't launch at all
These files won't be deleted by exiting the application
Altering them outside of this application may produce unexpected behaviour ¨C so don¡¯t do it


=============================================
POTENTIAL STRANGE SITUATIONS	

===

If SOUND doesn't work, create a new directory (not on USB) and move the hidden .resources folder and Voxspell.jar into it

It was encountered once that a folder copied and pasted from USB wouldn't play sound, 
but once moved contents into a newly made directory it works again.

===

On level chooser after resetting all stats / first launch, System.exit() strips around 30 pixels off 
the bottom of frame on just the next launch, but otherwise it doesn't happen.

This only appears to be the case on the UG4 lab computers
Because we have no idea why it happens, we have added 30px to the height of our frame to account for this, 
so no content is cut off.

This adding of 30px doesn't affect the overall aesthetics of our application at all.


A similar situation is found on Windows, except Windows just constantly has 30px less in the height

===

It occurred once that video progress bar doesn't work on the very first time playing.
The situation was not able to be replicated, but if it does happen, try it again.
