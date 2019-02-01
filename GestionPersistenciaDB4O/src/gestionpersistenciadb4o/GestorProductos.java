package gestionpersistenciadb4o;

import clasesIoc.Article;
import clasesIoc.Envas;
import clasesIoc.Magatzem;
import clasesIoc.Producte;
import clasesIoc.UnitatDeMesura;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.Db4oException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.util.ArrayList;
import java.util.List;

public class GestorProductos {

    private ObjectContainer baseDatos;
    private EmbeddedConfiguration config;
    private String nombreArchivo = "baseDatos.db4o";

    public GestorProductos() {

        this.config = Db4oEmbedded.newConfiguration();

        this.config.common().objectClass(Magatzem.class).cascadeOnDelete(true);
        this.config.common().activationDepth(6);
        this.config.common().updateDepth(6);

        this.baseDatos = Db4oEmbedded.openFile(this.config, this.nombreArchivo);
    }
    
    /* ---- Gestión de la Base de Datos ---- */
    public void abrirConexion() {
        if (this.baseDatos.ext().isClosed()) {
            this.baseDatos = Db4oEmbedded.openFile(this.config, this.nombreArchivo);
            System.out.println("Conexión a la Base de Datos abierta");
        } else {
            System.out.println("La conexión a la Base de Datos ya está abierta");
        }
    }
    
    public void cerrarConexion() {
        if (!this.baseDatos.ext().isClosed()) {
            this.baseDatos.close();
            System.out.println("Conexión a la Base de Datos cerrada");
        } else {
            System.out.println("La conexión a la Base de Datos ya está cerrada");
        }
    }
    
    public void guardarCambios() {
        this.baseDatos.commit();
    }
    
    public void guardarElemento(Object elemento) {
        this.baseDatos.store(elemento);
        guardarCambios();
        System.out.println(elemento.toString() + " añadido.");
    }
    
    public void eliminarElemento(Object elemento) {
        try {
            this.baseDatos.delete(elemento);
            System.out.println(elemento.toString() + " eliminado.");
        } catch(Db4oException e) {
            this.baseDatos.rollback();
            System.out.println("No se puedo eliminar el elemento.");
            System.out.println(e);
        }
        guardarCambios();
    }
    
    /* ---- Consultas de un solo Elemento ---- */
    public Article obtenerObjeto(Article art) {
        Object objeto = consultaObjeto(art);
        return (Article) objeto;
    }
    
    public Envas obtenerObjeto(Envas env) {
        Object objeto = consultaObjeto(env);
        return (Envas) objeto;
    }
    
    public Magatzem obtenerObjeto(Magatzem mag) {
        Object objeto = consultaObjeto(mag);
        return (Magatzem) objeto;
    }
    
    public Producte obtenerObjeto(Producte pro) {
        Object objeto = consultaObjeto(pro);
        return (Producte) objeto;
    }
    
    public UnitatDeMesura obtenerObjeto(UnitatDeMesura udm) {
        Object objeto = consultaObjeto(udm);
        return (UnitatDeMesura) objeto;
    }
    
    private Object consultaObjeto(Object elemento) {
        
        ObjectSet resultado = this.baseDatos.queryByExample(elemento);
        
        if (resultado.hasNext()) {
            elemento = resultado.next();
            if (resultado.hasNext()) {
                throw new Db4oException("Patrón Ambiguo.");
            }
        } else {
            this.baseDatos.store(elemento);
            this.baseDatos.commit();
            System.out.println(elemento.toString() + " añadido.");
        }
        
        return elemento;
    }
    
    /* ---- Consultas de todos los Elementos ---- */
    public List<Article> obtenerArticulos() {
        
        List<Article> lista = this.baseDatos.queryByExample(new Article());
        
        System.out.println("--> Artículos en la Base de Datos:");
        for (Object obj: lista) {
            System.out.println(obj.toString());
        }
        
        return lista;
    }
    
    public List<Envas> obtenerEnvases() {
        
        List<Envas> lista = this.baseDatos.queryByExample(new Envas());
        
        System.out.println("--> Envases en la Base de Datos:");
        for (Object obj: lista) {
            System.out.println(obj.toString());
        }
        
        return lista;
    }
    
    public List<Magatzem> obtenerAlmacenes() {
        
        List<Magatzem> lista = this.baseDatos.queryByExample(new Magatzem());
        
        System.out.println("--> Almacenes en la Base de Datos:");
        for (Object obj: lista) {
            System.out.println(obj.toString());
        }
        
        return lista;
    }
    
    public List<Producte> obtenerProductos() {
        
        List<Producte> lista = this.baseDatos.queryByExample(new Producte());
        
        System.out.println("--> Productos en la Base de Datos:");
        for (Object obj: lista) {
            System.out.println(obj.toString());
        }
        
        return lista;
    }
    
    public List<UnitatDeMesura> obtenerUnidadesDeMedida() {
        
        List<UnitatDeMesura> lista = this.baseDatos.queryByExample(new UnitatDeMesura());
        
        System.out.println("--> Unidades de Medida en la Base de Datos:");
        for (Object obj: lista) {
            System.out.println(obj.toString());
        }
        
        return lista;
    }
    
    /* ---- Consultas Condicionales ---- */
    public List<Producte> obtenerProductosArticulo(Article art) {
        
        Query consulta = this.baseDatos.query();
        
        consulta.constrain(Producte.class);
        consulta.descend("article").descend("id").constrain(art.getId());
        
        return consulta.execute();
    }
    
    public List<Producte> obtenerProductosArticuloPrecio(String nom, int min, int max) {
        
        Query consulta = this.baseDatos.query();
        
        consulta.constrain(Producte.class);
        consulta.descend("article").descend("id").constrain(nom).startsWith(true);
        consulta.descend("preu").constrain(min).greater();
        consulta.descend("preu").constrain(max).smaller();
        
        return consulta.execute();
    }
    
    public List<Producte> obtenerProductosAlmacenStock(String id, double stock) {
        
        final Magatzem almacen;
        
        ObjectSet<Magatzem> resultado = this.baseDatos.queryByExample(new Magatzem(id));
        
        if (resultado.hasNext()) {
            almacen = resultado.next();
        } else {
            System.out.println("El almacén especificado no existe en la base de datos.");
            return new ArrayList();
        }
        
        ObjectSet<Producte> productos = null;
        
        productos = this.baseDatos.query(new Predicate<Producte>() {
            @Override
            public boolean match(Producte pro) {
                return almacen.getEstoc(pro).getQuantitat() <= stock;
            }
        });
        
        return productos;
    }
    
}
