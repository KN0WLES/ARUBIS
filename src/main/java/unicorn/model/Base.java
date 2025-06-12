package unicorn.model;

import java.io.Serializable;
import java.lang.ModuleLayer.Controller;
import java.util.Objects;

/**
 * Clase base para serialización de modelos a archivos.
 * Proporciona la infraestructura común para todos los modelos que necesitan
 * persistencia en el sistema.
 * 
 * @description Funcionalidades principales:
 *                   - Serializar objetos a formato de texto para almacenamiento.
 *                   - Deserializar texto a objetos del modelo.
 *                   - Generar información legible para presentación.
 *                   - Implementación genérica de equals y hashCode basada en ID.
 *                   - Gestión de identificadores únicos para objetos del modelo.
 * 
 * @note Esta clase no debe manejar la persistencia directamente.
 *       La gestión de colecciones (HashMap, etc.) debe realizarse en los controllers.
 * 
 * @see Account
 * @see Controller
 * 
 * @param <T> Tipo de modelo que extiende esta clase base
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public abstract class Base<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Obtiene el identificador único del objeto.
     * Las clases hijas deben implementar este método para proporcionar su ID.
     *
     * @return El identificador único del objeto
     */
    protected abstract String getId();

    /**
     * Compara este objeto con otro objeto para determinar si son iguales.
     * Dos cuentas se consideran iguales si tienen el mismo identificador único (id).
     *
     * @param o El objeto a comparar con esta objeto
     * @return true si el objeto es una cuenta con el mismo id, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base<?> base = (Base<?>) o;
        return Objects.equals(getId(), base.getId());
    }
    
    /**
     * Genera un código hash para esta objeto basado en su identificador único.
     * Este método es consistente con equals(): si dos objetos son iguales según equals(),
     * sus códigos hash también serán iguales.
     *
     * @return El código hash generado para esta cuenta
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * Escapa los caracteres especiales en una cadena para su uso en serialización.
     * Reemplaza el carácter '|' por '\|' para evitar conflictos en la representación de texto.
     * Si el valor es null, devuelve "null" como cadena.
     *
     * @param value La cadena a escapar
     * @return La cadena escapada o "null" si el valor es null
     */
    protected String escapeForSerialization(String value) {
        return value != null ? value.replace("|", "\\|") : "null";
    }

    /**
     * Serializa el objeto a una representación en formato texto.
     * 
     * @return Cadena que representa el estado del objeto para almacenamiento
     */
    public abstract String toFile();

    /**
     * Deserializa una cadena de texto para reconstruir un objeto del modelo.
     * 
     * @param line Cadena que contiene los datos serializados
     * @return Objeto del tipo T reconstruido a partir de los datos
     */
    public abstract T fromFile(String line);

    /**
     * Genera una representación legible del objeto para presentación.
     * 
     * @return Cadena con información formateada del objeto
     */
    public abstract String getInfo();
}