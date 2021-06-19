# baba-is-you-clone
A minimal clone of the baba is you game.

Rules of the game can be found at [baba is you wiki](https://babaiswiki.fandom.com/wiki/Baba_Is_You_Wiki) or at *docs/dev.pdf*.

# requirements
- ant
- Java JRE 15+ installed

# how to run
```bash 
$ ant #compile
$ java -jar --enable-preview baba.jar [options] #run
```

Options are the following:
- **--level FILE** launches the level specified in the file of name FILE.
- **--levels DIR** launches all the levels contained in the DIR directory.

Launching the game without any option will launch a default level.
All the available levels are listed in the *levels* directory.

# Authors
LAGNEAU Gaétan - NGUYEN NgocTram
