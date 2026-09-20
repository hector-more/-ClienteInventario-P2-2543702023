package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.api.ProductoApi;
import sv.edu.utec.api.RespuestaProductos;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {

    private final HttpClient cliente;
    private final ObjectMapper mapper;

    public ProveedorAPI() {
        cliente = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
    }

    public List<Producto> obtenerProductos(int limite)
            throws IOException, InterruptedException {

        String url = "https://dummyjson.com/products?limit="
                + limite + "&select=title,stock";

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        if (respuesta.statusCode() != 200) {
            throw new IOException(
                    "Error al consultar la API. Código: "
                            + respuesta.statusCode()
            );
        }

        RespuestaProductos respuestaProductos =
                mapper.readValue(respuesta.body(), RespuestaProductos.class);

        List<Producto> productos = new ArrayList<>();

        for (ProductoApi productoApi : respuestaProductos.getProducts()) {
            productos.add(productoApi.aProducto());
        }

        return productos;
    }
}
