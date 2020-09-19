/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import clasesUtiles.Taller;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import org.json.JSONObject;
import org.primefaces.model.file.UploadedFile;
import javax.servlet.http.Part;

/**
 *
 * @author david
 */
@Named(value = "cargarTalleresBean")
@SessionScoped
public class CargarTalleresBean implements Serializable {

    private String id;
    private String latitud;
    private String longitud;
    private String nombre;
    private String direccion;
    private String telefono;

    private List<Taller> talleres;

    private Part uploadFile;
    
    private UploadedFile file;

    public CargarTalleresBean() {
        try {
            inicializar();
        } catch (IOException ex) {
            Logger.getLogger(CargarTalleresBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String dummyAction() throws MessagingException {
        System.out.println("Uploaded File Name Is :: " + uploadFile.getName());
        
        return "";
    }
    
    public void upload() {
        if (file != null) {
            System.out.println("Successful "+ file.getFileName() + " is uploaded.");
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            try {
                cargarArchivoTalleres();
            } catch (IOException ex) {
                Logger.getLogger(CargarTalleresBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void inicializar() throws IOException {
        this.id = "";
        this.latitud = "";
        this.longitud = "";
        this.nombre = "";
        this.direccion = "";
        this.telefono = "";
        this.talleres = new ArrayList<>();

        //cargarArchivoTalleres();
    }

    private void cargarArchivoTalleres() throws FileNotFoundException, IOException {
        String row = "";
        String pathToCsv = "C:/Users/david/Documents/Universidad/Practica_de_ingenieria_IV/ubicaciones.txt";

        //file.g
        //File csvFile = new File(pathToCsv);
        //if (csvFile.isFile()) {
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            int cont = 0;
            while ((row = csvReader.readLine()) != null) {
                if (cont == 0) {
                    String[] header = row.split(",");
                    for (String dataAux : header) {
                        System.out.println(dataAux);
                    }

                } else {
                    String[] data = row.split(",");
                    Taller taller = new Taller(Double.parseDouble(data[1]), Double.parseDouble(data[2]),
                            data[3], data[4], Integer.parseInt(data[5]));
                    talleres.add(taller);

                }
                cont++;

            }
            csvReader.close();
        //}
    }

    public void agregarTaller() {
        try {
            Taller taller = new Taller(Double.parseDouble(latitud), Double.parseDouble(longitud), nombre, direccion, Integer.parseInt(telefono));
            talleres.add(taller);
            limpiar();

        } catch (Exception e) {
            limpiar();
            System.out.println(e);
        }

    }

    private void limpiar() {
        this.id = "";
        this.latitud = "";
        this.longitud = "";
        this.nombre = "";
        this.direccion = "";
        this.telefono = "";
    }

    public void cargar() throws IOException {
        for (Taller tallerAux : talleres) {
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromPojo = gsonBuilder.toJson(tallerAux);
            System.out.println(jsonFromPojo);
            enviarRest(jsonFromPojo);
        }
        
        inicializar();
    }

    public void enviarRest(String json) {
        try {
            //LOGGER.debug("Ingresa a enviar REST {}",json);
            URL url = new URL("https://proyectoprueba-67c94.firebaseio.com/talleres.json");
            //URL url = new URL("https://mapa-6e459.firebaseio.com/Ubicaciones.json");
            

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
            System.out.println("Error en la URL de destino https://proyectoprueba-67c94.firebaseio.com/taller.json");
        } catch (IOException ex) {
            System.out.println("Error {}" + ex);
        }

    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    

    private int generarId() {

        int tam = 0;
        tam = talleres.size() + 1;
        return tam;

    }

    public Part getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(Part uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Taller> getTalleres() {
        return talleres;
    }

    public void setTalleres(List<Taller> talleres) {
        this.talleres = talleres;
    }
    
    

}
