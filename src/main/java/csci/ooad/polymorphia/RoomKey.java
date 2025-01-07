package csci.ooad.polymorphia;

public class RoomKey {
    private Boolean used;

    public RoomKey(){
        this.used = false;
    }

    public Boolean isUsed(){
        return used;
    }

    public void useKey(){
        used = true;
    }

}
