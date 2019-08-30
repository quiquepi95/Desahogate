package desahogate.proyectofinal.desahogate.Tabs.DIR_Notificaciones;



public class Datos_Notificaciones {

    private String name;
    private String from;
    private String image;


    public Datos_Notificaciones(){}

    public Datos_Notificaciones(String name, String from, String image) {
        this.name = name;
        this.from = from;
        this.image = image;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {

        this.from = from;
    }


    public String getImage() {

        return image;
    }

    public void setImage(String icon) {

        this.image = icon;
    }

}
