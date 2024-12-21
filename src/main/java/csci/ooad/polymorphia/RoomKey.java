package csci.ooad.polymorphia;

public class RoomKey {
    Boolean used;

    public RoomKey(){
        this.used = false;
    }

    Boolean isUsed(){
        return used;
    }

    void useKey(){
        used = true;
    }

}
