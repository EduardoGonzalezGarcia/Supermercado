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
        for(int i=40;i<str.length()-4;i++){
            if(str.charAt(i)=='s' && str.charAt(i+1)=='o' && str.charAt(i+2)=='l' && str.charAt(i+3)=='d'){
                for(int j=i+10;j<str.length()-3;j++){
                    if(str.charAt(j)=='<' && str.charAt(j+1)=='/'){
                        lista=lista.concat(str.substring(i+10,j)+"\n");
                        if(str.charAt(j+74)=='o') {
                            lista = lista.concat(str.substring(j + 77, j + 81) + "€\n\n");
                        }else{
                            lista = lista.concat(str.substring(j + 130, j + 134) + "€\n\n");
                        }
                        i=j+81;
                        break;
                    }
                }

            }
        }
        return lista;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
