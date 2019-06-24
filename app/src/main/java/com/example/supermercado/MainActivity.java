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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
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

public class MainActivity extends AppCompatActivity{

    TextView textViewResultado;
    Context context;
    LayoutParams layoutparams;
    TextView textview;
    static TextView textViewNProductos;

    ArrayList<Producto> productosCarrefour = new ArrayList<Producto>();
    ArrayList<Producto> productosAlcampo = new ArrayList<Producto>();
    ArrayList<Producto> productosEroski = new ArrayList<Producto>();
    public static ArrayList<Producto> cesta = new ArrayList<Producto>();
    public static Integer nProductosCesta = 0;

    int nlistasLlenas = 0;
    int nProductosCarrefour = 0;
    int nProductosAlcampo = 0;
    int nProductosEroski = 0;


    public ArrayList<Producto> getCesta() {
        return cesta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Búsqueda manual");

        textViewNProductos = (TextView) findViewById(R.id.textViewNProductos);
        textViewNProductos.setText(nProductosCesta.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                    Intent i = new Intent(MainActivity.this, ListaActivity.class);
                    MainActivity.this.startActivity(i);
                } else if (id == R.id.busqueda_manual) {

                } else if (id == R.id.otro) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        context = getApplicationContext();

        EditText editText = (EditText) findViewById(R.id.editTextBuscar);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    EditText editTextBuscar = (EditText) findViewById(R.id.editTextBuscar);
                    String texto = editTextBuscar.getText().toString();

                    //Ocultar teclado
                    ConstraintLayout mainLayout;
                    mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                    //Progreso
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
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

                    textViewResultado = (TextView) findViewById(R.id.textViewResultado);
                    OkHttpClient client = new OkHttpClient();

                    String urlCarrefour = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt="+texto+"&search=Buscar";
                    String urlAlcampo = "https://www.alcampo.es/compra-online/search/?text="+texto;
                    String urlEroski = "https://supermercado.eroski.es/es/search/results/?q="+texto;

                    Request request1 = new Request.Builder()
                            .url(urlCarrefour)
                            .build();

                    client.newCall(request1).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            textViewResultado.setText(e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                final String respuesta =response.body().string();
                                obtenerListaProductosCarrefour(respuesta);
                                nlistasLlenas++;
                                progressBar.setProgress(nlistasLlenas*3+1);
                                if(nlistasLlenas == 3) {
                                    //final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            crearCartas();
                                            //textViewResultado.setText(texto);
                                        }
                                    });
                                }


                            }
                        }
                    });

                    Request request2 = new Request.Builder()
                            .url(urlAlcampo)
                            .build();

                    client.newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            textViewResultado.setText(e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                final String respuesta2 =response.body().string();
                                obtenerListaProductosAlcampo(respuesta2);
                                nlistasLlenas++;
                                progressBar.setProgress(nlistasLlenas*3+1);
                                if(nlistasLlenas == 3) {
                                    //final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            crearCartas();
                                            //textViewResultado.setText(texto);
                                        }
                                    });
                                }
                            }
                        }
                    });

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
                            if(response.isSuccessful()){
                                final String respuesta3 =response.body().string();
                                obtenerListaProductosEroski(respuesta3);
                                nlistasLlenas++;
                                progressBar.setProgress(nlistasLlenas*3+1);
                                if(nlistasLlenas == 3) {
                                    //final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            crearCartas();
                                            //textViewResultado.setText(texto);
                                        }
                                    });
                                }
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

    //Llena el ArrayList productosCarrefour con 21 productos
    public void obtenerListaProductosCarrefour(String str){
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length()-25000; //TESTEAR EL NUMERO

        for(int i=20000;i<acaba;i++){ // Leer el HTML
            if(str.charAt(i+2)=='l' && str.charAt(i+3)=='d' && str.charAt(i+4)=='-' && str.charAt(i+5)=='o'){
                for(int j=i+10;j<acaba;j++){ // Leer el nombre del producto
                    if(str.charAt(j)=='&'){
                        if(str.charAt(j+1)=='#'){
                            sb.append('\'');
                        }else {
                            sb.append('"');
                        }
                        j=j+5;
                    } else {
                        sb.append(str.charAt(j));
                    }
                    if(str.charAt(j+1)=='<' && str.charAt(j+2)=='/'){
                        if(addProducts(productosCarrefour,new Producto(sb.toString(),"","Carrefour",""))) nProductosCarrefour++;
                        //productosCarrefour.add(new Producto(sb.toString(),"","Carrefour",""));
                        sb.delete(0,sb.length());
                        if(str.charAt(j+75)=='o') {
                            for(int k=j+78;k<acaba;k++){ // Leer el precio
                                if(str.charAt(k)!=' '){
                                    sb.append(str.charAt(k));
                                }else{
                                    productosCarrefour.get(nProductosCarrefour-1).setPrecioString(sb.toString());
                                    sb.delete(0,sb.length());
                                    break;
                                }
                            }
                        }else{
                            boolean encontrado=false;
                            for(int k=j+125;k<acaba;k++){ // Leer el precio rebajado
                                if(str.charAt(k)=='>' || encontrado){
                                    if(!encontrado) {
                                        encontrado = true;
                                        k++;
                                    }
                                    if (str.charAt(k) != ' ') {
                                        sb.append(str.charAt(k));
                                    } else {
                                        productosCarrefour.get(nProductosCarrefour-1).setPrecioString(sb.toString());
                                        sb.delete(0,sb.length());
                                        break;
                                    }
                                }
                            }
                        }
                        sb.delete(0,sb.length());
                        i=j+81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    public void obtenerListaProductosAlcampo(String str){
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length()-300000; //TESTEAR EL NUMERO
        for(int i=3000;i<acaba;i++){ // Leer el HTML
            if(str.charAt(i+4)==':' && str.charAt(i+2)=='e' && str.charAt(i+3)=='\'' && str.charAt(i+1)=='m' && str.charAt(i+5)=='\''){
                for(int j=i+6;j<acaba;j++){ // Leer el nombre del producto
                    if(str.charAt(j)=='&'){ //Caso comillas simples o dobles
                        if(str.charAt(j+4)=='4'){
                            sb.append('"');
                        }else {
                            sb.append('\'');
                        }
                        j=j+5;
                    } else {
                        sb.append(str.charAt(j));
                    }
                    if(str.charAt(j+1)=='\''){
                        if(addProducts(productosAlcampo,new Producto(sb.toString(),"","Alcampo",""))) nProductosAlcampo++;
                        //productosAlcampo.add(new Producto(sb.toString(),"","Alcampo",""));
                        sb.delete(0,sb.length());
                        if(str.charAt(j+50)=='\'') j--; // Calibrado de fallo de distancia al precio
                        for(int k=j+54;k<acaba;k++){ // Leer el precio
                            if(str.charAt(k)!='\''){
                                if(str.charAt(k)=='.') sb.append(","); //Cambiar puntos por comas en precio
                                else sb.append(str.charAt(k));
                            }else{
                                productosAlcampo.get(nProductosAlcampo-1).setPrecioString(sb.toString());
                                sb.delete(0,sb.length());
                                break;
                            }
                        }

                        sb.delete(0,sb.length());
                        i=j+81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    public void obtenerListaProductosEroski(String str){
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length()-20; //TESTEAR EL NUMERO
        for(int i=600000;i<acaba;i++){ // Leer el HTML
            if(str.charAt(i+4)=='\\' && str.charAt(i+2)=='m' && str.charAt(i+3)=='e' && str.charAt(i+1)=='a'){
                for(int j=i+9;j<acaba;j++){ // Leer el nombre del producto
                    sb.append(str.charAt(j));
                    if(str.charAt(j+1)=='\\'){
                        if(addProducts(productosEroski,new Producto(sb.toString(),"","Eroski",""))) nProductosEroski++;
                        //productosEroski.add(new Producto(sb.toString(),"","Eroski",""));
                        sb.delete(0,sb.length());
                        while(str.charAt(j)!='e') j++; // Calibrado de fallo de distancia al precio
                        for(int k=j+6;k<acaba;k++){ // Leer el precio
                            if(str.charAt(k)!='\\'){
                                if(str.charAt(k)=='.') sb.append(","); //Cambiar puntos por comas en precio
                                else sb.append(str.charAt(k));
                            }else{
                                productosEroski.get(nProductosEroski-1).setPrecioString(sb.toString());
                                sb.delete(0,sb.length());
                                break;
                            }
                        }

                        sb.delete(0,sb.length());
                        i=j+81; /////////////////////////////////////////////////////////////////////Intentar aumentar este numero
                        break;
                    }
                }

            }
        }
    }

    private String recorrerListas(){
        String lista = "";
        for (int i = 0; i < 5; i++) {
            if (i < productosCarrefour.size()) lista = lista + productosCarrefour.get(i).getTienda() + ": " + productosCarrefour.get(i).getNombre() + " " + productosCarrefour.get(i).getPrecioString() + "€\n";
            if (i < productosAlcampo.size()) lista = lista + productosAlcampo.get(i).getTienda() + ": " +  productosAlcampo.get(i).getNombre() + " " + productosAlcampo.get(i).getPrecioString() + "€\n";
            if (i < productosEroski.size()) lista = lista + productosEroski.get(i).getTienda() + ": " +  productosEroski.get(i).getNombre() + " " + productosEroski.get(i).getPrecioString() + "€\n";
        }
        return lista;
    }

    private void crearCartas(){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductos);
        ScrollView scroll = (ScrollView)findViewById(R.id.scrollViewProductos);
        layout.removeAllViews();
        scroll.setScrollY(0);
        int nProductos = 0;
        for(int i=0; i<40 && nProductos<20; i=i+3){
            if (nProductos < productosCarrefour.size()){
                CreateCardViewProgrammatically(500*nProductos,"Carrefour");
                nProductos++;
            }
            if (nProductos < productosEroski.size()){
                CreateCardViewProgrammatically(500*nProductos,"Eroski");
                nProductos++;
            }
            if (nProductos < productosAlcampo.size()){
                CreateCardViewProgrammatically(500*nProductos,"Alcampo");
                nProductos++;
            }
        }
    }

    public void CreateCardViewProgrammatically(int pos, String supermarket){
        final Producto producto;
        int index = pos/500;

        if(supermarket.equals("Carrefour")){
            producto = productosCarrefour.get(index);
        }else if(supermarket.equals("Eroski")){
            producto = productosEroski.get(index);
        }else{
            producto = productosAlcampo.get(index);
        }
        String name = producto.getNombre();
        String precio = producto.getPrecioString();

        //CARDVIEW
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductos);
        CardView cardview = new CardView(context);
        layoutparams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(17,11+pos,17,15);
        layoutparams.height = 500;
        cardview.setLayoutParams(layoutparams);
        cardview.setUseCompatPadding(true);

        //IMAGEN LOGO
        Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier(supermarket.toLowerCase()+"logo", "drawable", context.getPackageName()));
        ImageView logoview = new ImageView(context);
        logoview.setImageDrawable(logo);
        logoview.setPadding(0,300,700,0);
        LayoutParams layoutparams2 = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams2.setMargins(30,0,0,58);
        layoutparams2.height = 500;
        logoview.setLayoutParams(layoutparams2);

        //IMAGEN PLACEHOLDER
        Drawable placeholder = context.getResources().getDrawable(context.getResources().getIdentifier("placeholder", "drawable", context.getPackageName()));
        ImageView placeholderview = new ImageView(context);
        placeholderview.setImageDrawable(placeholder);
        placeholderview.setPadding(0,0,700,200);
        LayoutParams layoutparams1 = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams1.setMargins(20,20,0,0);
        layoutparams1.height = 500;
        placeholderview.setLayoutParams(layoutparams1);

        //TITULO PRODUCTO
        textview = new TextView(context);
        textview.setText(name);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        //textview.setTextColor(0x3f95a2);
        textview.setTypeface(null, Typeface.BOLD);
        textview.setPadding(370,25,25,0);
        textview.setMaxLines(5);

        //PRECIO
        TextView precioview = new TextView(context);
        precioview.setText(precio+"€");
        precioview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        precioview.setTextColor(Color.BLACK);
        precioview.setTypeface(null, Typeface.BOLD);
        precioview.setPadding(250,335,230,30);
        precioview.setGravity(Gravity.CENTER);

/*
        //CANTIDAD
        EditText cantidadtext = new EditText(context);

        cantidadtext.setGravity(Gravity.CENTER);
        cantidadtext.setBackgroundColor(Color.rgb(229, 229, 229));
        cantidadtext.setTextSize(15);
        //cantidadtext.setTypeface(null, Typeface.BOLD);
        LayoutParams layoutparams4 = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams4.width = 90;
        layoutparams4.height = 90;
        layoutparams4.setMargins(805,250,25,25);
        cantidadtext.setLayoutParams(layoutparams4);
        cantidadtext.setHeight(0);
        cantidadtext.setPadding(0,0,0,0);
        cantidadtext.setText("1");
        cantidadtext.bringToFront();
*/

        //BOTON
        Button boton = new Button(context);
        LayoutParams layoutparams3 = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams3.height = 90;
        layoutparams3.width = 300;
        layoutparams3.setMargins(700,350,30,25);
        boton.setLayoutParams(layoutparams3);
        boton.setText("Añadir");
        boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        boton.setTextColor(Color.WHITE);
        boton.setTypeface(null, Typeface.BOLD);
        boton.setBackgroundColor(Color.rgb(63, 149, 162));
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(addProducts(cesta,producto)) textViewNProductos.setText((++nProductosCesta).toString());
            }
        });

        //ADD DE LOS VIEWS
        cardview.addView(textview);
        cardview.addView(precioview);
        cardview.addView(logoview);
        //cardview.addView(cantidadtext);
        cardview.addView(boton);
        cardview.addView(placeholderview);
        layout.addView(cardview);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent myIntent = new Intent(MainActivity.this, CartActivity.class);
            MainActivity.this.startActivity(myIntent);
        }/*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public boolean addProducts(ArrayList<Producto> list, Producto product){
        for(int i=0; i<list.size();i++){
            if(list.get(i).getNombre().equals(product.getNombre()) && list.get(i).getPrecioString().equals(product.getPrecioString())){
                list.get(i).setCantidad(list.get(i).getCantidad()+1);
                        return false;
            }
        }
        list.add(product);
        return true;
    }

}
