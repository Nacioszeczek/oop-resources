import java.util.AbstractList;
import java.util.NoSuchElementException;

public class CustomList<T> extends AbstractList<T> {
    private static class Node<T> {
        private T value;
        private Node<T> next;
        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;

    public CustomList() {
        this.head = this.tail = null;
    }

    @Override
    public int size() {
        int index = 0;
        Node<T> current = head;
        while(current != null){
            index++;
            current = current.next;
        }
        return index;
    }

    @Override
    public T get(int i) {
        Node<T> node = this.head;
        for(int k = 0; k < i; k++) {
            if(node == null) throw new IndexOutOfBoundsException();
            node = node.next;
        }
        if(node == null) throw new IndexOutOfBoundsException();
        return node.value;
    }

    @Override
    public boolean add(T t) {
        addLast(t);
        return true;
    }

    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);
        if (this.tail == null) {
            this.tail = this.head = newNode; // była pusta lista, jest 1 element
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }
    }

    public T getLast() {
        if (this.tail == null) {
            throw new RuntimeException("Lista jest pusta");
        }
        return this.tail.value;
    }

    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        if (this.head == null) {
            this.head = this.tail = newNode;
        } else {
            newNode.next = this.head;
            this.head = newNode;
        }
    }

    public T getFirst() {
        if (this.head == null) {
            throw new RuntimeException("Lista jest pusta");
        }
        return this.head.value;
    }

    public T removeFirst() {
        if (this.head == null) {
            throw new RuntimeException("Lista jest pusta");
        }
        T value = this.head.value;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;   // usunięto ostatni element
        }
        return value;
    }

    public T removeLast() {
        if (this.tail == null) {
            throw new RuntimeException("Lista jest pusta");
        }
        T value = this.tail.value;

        if (head == tail) {
            head = tail = null; // lista jest pusta
            return value;
        }

        Node<T> current = this.head;
        while (current.next != tail) {
            current = current.next;
        }
        this.tail = current;
        this.tail.next = null;
        return value;
    }





}
