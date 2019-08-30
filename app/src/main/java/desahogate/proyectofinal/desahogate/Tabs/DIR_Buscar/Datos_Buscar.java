package desahogate.proyectofinal.desahogate.Tabs.DIR_Buscar;



public class Datos_Buscar {

    private String name;
    private String status;
    private String image;


    public Datos_Buscar(){}

    public Datos_Buscar(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }


    public String getImage() {

        return image;
    }

    public void setImage(String icon) {

        this.image = icon;
    }

}
