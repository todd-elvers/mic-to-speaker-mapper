# mic-to-speaker-mapper

A small, command-line application that allows you to map an arbitrary microphone
to an arbitrary speaker on your computer.

## How to run it

- Download the latest release JAR or, if your OS is Windows, the latest EXE from
the *release* tab

**OR**

- clone the project to a directory
- `cd` to the project root and execute `gradlew run`

Optionally, you could execute `gradlew jar` and then execute the JAR directly.
 
 
## How it works

- You run `m2sm` on the command line
- You're then prompted with all the microphones the app can find on your PC
- After you select your mic, you're prompted with all the speakers the app can find on your PC
- After you select your speaker, the application will create a mapping between the two.
- Terminate the application by pressing `^C` or by closing the terminal. 


## Why did I make this?

I regularly host VR events at my house so I created this tool in order to communicate with the person wearing the VR headset without having to be within swinging range of their controllers.  If you ever host VR events at your house I believe you'll find this tool useful!
