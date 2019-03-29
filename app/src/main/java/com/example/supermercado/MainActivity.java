package com.example.supermercado;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
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

    ArrayList<Producto> productos = new ArrayList<Producto>();
    int nProductos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        //relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout1);
        //constraintLayout = (ConstraintLayout)findViewById(R.id.layout1);
        Button buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextBuscar = (EditText) findViewById(R.id.editTextBuscar);
                String texto = editTextBuscar.getText().toString();


                textViewResultado = (TextView) findViewById(R.id.textViewResultado);
                OkHttpClient client = new OkHttpClient();

                String url = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt="+texto+"&search=Buscar";
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            final String respuesta =response.body().string();
                            String texto="";
                            texto = obtenerListaProductos(respuesta);
                            final String tex = texto;
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewResultado.setText(tex);
                                }
                            });
                        }
                    }
                });


            }
        });
    }

    public String obtenerListaProductos(String str){
        String lista = "";
        StringBuilder sb = new StringBuilder();
        int acaba = str.length()-25000; //TESTEAR EL NUMERO

        for(int i=30000;i<acaba;i++){ // Leer el HTML
            if(str.charAt(i+2)=='l' && str.charAt(i+3)=='d' && str.charAt(i+4)=='-' && str.charAt(i+5)=='o'){
                for(int j=i+10;j<acaba;j++){ // Leer el nombre del producto
                    if(str.charAt(j)=='&'){
                        sb.append('"');
                        j=j+5;
                    } else {
                        sb.append(str.charAt(j));
                    }
                    if(str.charAt(j+1)=='<' && str.charAt(j+2)=='/'){
                        productos.add(new Producto(sb.toString(),"",""));
                        nProductos++;
                        sb.delete(0,sb.length());
                        if(str.charAt(j+75)=='o') {
                            for(int k=j+78;k<acaba;k++){ // Leer el precio
                                if(str.charAt(k)!=' '){
                                    sb.append(str.charAt(k));
                                }else{
                                    productos.get(nProductos-1).setPrecioString(sb.toString());
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
                                        productos.get(nProductos-1).setPrecioString(sb.toString());
                                        sb.delete(0,sb.length());
                                        break;
                                    }
                                }
                            }
                        }
                        sb.delete(0,sb.length());
                        i=j+81;
                        break;
                    }
                }

            }
        }

        //ESTE BLOQUE SERA ELIMINADO CUANDO SE AVANCE
        for(int i=0; i<productos.size();i++){
            lista=lista+productos.get(i).getNombre()+" "+productos.get(i).getPrecioString()+"€\n";
        }
        productos.clear();
        nProductos=0;
        return lista;
        /////////////////////////////////////////////
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
