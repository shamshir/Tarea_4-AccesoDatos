package gestionpersistenciadb4o;

import clasesIoc.Article;
import clasesIoc.Envas;
import clasesIoc.Magatzem;
import clasesIoc.Producte;
import clasesIoc.ProducteAGranel;
import clasesIoc.ProducteEnvasat;
import clasesIoc.UnitatDeMesura;
import java.util.List;

public class GestionPersistenciaDB4O {

    public static void main(String[] args) {

        // Abrimos Conexión con la Base de Datos
        GestorProductos g = new GestorProductos();

//        // Añadimos Elementos a la Base de Datos
//        instanciar(g);

//        // Modificamos Elementos de la Base de Datos
//        Article a = g.obtenerObjeto(new Article("5"));
//        incrementarPrecio(g, a);

//        Magatzem alm = g.obtenerObjeto(new Magatzem("1"));
//        List<Producte> lista = g.obtenerProductosArticulo(new Article("3"));
//        aumentoStock(g, alm, lista, 3.0);

//        Magatzem alm = g.obtenerObjeto(new Magatzem("1"));
//        List<Producte> lista = g.obtenerProductosArticulo(new Article("4"));
//        reduccionStock(g, alm, lista, 2.0);
  
//        // Consultamos la Base de Datos (Productos con Stock menor a 10)
//        Magatzem alm = g.obtenerObjeto(new Magatzem("1"));
//        mostrarProductosStockMenor10(g, alm, 9);

//        // Pruebas Extra
//        g.guardarElemento(new Magatzem("2", "MegaAlmacén"));
//        g.obtenerAlmacenes();

//        Magatzem almacenAEliminar = g.obtenerObjeto(new Magatzem("2"));
//        g.eliminarElemento(almacenAEliminar);

//        eliminarBaseDatos(g);
        
        // Cerramos conexión con la Base de Datos
        g.cerrarConexion();

    }

    public static void instanciar(GestorProductos g) {

        // Añadir Artículos
        Article art1 = new Article("1", "Naranjas");
        Article art2 = new Article("2", "Aceite");
        Article art3 = new Article("3", "Agua");
        Article art4 = new Article("4", "Limones");
        Article art5 = new Article("5", "Donuts");
        Article art6 = new Article("6", "Lentejas");

        g.obtenerObjeto(art1);
        g.obtenerObjeto(art2);
        g.obtenerObjeto(art3);
        g.obtenerObjeto(art4);
        g.obtenerObjeto(art5);
        g.obtenerObjeto(art6);

        // Añadir Unidades de Medida
        UnitatDeMesura udm1 = new UnitatDeMesura("l", "Litros");
        UnitatDeMesura udm2 = new UnitatDeMesura("kg", "Kilos");
        UnitatDeMesura udm3 = new UnitatDeMesura("oz", "Onzas");

        g.obtenerObjeto(udm1);
        g.obtenerObjeto(udm2);
        g.obtenerObjeto(udm3);

        // Añadir Envases
        Envas env1 = new Envas("Botella", 2.0, udm1);
        Envas env2 = new Envas("Bolsa", 1.0, udm2);
        Envas env3 = new Envas("Paquete", 0.5, udm2);

        g.obtenerObjeto(env1);
        g.obtenerObjeto(env2);
        g.obtenerObjeto(env3);

        // Añadir Productos
        Producte pro1 = new Producte(art1, "Naranjo S.L.", 2.0);
        ProducteEnvasat pro2 = new ProducteEnvasat(art1, "NoSoloNaranjas", 4.0, env2);
        ProducteAGranel pro3 = new ProducteAGranel(art2, 5.0, udm3);
        ProducteEnvasat pro4 = new ProducteEnvasat(art5, "DonutsOriginales", 2.0, env3);
        ProducteAGranel pro5 = new ProducteAGranel(art4, 2.5, udm2);
        ProducteEnvasat pro6 = new ProducteEnvasat(art5, "Hacendado", 1.0, env3);
        ProducteAGranel pro7 = new ProducteAGranel(art6, 0.5, udm2);
        ProducteEnvasat pro8 = new ProducteEnvasat(art3, "SolánDeCabras", 3.5, env1);
        ProducteAGranel pro9 = new ProducteAGranel(art4, 1.0, udm2);
        ProducteEnvasat pro10 = new ProducteEnvasat(art3, "FontVella", 2.0, env1);

        g.obtenerObjeto(pro1);
        g.obtenerObjeto(pro2);
        g.obtenerObjeto(pro3);
        g.obtenerObjeto(pro4);
        g.obtenerObjeto(pro5);
        g.obtenerObjeto(pro6);
        g.obtenerObjeto(pro7);
        g.obtenerObjeto(pro8);
        g.obtenerObjeto(pro9);
        g.obtenerObjeto(pro10);

        // Añadir Almacén y asignar Stock
        Magatzem alm = new Magatzem("1", "SuperAlmacén");
        
        alm.assignarEstoc(pro1, 20.0);
        alm.assignarEstoc(pro2, 8.0);
        alm.assignarEstoc(pro3, 10.0);
        alm.assignarEstoc(pro4, 12.0);
        alm.assignarEstoc(pro5, 6.0);
        alm.assignarEstoc(pro6, 16.0);
        alm.assignarEstoc(pro7, 8.0);
        alm.assignarEstoc(pro8, 5.0);
        alm.assignarEstoc(pro9, 16.0);
        alm.assignarEstoc(pro10, 9.0);

        g.obtenerObjeto(alm);
    }

