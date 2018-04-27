package sample.model;


public class Memory {

    private String name;
    private boolean isHole;
    private int size;
    private int startingAddress;

    public Memory(String name, int startingAddress, int size, boolean isHole) {
        this.name = name;
        this.isHole = isHole;
        this.size = size;
        this.startingAddress = startingAddress;
    }

    public int getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(int startingAddress) {
        this.startingAddress = startingAddress;
    }

    public String getName() {
        return name;
    }

    public boolean isHole() {
        return isHole;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
