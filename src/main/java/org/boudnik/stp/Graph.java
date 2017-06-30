package org.boudnik.stp;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Alexandre_Boudnik
 * @since 06/28/17 22:10
 */
public class Graph {
    private int p = 0;
    private Node finish = null;
    private final List<Vertex> vertices = new ArrayList<>();
    Set<Node> list = new LinkedHashSet<>();
    Set<Node> processed = new HashSet<>();

    private static class Node implements Callable<Boolean> {
        String name;

        @Override
        public Boolean call() throws Exception {
            System.out.println(this);
            for (int i = 0; i < 10_000.; i++) {
                SecureRandom s = new SecureRandom();
            }
            return true;
        }

        public Node(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Vertex {
        Node from;
        Node to;

        Vertex(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return from + "->" + to;
        }
    }

    Graph sequential(Node first, Node... rest) {
        List<Node> sequential = concatenate(new Node[]{first}, rest);
        for (Node node : sequential) {
            vertices.add(new Vertex(finish, node));
            finish = node;
            list.add(node);
        }
        return this;
    }

    Graph parallel(Node first, Node second, Node... rest) {
        List<Node> parallel = concatenate(new Node[]{first, second}, rest);
        Node f = new Node("parallel." + p++);
        for (Node node : parallel) {
            vertices.add(new Vertex(finish, node));
            vertices.add(new Vertex(node, f));
            list.add(node);
        }
        list.add(f);
        finish = f;
        return this;
    }

    @NotNull
    private static List<Node> concatenate(Node[] f, Node[] s) {
        return new ArrayList<Node>() {{
            addAll(Arrays.asList(f));
            addAll(Arrays.asList(s));
        }};
    }

    public Future<Boolean> execute(ExecutorService executor) {
        Iterator<Vertex> iterator = vertices.iterator();
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            if (vertex.from == null) {
                iterator.remove();
                return executor.submit(wrap(executor, vertex.to));
            }
        }
        return null; //never
    }

    Callable<Boolean> wrap(ExecutorService executor, Callable<Boolean> callable) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception ignored) {
            } finally {
                Set<Node> toCall = new HashSet<>();
                synchronized (vertices) {
                    Iterator<Vertex> iterator = vertices.iterator();
                    while (iterator.hasNext()) {
                        Vertex vertex = iterator.next();
                        if (vertex.from == callable) {
                            iterator.remove();
                            vertex.from = null;
                            toCall.add(vertex.to);
                        }
                    }
                }
                for (Node node : toCall) {
                    if (processed.add(node)) {
                        executor.submit(wrap(executor, node));
                    }
                }
            }
            return null;
        };
    }

    public static void main(String[] args) {
        Graph graph = new Graph().sequential(
                new Node("1"),
                new Node("2")
        ).parallel(
                new Node("3"),
                new Node("4"),
                new Node("5")
        ).sequential(
                new Node("done")
        );

        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            Boolean r = graph.execute(executor).get();
            System.out.println("r = " + r);
        } catch (Exception ignored) {
        } finally {
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } finally {
                executor.shutdown();
            }
        }

        System.out.println("graph = " + graph);
    }
}
