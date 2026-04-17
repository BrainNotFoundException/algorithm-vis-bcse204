# Algorithm comparator written in java

## [Check Release](https://github.com/BrainNotFoundException/algorithm-vis-bcse204/releases/tag/Submission)

---

## Steps to run:

### 1 Compile code into a folder
```
javac -d build *.java utils/*.java themes/*.java
```

### 2 Go to build folder and run
```
cd build
```

From here you can either run it directly:
```
java Main
```

OR make a jar file to run

```
jar cfe Comparator.jar Main -C build .
java -jar Comparator.jar
```