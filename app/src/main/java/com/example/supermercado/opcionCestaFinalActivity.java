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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;

public class opcionCestaFinalActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_cesta_final);
        setTitle("Cesta de la compra");
        context = getApplicationContext();

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
                    Intent i = new Intent(opcionCestaFinalActivity.this, ListaActivity.class);
                    opcionCestaFinalActivity.this.startActivity(i);
                } else if (id == R.id.busqueda_manual) {
                    Intent i = new Intent(opcionCestaFinalActivity.this, MainActivity.class);
                    opcionCestaFinalActivity.this.startActivity(i);
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
            }
        });

        createItems();

    }

    public boolean tiendaCheck(ArrayList<Producto> cesta, String tienda){
        for(Producto prod: cesta){
            if(prod.getTienda().equals(tienda)){
                return true;
            }
        }
        return false;
    }

    public void createItems(){
        int pos = 0;
        final ArrayList<Producto> cesta = opcionesActivity.cestafinal;
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductosLista);

        //Precio
        double val = getPrecioDeCesta(cesta);
        val = val * 100;
        val = Math.round(val);
        val = val / 100;
        String precioString = Double.toString(val);
        boolean decimal = false;
        for(int i=0; i<precioString.length();i++){
            if(precioString.charAt(i)=='.'){
                decimal = true;
                if(precioString.length() == i+2) {
                    precioString = precioString + "0";
                    break;
                }
            }
        }
        if(!decimal) precioString = precioString + ".00";
        final TextView textviewP = new TextView(context);

        textviewP.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
        textviewP.setTypeface(null, Typeface.BOLD);
        textviewP.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutparams40 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        String totalPrice = "TOTAL\n ";
        if(precioString.length()>4) {
            totalPrice+= " ";
        }

        if(tiendaCheck(cesta,"Eroski")){
            Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("eroskilogo", "drawable", context.getPackageName()));
            ImageView logoview = new ImageView(context);
            logoview.setImageDrawable(logo);
            //logoview.setPadding(0, 50+pos, 0, 0);
            LinearLayout.LayoutParams layoutparams1 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams1.setMargins(30, 50, 0, 58);
            layoutparams1.height = 100;
            logoview.setLayoutParams(layoutparams1);
            layout.addView(logoview);
            pos += 200;
            for(Producto prod: cesta){
                if(prod.getTienda().equals("Eroski")){
                    //TITULO DE PRODUCTOS
                    TextView textview2 = new TextView(context);
                    textview2.setText(prod.getNombre());
                    textview2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams2.setMargins(30,pos,30,0);
                    textview2.setLayoutParams(layoutparams2);
                    textview2.setMaxLines(1);
                    layout.addView(textview2);

                    //PRECIO
                    final TextView textview = new TextView(context);
                    textview.setText(prod.getPrecioString()+"€");
                    textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
                    textview.setTypeface(null, Typeface.BOLD);
                    textview.setTextColor(Color.BLACK);
                    LinearLayout.LayoutParams layoutparams3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams3.setMargins(850, pos+80, 0, 0);
                    textview.setLayoutParams(layoutparams3);
                    textview.setMaxLines(1);
                    layout.addView(textview);
                    pos += 200;

                    //PRECIO POR UNIDAD
                    TextView textview3 = new TextView(context);
                    textview3.setText("Precio por unidad: "+prod.getPrecioString()+"€");
                    textview3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    LinearLayout.LayoutParams layoutparams10 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams10.setMargins(270, pos-108, 0, 0);
                    textview3.setLayoutParams(layoutparams10);
                    textview3.setMaxLines(1);
                    layout.addView(textview3);

                    //N UNIDADES
                    final TextView textviewX = new TextView(context);
                    textviewX.setText("1");
                    textviewX.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    textviewX.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams layoutparams20 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams20.setMargins(120,pos-118,30,0);
                    textviewX.setLayoutParams(layoutparams20);
                    textviewX.setMaxLines(1);
                    layout.addView(textviewX);

                    //BOTON +
                    Button boton = new Button(context);
                    LinearLayout.LayoutParams layoutparams11 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams11.height = 80;
                    layoutparams11.width = 80;
                    layoutparams11.setMargins(160,pos-120,0,0);
                    boton.setLayoutParams(layoutparams11);
                    boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton.setTextColor(Color.WHITE);
                    boton.setTypeface(null, Typeface.BOLD);
                    boton.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable add = getResources().getDrawable(R.drawable.add);
                    boton.setBackground(add);
                    final Producto p = prod;
                    boton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) < 10) {
                                int n = Integer.parseInt(textviewX.getText().toString()) + 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                df.setDecimalFormatSymbols(symbols);
                                p.setCantidad(p.getCantidad()+1);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);
                                /*
                                String lastPrecio = textviewP.getText().toString().substring(5);
                                lastPrecio = lastPrecio.replace("€","");
                                lastPrecio = lastPrecio.replace(" ", "");

                                double val = Double.parseDouble(lastPrecio);
                                val += Double.parseDouble(p.getPrecioString().replace(",", "."));*/
                                double val = 0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton);

                    //BOTON -
                    Button boton2 = new Button(context);
                    LinearLayout.LayoutParams layoutparams12 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams12.height = 80;
                    layoutparams12.width = 80;
                    layoutparams12.setMargins(30,pos-120,0,0);
                    boton2.setLayoutParams(layoutparams12);
                    boton2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton2.setTextColor(Color.WHITE);
                    boton2.setTypeface(null, Typeface.BOLD);
                    boton2.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable rem = getResources().getDrawable(R.drawable.remove);
                    boton2.setBackground(rem);
                    boton2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) > 0) {
                                int n = Integer.parseInt(textviewX.getText().toString()) - 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                df.setDecimalFormatSymbols(symbols);
                                p.setCantidad(p.getCantidad()-1);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);

                                double val=0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton2);

                }
            }
        }
        if(tiendaCheck(cesta,"Carrefour")){
            Drawable logo2 = context.getResources().getDrawable(context.getResources().getIdentifier("carrefourlogo", "drawable", context.getPackageName()));
            ImageView logoview2 = new ImageView(context);
            logoview2.setImageDrawable(logo2);
            //logoview2.setPadding(0, 50+pos, 0, 0);
            LinearLayout.LayoutParams layoutparams4 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams4.setMargins(30, pos+50, 0, 58);
            layoutparams4.height = 100;
            logoview2.setLayoutParams(layoutparams4);
            layout.addView(logoview2);
            pos += 200;
            for(Producto prod: cesta){
                if(prod.getTienda().equals("Carrefour")){
                    //TITULO DE PRODUCTOS
                    TextView textview2 = new TextView(context);
                    textview2.setText(prod.getNombre());
                    textview2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    LinearLayout.LayoutParams layoutparams5 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams5.setMargins(30,pos,30,0);
                    textview2.setLayoutParams(layoutparams5);
                    textview2.setMaxLines(1);
                    layout.addView(textview2);

                    //PRECIO
                    final TextView textview = new TextView(context);
                    textview.setText(prod.getPrecioString()+"€");
                    textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
                    textview.setTypeface(null, Typeface.BOLD);
                    textview.setTextColor(Color.BLACK);
                    LinearLayout.LayoutParams layoutparams6 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams6.setMargins(850, pos+80, 0, 0);
                    textview.setLayoutParams(layoutparams6);
                    textview.setMaxLines(1);
                    layout.addView(textview);
                    pos += 200;

                    //PRECIO POR UNIDAD
                    TextView textview3 = new TextView(context);
                    textview3.setText("Precio por unidad: "+prod.getPrecioString()+"€");
                    textview3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    LinearLayout.LayoutParams layoutparams10 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams10.setMargins(270, pos-108, 0, 0);
                    textview3.setLayoutParams(layoutparams10);
                    textview3.setMaxLines(1);
                    layout.addView(textview3);


                    //N UNIDADES
                    final TextView textviewX = new TextView(context);
                    textviewX.setText("1");
                    textviewX.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    textviewX.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams layoutparams20 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams20.setMargins(120,pos-118,30,0);
                    textviewX.setLayoutParams(layoutparams20);
                    textviewX.setMaxLines(1);
                    layout.addView(textviewX);

                    //BOTON +
                    Button boton = new Button(context);
                    LinearLayout.LayoutParams layoutparams11 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams11.height = 80;
                    layoutparams11.width = 80;
                    layoutparams11.setMargins(160,pos-120,0,0);
                    boton.setLayoutParams(layoutparams11);
                    boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton.setTextColor(Color.WHITE);
                    boton.setTypeface(null, Typeface.BOLD);
                    boton.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable add = getResources().getDrawable(R.drawable.add);
                    boton.setBackground(add);
                    final Producto p = prod;
                    boton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) < 10) {
                                int n = Integer.parseInt(textviewX.getText().toString()) + 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                df.setDecimalFormatSymbols(symbols);
                                p.setCantidad(p.getCantidad()+1);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);

                                double val=0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton);

                    //BOTON -
                    Button boton2 = new Button(context);
                    LinearLayout.LayoutParams layoutparams12 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams12.height = 80;
                    layoutparams12.width = 80;
                    layoutparams12.setMargins(30,pos-120,0,0);
                    boton2.setLayoutParams(layoutparams12);
                    boton2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton2.setTextColor(Color.WHITE);
                    boton2.setTypeface(null, Typeface.BOLD);
                    boton2.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable rem = getResources().getDrawable(R.drawable.remove);
                    boton2.setBackground(rem);
                    boton2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) > 0) {
                                int n = Integer.parseInt(textviewX.getText().toString()) - 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                p.setCantidad(p.getCantidad()-1);
                                df.setDecimalFormatSymbols(symbols);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);

                                double val=0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton2);
                }
            }
        }
        if(tiendaCheck(cesta,"Alcampo")){
            Drawable logo3 = context.getResources().getDrawable(context.getResources().getIdentifier("alcampologo", "drawable", context.getPackageName()));
            ImageView logoview3 = new ImageView(context);
            logoview3.setImageDrawable(logo3);
            //logoview3.setPadding(0, 50+pos, 0, 0);
            LinearLayout.LayoutParams layoutparams7 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams7.setMargins(30, pos+50, 0, 58);
            layoutparams7.height = 100;
            logoview3.setLayoutParams(layoutparams7);
            layout.addView(logoview3);
            pos += 200;
            for(Producto prod: cesta){
                if(prod.getTienda().equals("Alcampo")){
                    //TITULO DE PRODUCTOS
                    TextView textview2 = new TextView(context);
                    textview2.setText(prod.getNombre());
                    textview2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    LinearLayout.LayoutParams layoutparams8 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams8.setMargins(30,pos,30,0);
                    textview2.setLayoutParams(layoutparams8);
                    textview2.setMaxLines(1);
                    layout.addView(textview2);

                    //PRECIO
                    final TextView textview = new TextView(context);
                    textview.setText(prod.getPrecioString()+"€");
                    textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
                    textview.setTypeface(null, Typeface.BOLD);
                    textview.setTextColor(Color.BLACK);
                    LinearLayout.LayoutParams layoutparams9 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams9.setMargins(850, pos+80, 0, 0);
                    textview.setLayoutParams(layoutparams9);
                    textview.setMaxLines(1);
                    layout.addView(textview);
                    pos += 200;

                    //PRECIO POR UNIDAD
                    TextView textview3 = new TextView(context);
                    textview3.setText("Precio por unidad: "+prod.getPrecioString()+"€");
                    textview3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    LinearLayout.LayoutParams layoutparams10 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams10.setMargins(270, pos-108, 0, 0);
                    textview3.setLayoutParams(layoutparams10);
                    textview3.setMaxLines(1);
                    layout.addView(textview3);


                    //N UNIDADES
                    final TextView textviewX = new TextView(context);
                    textviewX.setText("1");
                    textviewX.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    textviewX.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams layoutparams20 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams20.setMargins(120,pos-118,30,0);
                    textviewX.setLayoutParams(layoutparams20);
                    textviewX.setMaxLines(1);
                    layout.addView(textviewX);

                    //BOTON +
                    Button boton = new Button(context);
                    LinearLayout.LayoutParams layoutparams11 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams11.height = 80;
                    layoutparams11.width = 80;
                    layoutparams11.setMargins(160,pos-120,0,0);
                    boton.setLayoutParams(layoutparams11);
                    boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton.setTextColor(Color.WHITE);
                    boton.setTypeface(null, Typeface.BOLD);
                    boton.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable add = getResources().getDrawable(R.drawable.add);
                    boton.setBackground(add);
                    final Producto p = prod;
                    boton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) < 10) {
                                int n = Integer.parseInt(textviewX.getText().toString()) + 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                df.setDecimalFormatSymbols(symbols);
                                p.setCantidad(p.getCantidad()+1);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);

                                double val=0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton);

                    //BOTON -
                    Button boton2 = new Button(context);
                    LinearLayout.LayoutParams layoutparams12 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams12.height = 80;
                    layoutparams12.width = 80;
                    layoutparams12.setMargins(30,pos-120,0,0);
                    boton2.setLayoutParams(layoutparams12);
                    boton2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    boton2.setTextColor(Color.WHITE);
                    boton2.setTypeface(null, Typeface.BOLD);
                    boton2.setBackgroundColor(Color.rgb(63, 149, 162));
                    Drawable rem = getResources().getDrawable(R.drawable.remove);
                    boton2.setBackground(rem);
                    boton2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(Integer.parseInt(textviewX.getText().toString()) > 0) {
                                int n = Integer.parseInt(textviewX.getText().toString()) - 1;
                                textviewX.setText(n+"");
                                layout.removeView(textviewX);
                                layout.addView(textviewX);
                                DecimalFormat df = new DecimalFormat();
                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                symbols.setDecimalSeparator(',');
                                symbols.setGroupingSeparator(' ');
                                p.setCantidad(p.getCantidad()-1);
                                df.setDecimalFormatSymbols(symbols);
                                double precio = 0;
                                try {
                                    precio = df.parse(p.getPrecioString()).doubleValue();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                precio *= Double.parseDouble(textviewX.getText().toString());
                                textview.setText(precio+"€");
                                layout.removeView(textview);
                                layout.addView(textview);

                                double val=0;
                                for(Producto prod: cesta){
                                    val+=Double.parseDouble(prod.getPrecioString().replace(",", ".")) * prod.getCantidad();
                                }
                                val = val * 100;
                                val = Math.round(val);
                                val = val / 100;
                                String precioString = Double.toString(val);
                                boolean decimal = false;
                                for(int i=0; i<precioString.length();i++){
                                    if(precioString.charAt(i)=='.'){
                                        decimal = true;
                                        if(precioString.length() == i+2) {
                                            precioString = precioString + "0";
                                            break;
                                        }
                                    }
                                }
                                if(!decimal) precioString = precioString + ".00";
                                String totalPrice = "TOTAL\n ";
                                if(precioString.length()>4) {
                                    totalPrice+= " ";
                                }
                                textviewP.setText(totalPrice+precioString+"€");
                                layout.removeView(textviewP);
                                layout.addView(textviewP);
                            }
                        }
                    });
                    layout.addView(boton2);
                }
            }
        }

        //POSICION DE PRECIO TOTAL
        int postotal = pos;
        if(pos<1250) postotal = 1300;
        textviewP.setText(totalPrice+precioString+"€");
        layoutparams40.setMargins(400, postotal, 0, 0);
        textviewP.setLayoutParams(layoutparams40);
        layout.addView(textviewP);

    }

    public double getPrecioDeCesta(ArrayList<Producto> cesta){
        double precio=0;
        for(Producto prod: cesta){
            precio+=Double.parseDouble(prod.getPrecioString().replace(",", "."));
        }
        return precio;
    }

}
