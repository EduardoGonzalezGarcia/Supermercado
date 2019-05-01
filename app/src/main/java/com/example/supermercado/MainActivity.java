package com.example.supermercado;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
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

    ArrayList<Producto> productosCarrefour = new ArrayList<Producto>();
    ArrayList<Producto> productosAlcampo = new ArrayList<Producto>();
    ArrayList<Producto> productosEroski = new ArrayList<Producto>();
    int nlistasLlenas = 0;
    int nProductos = 0;
    int nProductosAlcampo = 0;
    int nProductosEroski = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    progressBar.setVisibility(View.VISIBLE);


                    productosCarrefour.clear();
                    productosAlcampo.clear();
                    productosEroski.clear();
                    nProductos = 0;
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
                                    final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            textViewResultado.setText(texto);
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
                                    final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            textViewResultado.setText(texto);
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
                                    final String texto = recorrerListas();
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            textViewResultado.setText(texto);
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
                        productosCarrefour.add(new Producto(sb.toString(),"","Carrefour",""));
                        nProductos++;
                        sb.delete(0,sb.length());
                        if(str.charAt(j+75)=='o') {
                            for(int k=j+78;k<acaba;k++){ // Leer el precio
                                if(str.charAt(k)!=' '){
                                    sb.append(str.charAt(k));
                                }else{
                                    productosCarrefour.get(nProductos-1).setPrecioString(sb.toString());
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
                                        productosCarrefour.get(nProductos-1).setPrecioString(sb.toString());
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
        int acaba = str.length()-1300000; //TESTEAR EL NUMERO
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
                        productosAlcampo.add(new Producto(sb.toString(),"","Alcampo",""));
                        nProductosAlcampo++;
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
        //productosEroski.add(new Producto("PRODUCTOEROSKI1","1,30","EroskiFake",""));

        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length()-20; //TESTEAR EL NUMERO
        for(int i=600000;i<acaba;i++){ // Leer el HTML
            if(str.charAt(i+4)=='\\' && str.charAt(i+2)=='m' && str.charAt(i+3)=='e' && str.charAt(i+1)=='a'){
                for(int j=i+9;j<acaba;j++){ // Leer el nombre del producto
                    sb.append(str.charAt(j));
                    if(str.charAt(j+1)=='\\'){
                        productosEroski.add(new Producto(sb.toString(),"","Eroski",""));
                        nProductosEroski++;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
