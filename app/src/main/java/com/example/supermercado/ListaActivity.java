package com.example.supermercado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    Context context;
    public static Integer nProductosLista;
    public static Integer nProductosListaMostrados;
    public static ArrayList<Button> botonesList;
    public static ArrayList<TextView> titulosList;
    public static ArrayList<Producto[]> ProductosFinales;
    String name;
    public static double totalPrecioEroski;
    public static double totalPrecioCarrefour;
    public static double totalPrecioAlcampo;
    public static double precioMasBarato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Búsqueda por listas");
        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(nProductosLista == null){
            botonesList = new ArrayList<Button>();
            titulosList = new ArrayList<TextView>();
            ProductosFinales = new ArrayList<Producto[]>();
            nProductosListaMostrados=0;
            nProductosLista=0;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(nProductosLista > nProductosListaMostrados) anadirLista();
        refreshList();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.busqueda_listas) {

                } else if (id == R.id.busqueda_manual) {
                    Intent i = new Intent(ListaActivity.this, MainActivity.class);
                    ListaActivity.this.startActivity(i);
                } else if (id == R.id.otro) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Button button= (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrecioEroski=0;
                totalPrecioCarrefour=0;
                totalPrecioAlcampo=0;
                precioMasBarato=0;
                for(int i=0;i<ProductosFinales.size();i++){
                    totalPrecioEroski += Double.parseDouble(ProductosFinales.get(i)[0].getPrecioString().replace(",", "."));
                    totalPrecioCarrefour += Double.parseDouble(ProductosFinales.get(i)[1].getPrecioString().replace(",", "."));
                    totalPrecioAlcampo += Double.parseDouble(ProductosFinales.get(i)[2].getPrecioString().replace(",", "."));
                    if(Double.parseDouble(ProductosFinales.get(i)[0].getPrecioString().replace(",", ".")) < Double.parseDouble(ProductosFinales.get(i)[1].getPrecioString().replace(",", "."))){
                        if(Double.parseDouble(ProductosFinales.get(i)[0].getPrecioString().replace(",", ".")) < Double.parseDouble(ProductosFinales.get(i)[2].getPrecioString().replace(",", "."))){
                            precioMasBarato += Double.parseDouble(ProductosFinales.get(i)[0].getPrecioString().replace(",", "."));
                        } else {
                            precioMasBarato += Double.parseDouble(ProductosFinales.get(i)[2].getPrecioString().replace(",", "."));
                        }
                    } else {
                        if(Double.parseDouble(ProductosFinales.get(i)[1].getPrecioString().replace(",", ".")) < Double.parseDouble(ProductosFinales.get(i)[2].getPrecioString().replace(",", "."))){
                            precioMasBarato += Double.parseDouble(ProductosFinales.get(i)[1].getPrecioString().replace(",", "."));
                        } else {
                            precioMasBarato += Double.parseDouble(ProductosFinales.get(i)[2].getPrecioString().replace(",", "."));
                        }
                    }
                    System.out.println(ProductosFinales.get(i)[0].getTienda() + ": " + ProductosFinales.get(i)[0].getNombre() + " " + ProductosFinales.get(i)[0].getPrecioString() + "€");
                    System.out.println(ProductosFinales.get(i)[1].getTienda() + ": " + ProductosFinales.get(i)[1].getNombre() + " " + ProductosFinales.get(i)[1].getPrecioString() + "€");
                    System.out.println(ProductosFinales.get(i)[2].getTienda() + ": " + ProductosFinales.get(i)[2].getNombre() + " " + ProductosFinales.get(i)[2].getPrecioString() + "€");
                    System.out.println("-");
                }
                System.out.println("Total precio Eroski = " + totalPrecioEroski);
                System.out.println("Total precio Carrefour = " + totalPrecioCarrefour);
                System.out.println("Total precio Alcampo = " + totalPrecioAlcampo);
                System.out.println("Total precio más barato = " + precioMasBarato);
            }
        });

    }

    public void createAddListItem(int pos){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductosLista);
        //BOTON
        Button boton = new Button(context);
        LinearLayout.LayoutParams layoutparams3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams3.height = 120;
        layoutparams3.width = 120;
        layoutparams3.setMargins(25,70+150*pos,30,25);
        boton.setLayoutParams(layoutparams3);
        boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        boton.setTextColor(Color.WHITE);
        boton.setTypeface(null, Typeface.BOLD);
        boton.setBackgroundColor(Color.rgb(63, 149, 162));
        Drawable add = getResources().getDrawable(R.drawable.add);
        boton.setBackground(add);
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ListaActivity.this, SeleccionarProducto.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                ListaActivity.this.startActivity(i);
            }
        });

        //TITULO PRODUCTO
        TextView textview = new TextView(context);
        textview.setText("Añadir producto");
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        //textview.setTextColor(0x3f95a2);
        LinearLayout.LayoutParams layoutparams4 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams4.setMargins(165,100+150*pos,30,25);
        textview.setLayoutParams(layoutparams4);
        textview.setMaxLines(1);

        //RELLENAR LISTAS
        botonesList.add(boton);
        titulosList.add(textview);

        //ADD DE LOS VIEWS
        layout.addView(boton);
        layout.addView(textview);
    }

    public void anadirLista(){
        nProductosListaMostrados++;
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductosLista);
        layout.post(new Runnable(){
            @Override
            public void run() {
                final TextView titulo = titulosList.get(titulosList.size()-1);
                final Button boton = botonesList.get(botonesList.size()-1);
                titulo.setText(SeleccionarProducto.name);
                boton.setBackground(getResources().getDrawable(R.drawable.remove));
                boton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        for(int i=0; i<ProductosFinales.size();i++){
                            System.out.println(ProductosFinales.get(i)[0].getNombre() + " - " + titulo.getText());
                            if(ProductosFinales.get(i)[0].getNombre().equals(titulo.getText())){
                                ProductosFinales.remove(i);
                                break;
                            }
                        }
                        ((ViewGroup) titulo.getParent()).removeView(titulo);
                        ((ViewGroup) boton.getParent()).removeView(boton);
                        ((ViewGroup) titulosList.get(titulosList.size()-1).getParent()).removeView(titulosList.get(titulosList.size()-1));
                        ((ViewGroup) botonesList.get(botonesList.size()-1).getParent()).removeView(botonesList.get(botonesList.size()-1));
                        titulosList.remove(titulo);
                        botonesList.remove(boton);
                        titulosList.remove(titulosList.size()-1);
                        botonesList.remove(botonesList.size()-1);
                        nProductosLista--;
                        nProductosListaMostrados--;
                        refreshList();
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        finish();
                        overridePendingTransition(0, 0);
                        ListaActivity.this.startActivity(intent);
                    }
                });
            }
        });
    }

    private void refreshList(){
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductosLista);
        layout.post(new Runnable(){
            @Override
            public void run() {
                if(!titulosList.isEmpty()) {
                    for (int i = 0; i < titulosList.size(); i++) {
                        if ((ViewGroup) titulosList.get(i).getParent() != null)
                            ((ViewGroup) titulosList.get(i).getParent()).removeView(titulosList.get(i));
                        if ((ViewGroup) botonesList.get(i).getParent() != null)
                            ((ViewGroup) botonesList.get(i).getParent()).removeView(botonesList.get(i));
                    }
                    for (int i = 0; i < titulosList.size(); i++) {
                        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutparams.height = 120;
                        layoutparams.width = 120;
                        layoutparams.setMargins(25,70+150*i,30,25);
                        LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutparams2.setMargins(165,100+150*i,30,25);
                        botonesList.get(i).setLayoutParams(layoutparams);
                        titulosList.get(i).setLayoutParams(layoutparams2);
                        layout.addView(titulosList.get(i));
                        layout.addView(botonesList.get(i));
                    }
                    if(!titulosList.get(titulosList.size()-1).getText().equals("Añadir producto")) createAddListItem(titulosList.size());
                }
                else{
                    createAddListItem(titulosList.size());
                }
            }

        });
    }
}
