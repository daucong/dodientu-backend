package ute.udn.userservice.entity;

public enum Status {
    DRAF_STATUS(0), UN_APRROVE_STATUS(1), APRROVE_STATUS(2), DENY_STATUS(3);

    private int id;

    Status(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
