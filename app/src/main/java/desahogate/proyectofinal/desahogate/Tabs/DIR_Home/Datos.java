package desahogate.proyectofinal.desahogate.Tabs.DIR_Home;


public class Datos {

    private String image;
    private String message;
    private String name;
    private long time;
    private String KeyID;
    private String UID;

    public Datos() {}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }

    public Datos(String image, String message, String name, long time, String KeyID, String UID) {

        this.image = image;
        this.message = message;

        this.name = name;
        this.time = time;
        this.KeyID = KeyID;
        this.UID = UID;
    }
}