    public static void incrementarPrecio(GestorProductos g, Article a) {

        List<Producte> listaProductos = g.obtenerProductosArticulo(a);
        
        for (Producte pro: listaProductos) {
            System.out.println(pro.toString());
            pro.setPreu(pro.getPreu()*1.05);
            System.out.println(pro.toString());
        }
        
        g.guardarCambios();
    }

    public static void aumentoStock(GestorProductos g, Magatzem alm,
            List<Producte> lista, double unidades) {
        
        for (Producte pro: lista) {
            System.out.println("Antes --> " + alm.getEstoc(pro).toString());
            alm.incrementarEstocProducte(pro, unidades);
            System.out.println("Después --> " + alm.getEstoc(pro).toString());
        }
        
        g.guardarCambios();
    }

    public static void reduccionStock(GestorProductos g, Magatzem alm,
            List<Producte> lista, double unidades) {

        for (Producte pro: lista) {
            System.out.println("Antes --> " + alm.getEstoc(pro).toString());
            alm.decrementarEstocProducte(pro, unidades);
            System.out.println("Después --> " + alm.getEstoc(pro).toString());
        }
        
        g.guardarCambios();
    }

    public static void mostrarProductosStockMenor10(GestorProductos g,
            Magatzem alm, int maxStock) {

        List<Producte> lista = g.obtenerProductosAlmacenStock(alm.getId(), maxStock);
        
        for (Producte pro: lista) {
            System.out.println(alm.getEstoc(pro).toString());
        }
    }
    
    private static void imprimirProductos(List<Producte> lista) {
        
        System.out.println("Productos Recuperados:");
        
        for (Producte pro: lista) {
            System.out.println(pro.toString());
        }
    }
    
    private static void eliminarBaseDatos(GestorProductos g) {
        List<Magatzem> lista5 = g.obtenerAlmacenes();
        for (Object obj: lista5) {
            g.eliminarElemento(obj);
        }
        List<Producte> lista4 = g.obtenerProductos();
        for (Object obj: lista4) {
            g.eliminarElemento(obj);
        }
        List<Envas> lista3 = g.obtenerEnvases();
        for (Object obj: lista3) {
            g.eliminarElemento(obj);
        }
        List<UnitatDeMesura> lista2 = g.obtenerUnidadesDeMedida();
        for (Object obj: lista2) {
            g.eliminarElemento(obj);
        }
        List<Article> lista = g.obtenerArticulos();
        for (Object obj: lista) {
            g.eliminarElemento(obj);
        }
    }

}
