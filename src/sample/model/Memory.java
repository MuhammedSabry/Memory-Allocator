package sample.model;

import java.util.List;

public class Memory {

    private String name;
    private boolean isHole;
    private int size;
    private int startingAddress;

    public Memory() {

    }

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

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean hole) {
        isHole = hole;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static void organizeMemory(List<Memory> memoryList) {
        for (int i = 0; i < memoryList.size(); i++) {
            if (memoryList.get(i).isHole() && i < (memoryList.size() - 1)) {
                if (memoryList.get(i + 1).isHole()) {
                    memoryList.get(i).setSize(memoryList.get(i).getSize() + memoryList.get(i + 1).getSize());
                    memoryList.remove(i + 1);
                }
            }
        }
    }

    @Override
    public String toString() {

        return this.getStartingAddress()
                + " -> "
                + this.getName();
    }
}
