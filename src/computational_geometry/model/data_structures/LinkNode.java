package computational_geometry.model.data_structures;

/**
 * Doubly linked node mostly used for circular list
 * @author eloi
 *
 * @param <T> : The type of the value inside the node
 */
public class LinkNode<T extends Comparable<T>> implements
        Comparable<LinkNode<T>> { // circular list

    private T value;
    private LinkNode<T> next;
    private LinkNode<T> prev;

    public LinkNode(T value, LinkNode<T> next, LinkNode<T> prev) {
        this.value = value;
        this.next = next;
        this.prev = prev;
    }

    public LinkNode(LinkNode<T> elem) {
        this(elem.getValue(), elem.getNext(), elem.getPrev());
    }

    public LinkNode(T obj) {
        this(obj, null, null);
    }

    public LinkNode<T> insertAfter(LinkNode<T> elem) {
        elem.setNext(this.getNext());
        elem.setPrev(this);
        this.getNext().setPrev(elem);
        this.setNext(elem);

        return elem;
    }

    public LinkNode<T> insertBefore(LinkNode<T> elem) {
        return this.getPrev().insertAfter(elem);
    }

    public LinkNode<T> remove() {
        this.getNext().setPrev(this.getPrev());
        this.getPrev().setNext(this.getNext());

        this.setNext(null);
        this.setPrev(null);

        return this;
    }

    /**
     * Check the values and one level of recursivity
     * @param elem
     * @return
     */
    public boolean equals(LinkNode<T> elem) {
        if (elem == null)
            return false;
        return this.value.equals(elem.value)
                && next.value.equals(elem.next.value)
                && prev.value.equals(elem.prev.value);
    }

    public String toString() {
        if (value != null)
            ;
        return this.value.toString();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public LinkNode<T> getNext() {
        return next;
    }

    public void setNext(LinkNode<T> next) {
        this.next = next;
    }

    public LinkNode<T> getPrev() {
        return prev;
    }

    public void setPrev(LinkNode<T> prev) {
        this.prev = prev;
    }

    @Override
    public int compareTo(LinkNode<T> o) {
        return this.value.compareTo(o.value);
    }

}
