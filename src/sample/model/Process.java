package sample.model;

public class Process {
    private String name;
    private int size;
    private boolean isAllocated;

    public Process(String name, int size, boolean isAllocated) {
        this.name = name;
        this.size = size;
        this.isAllocated = isAllocated;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    public void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }

}
