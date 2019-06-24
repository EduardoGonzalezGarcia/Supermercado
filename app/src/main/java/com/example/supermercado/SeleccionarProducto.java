package com.example.supermercado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SeleccionarProducto extends AppCompatActivity {

    TextView textViewResultado;
    Context context;
    LinearLayout.LayoutParams layoutparams;
    TextView textview;

    ArrayList<Producto> productosCarrefour = new ArrayList<Producto>();
    ArrayList<Producto> productosAlcampo = new ArrayList<Producto>();
    ArrayList<Producto> productosEroski = new ArrayList<Producto>();
    public static ArrayList<Producto> cesta = new ArrayList<Producto>();

    int nlistasLlenas = 0;
    int nProductosCarrefour = 0;
    int nProductosAlcampo = 0;
    int nProductosEroski = 0;

    public static String name;
    public static String searchAndBrand;


    public ArrayList<Producto> getCesta() {
        return cesta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_producto);
        setTitle("Seleccionar producto");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.busqueda_listas) {
                    Intent i = new Intent(SeleccionarProducto.this, ListaActivity.class);
                    startActivity(i);
                } else if (id == R.id.busqueda_manual) {
                    Intent i = new Intent(SeleccionarProducto.this, MainActivity.class);
                    SeleccionarProducto.this.startActivity(i);
                } else if (id == R.id.otro) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        context = getApplicationContext();

        EditText editText = (EditText) findViewById(R.id.editTextBuscar2);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    EditText editTextBuscar2 = (EditText) findViewById(R.id.editTextBuscar2);
                    String texto = editTextBuscar2.getText().toString();
                    searchAndBrand = texto;

                    //Ocultar teclado
                    ConstraintLayout seleccionarLayout;
                    seleccionarLayout = (ConstraintLayout) findViewById(R.id.seleccionarLayout);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(seleccionarLayout.getWindowToken(), 0);

                    //Progreso
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                    progressBar.setMax(10);
                    progressBar.setProgress(0);
                    progressBar.bringToFront();
                    progressBar.setVisibility(View.VISIBLE);

                    productosCarrefour.clear();
                    productosAlcampo.clear();
                    productosEroski.clear();
                    nProductosCarrefour = 0;
                    nProductosAlcampo = 0;
                    nProductosEroski = 0;
                    nlistasLlenas = 0;

                    textViewResultado = (TextView) findViewById(R.id.textViewResultado2);
                    OkHttpClient client = new OkHttpClient();

                    String urlEroski = "https://supermercado.eroski.es/es/search/results/?q=" + texto;

                    Request request3 = new Request.Builder()
                            .url(urlEroski)
                            .build();

                    client.newCall(request3).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            textViewResultado.setText(e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            progressBar.setProgress(4);
                            if (response.isSuccessful()) {
                                final String respuesta = response.body().string();
                                progressBar.setProgress(7);
                                obtenerListaProductosEroski(respuesta);
                                progressBar.setProgress(9);
                                SeleccionarProducto.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        crearCartas();
                                    }
                                });

                            }
                        }
                    });
                    progressBar.setProgress(1);
                    return true;
                }
                return false;
            }
        });
    }

    public ArrayList<String> deleteDuplicateTokens(String productName) {
        ArrayList<String> CorrectTokens = new ArrayList<>();
        String words[] = productName.split(" ");

        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (words[i].toLowerCase().equals(words[j].toLowerCase()) && i != j) {
                    words[j] = "0";
                }
            }
            if (words[i] != "0")
                CorrectTokens.add(words[i]);
        }
        return CorrectTokens;
    }

    //Llena el ArrayList productosCarrefour con 21 productos
    public void obtenerListaProductosCarrefour(String str) {
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length() - 25000; //TESTEAR EL NUMERO

        for (int i = 20000; i < acaba; i++) { // Leer el HTML
            if (str.charAt(i + 2) == 'l' && str.charAt(i + 3) == 'd' && str.charAt(i + 4) == '-' && str.charAt(i + 5) == 'o') {
                for (int j = i + 10; j < acaba; j++) { // Leer el nombre del producto
                    if (str.charAt(j) == '&') {
                        if (str.charAt(j + 1) == '#') {
                            sb.append('\'');
                        } else {
                            sb.append('"');
                        }
                        j = j + 5;
                    } else {
                        sb.append(str.charAt(j));
                    }
                    if (str.charAt(j + 1) == '<' && str.charAt(j + 2) == '/') {
                        if (addProducts(productosCarrefour, new Producto(sb.toString(), "", "Carrefour", "")))
                            nProductosCarrefour++;
                        //productosCarrefour.add(new Producto(sb.toString(),"","Carrefour",""));
                        sb.delete(0, sb.length());
                        if (str.charAt(j + 75) == 'o') {
                            for (int k = j + 78; k < acaba; k++) { // Leer el precio
                                if (str.charAt(k) != ' ') {
                                    sb.append(str.charAt(k));
                                } else {
                                    productosCarrefour.get(nProductosCarrefour - 1).setPrecioString(sb.toString());
                                    sb.delete(0, sb.length());
                                    break;
                                }
                            }
                        } else {
                            boolean encontrado = false;
                            for (int k = j + 125; k < acaba; k++) { // Leer el precio rebajado
                                if (str.charAt(k) == '>' || encontrado) {
                                    if (!encontrado) {
                                        encontrado = true;
                                        k++;
                                    }
                                    if (str.charAt(k) != ' ') {
                                        sb.append(str.charAt(k));
                                    } else {
                                        productosCarrefour.get(nProductosCarrefour - 1).setPrecioString(sb.toString());
                                        sb.delete(0, sb.length());
                                        break;
                                    }
                                }
                            }
                        }
                        sb.delete(0, sb.length());
                        i = j + 81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    public void obtenerListaProductosAlcampo(String str) {
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length() - 300000; //TESTEAR EL NUMERO
        for (int i = 3000; i < acaba; i++) { // Leer el HTML
            if (str.charAt(i + 4) == ':' && str.charAt(i + 2) == 'e' && str.charAt(i + 3) == '\'' && str.charAt(i + 1) == 'm' && str.charAt(i + 5) == '\'') {
                for (int j = i + 6; j < acaba; j++) { // Leer el nombre del producto
                    if (str.charAt(j) == '&') { //Caso comillas simples o dobles
                        if (str.charAt(j + 4) == '4') {
                            sb.append('"');
                        } else {
                            sb.append('\'');
                        }
                        j = j + 5;
                    } else {
                        sb.append(str.charAt(j));
                    }
                    if (str.charAt(j + 1) == '\'') {
                        if (addProducts(productosAlcampo, new Producto(sb.toString(), "", "Alcampo", "")))
                            nProductosAlcampo++;
                        //productosAlcampo.add(new Producto(sb.toString(),"","Alcampo",""));
                        sb.delete(0, sb.length());
                        if (str.charAt(j + 50) == '\'')
                            j--; // Calibrado de fallo de distancia al precio
                        for (int k = j + 54; k < acaba; k++) { // Leer el precio
                            if (str.charAt(k) != '\'') {
                                if (str.charAt(k) == '.')
                                    sb.append(","); //Cambiar puntos por comas en precio
                                else sb.append(str.charAt(k));
                            } else {
                                productosAlcampo.get(nProductosAlcampo - 1).setPrecioString(sb.toString());
                                sb.delete(0, sb.length());
                                break;
                            }
                        }

                        sb.delete(0, sb.length());
                        i = j + 81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    public void obtenerListaProductosEroski(String str) {
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length() - 20; //TESTEAR EL NUMERO
        for (int i = 600000; i < acaba; i++) { // Leer el HTML
            if (str.charAt(i + 4) == '\\' && str.charAt(i + 2) == 'm' && str.charAt(i + 3) == 'e' && str.charAt(i + 1) == 'a') {
                for (int j = i + 9; j < acaba; j++) { // Leer el nombre del producto
                    sb.append(str.charAt(j));
                    if (str.charAt(j) == 'E' && str.charAt(j + 1) == 'R' && str.charAt(j + 2) == 'O' && str.charAt(j + 3) == 'S' && str.charAt(j + 4) == 'K') {
                        sb.delete(0, sb.length());
                        break;
                    }
                    if (str.charAt(j + 1) == '\\') {
                        if (addProducts(productosEroski, new Producto(sb.toString(), "", "Eroski", "")))
                            nProductosEroski++;
                        sb.delete(0, sb.length());
                        while (str.charAt(j) != 'e')
                            j++; // Calibrado de fallo de distancia al precio
                        for (int k = j + 6; k < acaba; k++) { // Leer el precio
                            if (str.charAt(k) != '\\') {
                                if (str.charAt(k) == '.')
                                    sb.append(","); //Cambiar puntos por comas en precio
                                else sb.append(str.charAt(k));
                            } else {
                                productosEroski.get(nProductosEroski - 1).setPrecioString(sb.toString());
                                sb.delete(0, sb.length());
                                break;
                            }
                        }

                        sb.delete(0, sb.length());
                        i = j + 81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    private String recorrerListas() {
        String lista = "";
        for (int i = 0; i < 5; i++) {
            if (i < productosCarrefour.size())
                lista = lista + productosCarrefour.get(i).getTienda() + ": " + productosCarrefour.get(i).getNombre() + " " + productosCarrefour.get(i).getPrecioString() + "€\n";
            if (i < productosAlcampo.size())
                lista = lista + productosAlcampo.get(i).getTienda() + ": " + productosAlcampo.get(i).getNombre() + " " + productosAlcampo.get(i).getPrecioString() + "€\n";
            if (i < productosEroski.size())
                lista = lista + productosEroski.get(i).getTienda() + ": " + productosEroski.get(i).getNombre() + " " + productosEroski.get(i).getPrecioString() + "€\n";
        }
        return lista;
    }

    private void crearCartas() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutProductos2);
        ScrollView scroll = (ScrollView) findViewById(R.id.scrollViewProductos2);
        layout.removeAllViews();
        scroll.setScrollY(0);
        int nProductos = 0;
        for (int i = 0; i < 8 && nProductos < 8; i++) {/*
            if (nProductos < productosCarrefour.size()){
                CreateCardViewProgrammatically(360*nProductos,"Carrefour");
                nProductos++;
            }*/
            if (nProductos < productosEroski.size()) {
                CreateCardViewProgrammatically(360 * nProductos, "Eroski");
                nProductos++;
            }/*
            if (nProductos < productosAlcampo.size()){
                CreateCardViewProgrammatically(360*nProductos,"Alcampo");
                nProductos++;
            }*/
        }
    }

    public void CreateCardViewProgrammatically(int pos, String supermarket) {
        final Producto producto;
        int index = pos / 360;

        if (supermarket.equals("Carrefour")) {
            producto = productosCarrefour.get(index);
        } else if (supermarket.equals("Eroski")) {
            producto = productosEroski.get(index);
        } else {
            producto = productosAlcampo.get(index);
        }
        final String name = producto.getNombre();
        String precio = producto.getPrecioString();

        //CARDVIEW
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutProductos2);
        CardView cardview = new CardView(context);
        layoutparams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(17, 11 + pos, 17, 15);
        layoutparams.height = 360;
        cardview.setLayoutParams(layoutparams);
        cardview.setUseCompatPadding(true);

        //IMAGEN PLACEHOLDER
        Drawable placeholder = context.getResources().getDrawable(context.getResources().getIdentifier("placeholder", "drawable", context.getPackageName()));
        ImageView placeholderview = new ImageView(context);
        placeholderview.setImageDrawable(placeholder);
        placeholderview.setPadding(0, 0, 700, 200);
        LinearLayout.LayoutParams layoutparams1 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams1.setMargins(20, 20, 0, 20);
        layoutparams1.height = 500;
        placeholderview.setLayoutParams(layoutparams1);

        //TITULO PRODUCTO
        textview = new TextView(context);
        textview.setText(name);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        //textview.setTextColor(0x3f95a2);
        textview.setTypeface(null, Typeface.BOLD);
        textview.setPadding(370, 25, 25, 0);
        textview.setMaxLines(3);

        //BOTON
        Button boton = new Button(context);
        LinearLayout.LayoutParams layoutparams3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams3.height = 90;
        layoutparams3.width = 300;
        layoutparams3.setMargins(710, 230, 0, 25);
        boton.setLayoutParams(layoutparams3);
        boton.setText("Seleccionar");
        boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        boton.setTextColor(Color.WHITE);
        boton.setTypeface(null, Typeface.BOLD);
        boton.setBackgroundColor(Color.rgb(63, 149, 162));
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListaActivity.nProductosLista++;
                SeleccionarProducto.name = name;
                final String search = searchAndBrand;
                ArrayList<String> aux = deleteDuplicateTokens(searchAndBrand + " " + name);
                for (int i = 0; i < aux.size(); i++) {
                    String token = aux.get(i).replace(",", "");
                    token = token.replaceAll("[0-9]", "");
                    if (token.equals(token.toUpperCase())) {
                        searchAndBrand += " ";
                        searchAndBrand += token.toLowerCase();
                    }
                }
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        Producto[] vectorProductos = new Producto[3];
                        vectorProductos[0] = producto;

                        String urlCarrefour = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt=" + searchAndBrand + "&search=Buscar";
                        String urlAlcampo = "https://www.alcampo.es/compra-online/search/?text=" + searchAndBrand;

                        final ArrayList<String> productoElegidoEditado = deleteDuplicateTokens(name);
                        for (int i = 0; i < productoElegidoEditado.size(); i++) {
                            productoElegidoEditado.set(i, productoElegidoEditado.get(i).replace(",", ""));
                            productoElegidoEditado.set(i, productoElegidoEditado.get(i).replace(".", ""));
                        }

                        findSameProductOnCarrefour(client, vectorProductos, urlCarrefour, productoElegidoEditado);
                        if(productosCarrefour.size()==0){
                            urlCarrefour = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt=" + search + "&search=Buscar";
                            findSameProductOnCarrefour(client, vectorProductos, urlCarrefour, productoElegidoEditado);
                        }

                        findSameProductOnAlcampo(client, vectorProductos, urlAlcampo, productoElegidoEditado);
                        if(productosAlcampo.size()==0){
                            urlAlcampo = "https://www.alcampo.es/compra-online/search/?text=" + search;
                            findSameProductOnAlcampo(client, vectorProductos, urlAlcampo, productoElegidoEditado);
                        }

                        ListaActivity.ProductosFinales.add(vectorProductos);

                    }
                };

                thread.start();

                //ELIMINAR THREAD (NO SE USA)
                /*Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                Iterator<Thread> it = threadSet.iterator();
                while(it.hasNext()){
                    Thread t = it.next();
                    if(t.getId()==thread.getId()){
                        thread.interrupt();
                    }
                }*/

                Intent i = new Intent(SeleccionarProducto.this, ListaActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                SeleccionarProducto.this.startActivity(i);
            }
        });

        //ADD DE LOS VIEWS
        cardview.addView(textview);
        //cardview.addView(precioview);
        //cardview.addView(logoview);
        //cardview.addView(cantidadtext);
        cardview.addView(boton);
        cardview.addView(placeholderview);
        layout.addView(cardview);
    }

    private void findSameProductOnAlcampo(OkHttpClient client, Producto[] vectorProductos, String urlAlcampo, ArrayList<String> productoElegidoEditado) {
        Request request2 = new Request.Builder()
                .url(urlAlcampo)
                .build();

        try {
            Response response = client.newCall(request2).execute();
            if (response.isSuccessful()) {
                final String respuesta2 = response.body().string();
                obtenerListaProductosAlcampo(respuesta2);
                ArrayList<String>[] productosEditados = new ArrayList[productosAlcampo.size()];
                int[] gradosSemejanza = new int[productosAlcampo.size()];

                if(productosAlcampo.size()==0) return;

                for (int i = 0; i < productosAlcampo.size(); i++) {
                    if (i < productosAlcampo.size()) {
                        productosEditados[i] = deleteDuplicateTokens(productosAlcampo.get(i).getNombre());
                        for (int j = 0; j < productosEditados[i].size(); j++) {
                            productosEditados[i].set(j, productosEditados[i].get(j).replace(",", ""));
                            productosEditados[i].set(j, productosEditados[i].get(j).replace(".", ""));
                            for (int k = 0; k < productoElegidoEditado.size(); k++) {
                                if (productoElegidoEditado.get(k).equals(productosEditados[i].get(j)) && productoElegidoEditado.get(k).length() > 2) {
                                    gradosSemejanza[i]++;
                                    if (productoElegidoEditado.get(k).matches("-?\\d+(\\.\\d+)?")) {
                                        gradosSemejanza[i]+=2;
                                    }
                                    k = 100;
                                }
                            }
                        }
                    }
                }
                ArrayList<Integer> bestProductsIndex = new ArrayList<Integer>();
                bestProductsIndex.add(0);
                for (int i = 0; i < gradosSemejanza.length; i++) {
                    if (gradosSemejanza[i] == gradosSemejanza[bestProductsIndex.get(0)]) {
                        bestProductsIndex.add(i);
                    }
                    if (gradosSemejanza[i] > gradosSemejanza[bestProductsIndex.get(0)]) {
                        bestProductsIndex.clear();
                        bestProductsIndex.add(i);
                    }
                }
                Producto mismoProd = new Producto();
                double mejorPrecio = 10000;
                for (int i = 0; i < bestProductsIndex.size(); i++) {
                    String precioString = productosAlcampo.get(bestProductsIndex.get(i)).getPrecioString();
                    precioString = precioString.replace(",", ".");
                    if (Double.parseDouble(precioString) < mejorPrecio) {
                        mejorPrecio = Double.parseDouble(precioString);
                        mismoProd = productosAlcampo.get(bestProductsIndex.get(i));
                    }
                }
                vectorProductos[2] = mismoProd;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findSameProductOnCarrefour(OkHttpClient client, Producto[] vectorProductos, String urlCarrefour, ArrayList<String> productoElegidoEditado) {
        Request request1 = new Request.Builder()
                .url(urlCarrefour)
                .build();
        try {
            Response response = client.newCall(request1).execute();
            if (response.isSuccessful()) {
                final String respuesta = response.body().string();
                obtenerListaProductosCarrefour(respuesta);
                ArrayList<String>[] productosEditados = new ArrayList[productosCarrefour.size()];
                int[] gradosSemejanza = new int[productosCarrefour.size()];

                if(productosCarrefour.size()==0) return;

                for (int i = 0; i < 21; i++) {
                    if (i < productosCarrefour.size()) {
                        productosEditados[i] = deleteDuplicateTokens(productosCarrefour.get(i).getNombre());
                        for (int j = 0; j < productosEditados[i].size(); j++) {
                            productosEditados[i].set(j, productosEditados[i].get(j).replace(",", ""));
                            productosEditados[i].set(j, productosEditados[i].get(j).replace(".", ""));
                            for (int k = 0; k < productoElegidoEditado.size(); k++) {
                                if (productoElegidoEditado.get(k).equals(productosEditados[i].get(j)) && productoElegidoEditado.get(k).length() > 2) {
                                    gradosSemejanza[i]++;
                                    if (productoElegidoEditado.get(k).matches("-?\\d+(\\.\\d+)?")) {
                                        gradosSemejanza[i]+=2;
                                    }
                                    k = 100;
                                }
                            }
                        }
                    }
                }
                ArrayList<Integer> bestProductsIndex = new ArrayList<Integer>();
                bestProductsIndex.add(0);
                for (int i = 0; i < gradosSemejanza.length; i++) {
                    if (gradosSemejanza[i] == gradosSemejanza[bestProductsIndex.get(0)]) {
                        bestProductsIndex.add(i);
                    }
                    if (gradosSemejanza[i] > gradosSemejanza[bestProductsIndex.get(0)]) {
                        bestProductsIndex.clear();
                        bestProductsIndex.add(i);
                    }
                }
                Producto mismoProd = new Producto();
                double mejorPrecio = 10000;
                for (int i = 0; i < bestProductsIndex.size(); i++) {
                    String precioString = productosCarrefour.get(bestProductsIndex.get(i)).getPrecioString();
                    precioString = precioString.replace(",", ".");
                    if (Double.parseDouble(precioString) < mejorPrecio) {
                        mejorPrecio = Double.parseDouble(precioString);
                        mismoProd = productosCarrefour.get(bestProductsIndex.get(i));
                    }
                }
                vectorProductos[1] = mismoProd;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent myIntent = new Intent(SeleccionarProducto.this, CartActivity.class);
            SeleccionarProducto.this.startActivity(myIntent);
        }/*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public boolean addProducts(ArrayList<Producto> list, Producto product) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNombre().equals(product.getNombre()) && list.get(i).getPrecioString().equals(product.getPrecioString())) {
                list.get(i).setCantidad(list.get(i).getCantidad() + 1);
                return false;
            }
        }
        list.add(product);
        return true;
    }

}
