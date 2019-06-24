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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public static TextView textViewNProductos;
    public static TextView textViewCestaVacia;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        setTitle("Cesta de la compra");

        textViewCestaVacia = (TextView) findViewById(R.id.textVacio);
        textViewNProductos = (TextView) findViewById(R.id.textViewNProductos);
        textViewNProductos.setText(MainActivity.nProductosCesta.toString());
        final Context context = getApplicationContext();

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
                    Intent i = new Intent(CartActivity.this, ListaActivity.class);
                    CartActivity.this.startActivity(i);
                } else if (id == R.id.busqueda_manual) {
                    Intent i = new Intent(CartActivity.this, MainActivity.class);
                    CartActivity.this.startActivity(i);
                } else if (id == R.id.otro) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        LinearLayout.LayoutParams layoutparams;

        createCardViews(context);


    }

    private void createCardViews(Context context) {
        LinearLayout.LayoutParams layoutparams;

        this.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                if(MainActivity.nProductosCesta>0) textViewCestaVacia.setVisibility(View.GONE);
                else textViewCestaVacia.setVisibility(View.VISIBLE);
            } });

        ArrayList<Producto> cestaEroski = new ArrayList<Producto>();
        ArrayList<Producto> cestaCarrefour = new ArrayList<Producto>();
        ArrayList<Producto> cestaAlcampo = new ArrayList<Producto>();
        for(int i = 0; i< MainActivity.cesta.size(); i++){
            if("Carrefour".equals(MainActivity.cesta.get(i).getTienda())){
                cestaCarrefour.add(MainActivity.cesta.get(i));
            }
            else if("Eroski".equals(MainActivity.cesta.get(i).getTienda())){
                cestaEroski.add(MainActivity.cesta.get(i));
            }
            else if("Alcampo".equals(MainActivity.cesta.get(i).getTienda())){
                cestaAlcampo.add(MainActivity.cesta.get(i));
            }
        }

        for(int i = 0; i< MainActivity.cesta.size(); i++){
            final Producto producto = MainActivity.cesta.get(i);
            int index = i;
            int pos = i*500;
            String name = producto.getNombre();
            String precio = producto.getPrecioString();

            //CARDVIEW
            final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutCesta);
            final CardView cardview = new CardView(context);
            layoutparams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams.setMargins(17,11+pos,17,15);
            layoutparams.height = 500;
            cardview.setLayoutParams(layoutparams);
            cardview.setUseCompatPadding(true);

            //IMAGEN LOGO
            Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier(producto.getTienda().toLowerCase()+"logo", "drawable", context.getPackageName()));
            ImageView logoview = new ImageView(context);
            logoview.setImageDrawable(logo);
            logoview.setPadding(0,300,700,0);
            LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams2.setMargins(30,0,0,58);
            layoutparams2.height = 500;
            logoview.setLayoutParams(layoutparams2);

            //IMAGEN PLACEHOLDER
            Drawable placeholder = context.getResources().getDrawable(context.getResources().getIdentifier("placeholder", "drawable", context.getPackageName()));
            ImageView placeholderview = new ImageView(context);
            placeholderview.setImageDrawable(placeholder);
            placeholderview.setPadding(0,0,700,200);
            LinearLayout.LayoutParams layoutparams1 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams1.setMargins(20,20,0,0);
            layoutparams1.height = 500;
            placeholderview.setLayoutParams(layoutparams1);

            //TITULO PRODUCTO
            TextView textview = new TextView(context);
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

            //BOTON
            Button boton = new Button(context);
            LinearLayout.LayoutParams layoutparams3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams3.height = 90;
            layoutparams3.width = 300;
            layoutparams3.setMargins(700,350,30,25);
            boton.setLayoutParams(layoutparams3);
            boton.setText("Eliminar");
            boton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
            boton.setTextColor(Color.WHITE);
            boton.setTypeface(null, Typeface.BOLD);
            boton.setBackgroundColor(Color.rgb(63, 149, 162));
            final Context con = context;
            boton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.cesta.remove(producto);
                    textViewNProductos.setText((--MainActivity.nProductosCesta).toString());
                    layout.removeAllViews();
                    layout.addView(textViewCestaVacia);
                    createCardViews(con);
                }
            });

            //ADD DE LOS VIEWS
            cardview.addView(textview);
            cardview.addView(precioview);
            cardview.addView(logoview);
            cardview.addView(boton);
            cardview.addView(placeholderview);
            layout.addView(cardview);
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.textViewNProductos.setText((MainActivity.nProductosCesta).toString());
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

        /*if (id == R.id.mybutton) {
            Intent myIntent = new Intent(this, CartActivity.class);
            startActivity(myIntent);
        }/*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
