package csci.ooad.polymorphia;

public class GateRoom extends Maze.Room {
    private GateRoom correspondingRoom;
    private Boolean isOpened = false;

    public GateRoom(String name) {
        super(name);
    }

    public void connectCorrespondingRoom(GateRoom correspondingRoom) {
        this.correspondingRoom = correspondingRoom;
        this.connect(correspondingRoom);
    }

    Boolean isOpened(){
        return isOpened;
    }

    public void openRoom(RoomKey key){
        if (!key.isUsed()){
            isOpened = true;
        }
    }




}
