# WavesNodeUI - A simple user interface for a node of the Waves platform
After cloning the repository, the tool could be build with the following commands:
```sh
cd src
javac -cp ../gson-2.8.0.jar:. com/wavesgo/waves/node/ui/*.java
jar -cmf META-INF/MANIFEST.MF WavesNodeUI.jar *
cp WavesNodeUI.jar ..
cd ..
```
After the successful build process, the tool could be started with:
```sh
java -cp gson-2.8.0.jar -jar WavesNodeUI.jar
```
