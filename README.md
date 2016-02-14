# DAG

Semplice modulo per implementare DAG *Directed Acyclic Graph*. Lo scopo è dare una interfaccia semplice
per avere dei grafi che mantengano questa proprietà a ogni arco che viene aggiunto.

## Cosa **non è**

Questo progetto non:

* E' una implementazione ottimizata di grafi direzionati aciclici
* E' un framework di per algoritmi di grafi

## Cosa è

Vuole essere solo una interfaccia generica per avere *sotto controllo* la proprietà che il grafo che stiamo 
costruendo è aciclico. Questo permette di costruire algoritmi che usano questa propietà senza preoccuparsi
di doverla reimplementare.

Se poi si vuole usare i propri algoritmi chiedendo scalabilità e performance si può implementare un adapter
a qualche libreria che implementa efficacemente i grafi e agganciarla al posto del backend
di base.

## Linguaggi

Prevedo di fare una implementazione in 

* Kotlin (in corso)
* Python (ASAP)
* C (a medio termine)
* Pure Java 8 (a medio termine)
* C++ (a medio termine)

## Interfaccia

### Kotlin

#### `DAG<T>`

Grafo direzionato aciclico

* Costruttore di base può prendere una collection di `T` per costruire i nodi iniziali (costruttore alternativo 
con nunmero variabile di nodi in ingresso)
* `addNode(T)` aggiunge il nodo (se non esiste) e lo rende
* `addEdge(from, to)` aggiunge un arco da `from` a `to` e manda eccezzione se 
  * `from` e `to` non fanno parte dello stesso grafo
  * il nuovo arco crea un ciclo
* `nodes()` la collection di nodi
* `edges()` la collection di archi
* `[T]` il nodo
* `[T, T]` l'arco se esiste

#### `Node<T>`

Nodo del grafo.

* `context` property in lettura
* `edges()` la colection di edge uscenti come Pair(from, to)
* `nodes()` la colection di nodi collegati come valori
* `bfs()` la lista dei nodi raggiungibili con una ricerca *BFS* dal nodo
* `bfsEdges()` la lista degli archi raggiungibili con una ricerca *BFS* dal nodo
* `dfs()` la lista dei nodi raggiungibili con una ricerca *DFS* dal nodo
* `dfsEdges()` la lista degli archi raggiungibili con una ricerca *DFS* dal nodo
* `ends()` `Set` dei nodi raggiungibili

#### `Edge<T>`

* `from`, `to` nodi
* Scompattabile (implementa `component1` e `component2`)

