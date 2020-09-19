/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import clasesUtiles.Servicio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.json.JSONObject;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author david
 */
@Named(value = "cargarServiciosBean")
@SessionScoped
public class CargarServiciosBean implements Serializable {

    private String id;
    private String grupo;
    private String nombre;
    private String telefono;

    private List<Servicio> servicios;

    private List<String> grupos;
    private SelectItem[] gruposReal;

    private UploadedFile file;

    private String mensajeAux;

    public CargarServiciosBean() {

        try {
            inicializar();
        } catch (IOException ex) {
            Logger.getLogger(CargarServiciosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inicializar() throws IOException {
        this.id = "";
        this.grupo = "";
        this.nombre = "";
        this.telefono = "";
        this.mensajeAux = "";
        this.servicios = new ArrayList<>();
        llenarLstGrupos();
        inicializarItemsGrupos();

    }

    public void upload() {
        if (file != null) {
            System.out.println("Successful " + file.getFileName() + " is uploaded.");
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            try {
                cargarArchivoServicios();
            } catch (IOException ex) {
                Logger.getLogger(CargarTalleresBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void llenarLstGrupos() {
        this.grupos = new ArrayList<>();
        grupos.add("1");
        grupos.add("2");
        grupos.add("3");
        grupos.add("4");
        grupos.add("5");

    }

    private void inicializarItemsGrupos() {
        this.gruposReal = new SelectItem[5];
        int i = 0;
        gruposReal[i++] = new SelectItem("1", "Grua");
        gruposReal[i++] = new SelectItem("2", "Repuestos");
        gruposReal[i++] = new SelectItem("3", "Centro Diagnostico");
        gruposReal[i++] = new SelectItem("4", "Soat");
        gruposReal[i++] = new SelectItem("5", "Accesorios");
    }

    private void limpiar() {
        this.id = "";
        this.grupo = "";
        this.nombre = "";
        this.telefono = "";
        llenarLstGrupos();
    }

    public void agregarServicio() {
        mensajeAux = "";
        try {
            Servicio servicio = new Servicio(Integer.parseInt(grupo), nombre, Long.parseLong(telefono));
            servicios.add(servicio);
            limpiar();

        } catch (Exception e) {
            limpiar();
            System.out.println(e);
        }

    }

    private void cargarArchivoServicios() throws FileNotFoundException, IOException {
        String row = "";
        String pathToCsv = "C:/Users/david/Documents/Universidad/Practica_de_ingenieria_IV/Servicios.txt";

        BufferedReader csvReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        int cont = 0;
        while ((row = csvReader.readLine()) != null) {
            if (cont == 0) {
                String[] header = row.split(",");

            } else {
                String[] data = row.split(",");
                Servicio servicio = new Servicio(Integer.parseInt(data[1]), data[2], Long.parseLong(data[3]));
                servicios.add(servicio);

            }
            cont++;

        }
        csvReader.close();

    }

    private String generarId() {

        int tam = 0;
        tam = servicios.size() + 1;
        return String.valueOf(tam);

    }

    public void cargar() throws IOException {
        for (Servicio servicioAux : servicios) {
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromPojo = gsonBuilder.toJson(servicioAux);
            System.out.println(jsonFromPojo);
            enviarRest(jsonFromPojo);
        }

        inicializar();
        mensajeAux = "Archivo cargado con exito";
    }

    public void enviarRest(String json) {
        try {
            //LOGGER.debug("Ingresa a enviar REST {}",json);
            URL url = new URL("https://proyectoprueba-67c94.firebaseio.com/servicios.json");
            //URL url = new URL("https://mapa-6e459.firebaseio.com/Servicios.json");

            JSONObject jsonObject = new JSONObject(json);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String jsonEnviar = jsonObject.toString();

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonEnviar.getBytes());
                os.flush();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    InputStream in = conn.getErrorStream();
                    String encoding = conn.getContentEncoding();
                    encoding = encoding == null ? "UTF-8" : encoding;
                    //String respuesta = (IOUtils. (in, encoding));
                    //respuesta = encontrarRespuestaError(respuesta);

                    //LOGGER.error(respuesta);
                }
                System.out.println("Respuesta: " + String.valueOf(conn.getResponseCode()) + " mensaje" + String.valueOf(conn.getResponseMessage()));
            }
        } catch (MalformedURLException ex) {
            System.out.println("Error en la URL de destino https://proyectoprueba-67c94.firebaseio.com/service.json");
        } catch (IOException ex) {
            System.out.println("Error {}" + ex);
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public List<String> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public SelectItem[] getGruposReal() {
        return gruposReal;
    }

    public void setGruposReal(SelectItem[] gruposReal) {
        this.gruposReal = gruposReal;
    }

    public String getMensajeAux() {
        return mensajeAux;
    }

    public void setMensajeAux(String mensajeAux) {
        this.mensajeAux = mensajeAux;
    }
    
    

}
