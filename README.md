# network-simplex
Implementation of the network simplex algorithm, see e.g. 
Vašek Chvátal: Linear Programming. Freeman, 1983.

This programming project was part of this class [http://www.zib.de/groetschel/teaching/WS1415/VL-WS1415.htm](http://www.zib.de/groetschel/teaching/WS1415/VL-WS1415.htm).

My implementation was among the ones with top performance. Test data can be found at [http://elib.zib.de/pub/mp-testdata/mincost/netg/index.html](http://elib.zib.de/pub/mp-testdata/mincost/netg/index.html) 
with a description of the data format [here](http://elib.zib.de/pub/mp-testdata/mincost/netg/info).

A very simple example instance is contained in the file simple_flow.

#### Compile and Run
```
cd network-simplex
mkdir bin
javac src/*.java -d bin
cd bin
java nsimplex ../simple_flow
```
