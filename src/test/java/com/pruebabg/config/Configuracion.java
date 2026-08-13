package com.pruebabg.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Configuracion {

    private static final String ARCHIVO = "config.properties";
    private static final Properties PROPIEDADES = cargar();

    private Configuracion() {
    }

    public static String baseUrl() {
        return valorDe("base.url");
    }

    public static String urlDe(String ruta) {
        return baseUrl() + ruta;
    }

    public static boolean headless() {
        return Boolean.parseBoolean(valorDe("headless"));
    }

    public static double timeoutMs() {
        return Double.parseDouble(valorDe("timeout.ms"));
    }

    public static double slowMoMs() {
        return Double.parseDouble(valorDe("slowmo.ms"));
    }

    private static String valorDe(String clave) {
        String desdeSistema = System.getProperty(clave);
        if (desdeSistema != null && !desdeSistema.isBlank()) {
            return desdeSistema;
        }
        String desdeArchivo = PROPIEDADES.getProperty(clave);
        if (desdeArchivo == null) {
            throw new IllegalStateException(
                    "No se encontró la clave de configuración '" + clave + "' en " + ARCHIVO);
        }
        return desdeArchivo;
    }

    private static Properties cargar() {
        Properties propiedades = new Properties();
        try (InputStream entrada = Configuracion.class.getClassLoader().getResourceAsStream(ARCHIVO)) {
            if (entrada == null) {
                throw new IllegalStateException(
                        "No se encontró " + ARCHIVO + " en el classpath de pruebas.");
            }
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + ARCHIVO, e);
        }
        return propiedades;
    }
}
