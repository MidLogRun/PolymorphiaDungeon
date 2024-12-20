package csci.ooad.polymorphia;

public class RoomKey {
    Maze.Room room;
    Boolean used = false;

    public RoomKey(Maze.Room room){
        this.room = room;
    }

    Boolean isUsed(){
        return used;
    }

    void useKey(){
        used = true;
    }

    public Maze.Room getRoom(){
        return room;
    }
}
