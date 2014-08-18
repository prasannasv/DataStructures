import java.util.*;

/*
 * Given a partial set of ordered words in an unknown language with latin alphabets,
 * enlist one possible ordering of the alphabets.
 * Ex: xza, ayh, ples, plares, bhaaz, bnc
 * One possible ordering of alphabets is: x, e, a, p, b, z, h, n, y, c, l, s, r
 * The approach is to build a graph with alphabets as vertices and an edge between them if one "follows" other.
 * Once we have the dependency graph, the desired output is one possible topological sorting of the vertices.
 */
public class AlienLanguage {
  public static void main(String[] args) {
    String[] sample = {"xza", "ayh", "ples", "plares", "bhaaz", "bnc"};
    System.out.println(new AlienLanguage().findPossibleAlphabetOrdering(sample));
  }

  private List<Character> findPossibleAlphabetOrdering(String[] words) {
    Graph<Character> followsGraph = new Graph<Character>();
    buildFollowsGraph(followsGraph, words);
    System.out.println("followsGraph: " + followsGraph);

    return followsGraph.topologicallySort();
  }

  private void buildFollowsGraph(Graph<Character> followsGraph, String[] words) {
    String prev = words[0];
    insertVertices(followsGraph, prev);

    for (int i = 1; i < words.length; ++i) {
      insertVertices(followsGraph, words[i]);
      generateRelationship(followsGraph, prev, words[i]);
      prev = words[i];
    }
  }

  private void insertVertices(Graph<Character> followsGraph, String word) {
    for (int i = 0; i < word.length(); ++i) {
      followsGraph.addVertex(word.charAt(i));
    }
  }

  private void generateRelationship(Graph<Character> followsGraph, String prev, String curr) {
    int i = 0;
    int j = 0;
    while (i < prev.length() && j < curr.length() && prev.charAt(i) == curr.charAt(j)) {
      ++i;
      ++j;
    }

    if (i < prev.length() && j < curr.length()) {
      followsGraph.addEdge(curr.charAt(j), prev.charAt(i));
    }
  }

}

class Graph<V> {
  private Map<V, List<V>> edgeMap = new HashMap<V, List<V>>();

  public void addVertex(V v) {
    if (!edgeMap.containsKey(v)) {
      edgeMap.put(v, new LinkedList<V>());
    }
  }

  public void addEdge(V from, V to) {
    List<V> relations = edgeMap.get(from);
    relations.add(to);
  }

  public List<V> topologicallySort() {
    List<V> orderedVertices = new LinkedList<V>();

    Set<V> discovered = new HashSet<V>();
    Set<V> processed = new HashSet<V>();
    Stack<V> dfsStack = new Stack<V>();

    for (V startNode : edgeMap.keySet()) {
      if (discovered.contains(startNode)) {
        continue;
      }
      dfsStack.push(startNode);
      while (!dfsStack.isEmpty()) {
        V top = dfsStack.peek();
        if (!discovered.contains(top)) {
          discovered.add(top);
          for (V predecessor : edgeMap.get(top)) {
            dfsStack.push(predecessor);
          }
        } else {
          dfsStack.pop();
          if (!processed.contains(top)) {
            processed.add(top);
            orderedVertices.add(top);
          }
        }
      }
    }

    return orderedVertices;
  }

  public String toString() {
    return edgeMap.toString();
  }
}
