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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class opcionesActivity extends AppCompatActivity {


    Context context;
    public static ArrayList<Producto> cestafinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        setTitle("Seleccionar una opción");
        context = getApplicationContext();
        cestafinal = new ArrayList<Producto>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        selectCardsOrder();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.busqueda_listas) {
                    Intent i = new Intent(opcionesActivity.this, ListaActivity.class);
                    opcionesActivity.this.startActivity(i);
                } else if (id == R.id.busqueda_manual) {
                    Intent i = new Intent(opcionesActivity.this, MainActivity.class);
                    opcionesActivity.this.startActivity(i);
                } else if (id == R.id.otro) {

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void selectCardsOrder(){
        int pos = 20;

        //IMPRIME MEJOR 1 SUPER
        if(getPrecioDeCesta(ListaActivity.ProductosEroski) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroski) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            addCardView(pos, ListaActivity.ProductosEroski);
            pos+=500;
        } else if(getPrecioDeCesta(ListaActivity.ProductosCarrefour) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
            addCardView(pos, ListaActivity.ProductosCarrefour);
            pos+=500;
        } else {
            addCardView(pos, ListaActivity.ProductosAlcampo);
            pos+=500;
        }

        //IMPRIME MEJOR 2 SUPERS SI HAY AHORRO
        if(getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            addCardView(pos, ListaActivity.ProductosCarrefourAlcampo);
            pos+=500;
        } else if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski)){
            addCardView(pos, ListaActivity.ProductosEroskiAlcampo);
            pos+=500;
        } else if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            addCardView(pos, ListaActivity.ProductosEroskiCarrefour);
            pos+=500;
        }

        //IMPRIME 3 SUPERS SI HAY AHORRO
        if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour)){
            addCardView(pos, ListaActivity.ProductosEroskiCarrefourAlcampo);
            pos+=500;
        }

        //IMPRIME SEGUNDO MEJOR SUPER Y PEOR SUPER
        if(getPrecioDeCesta(ListaActivity.ProductosEroski) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroski) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            //MIRAR MEJOR ENTRE ALCAMPO Y CARREFOUR
            if(getPrecioDeCesta(ListaActivity.ProductosAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                addCardView(pos, ListaActivity.ProductosAlcampo);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosCarrefour);
                pos+=500;
            } else {
                addCardView(pos, ListaActivity.ProductosCarrefour);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosAlcampo);
                pos+=500;

            }
        } else if(getPrecioDeCesta(ListaActivity.ProductosCarrefour) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
            //MIRAR MEJOR ENTRE ALCAMPO Y EROSKI
            if(getPrecioDeCesta(ListaActivity.ProductosAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski)){
                addCardView(pos, ListaActivity.ProductosAlcampo);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosEroski);
                pos+=500;
            } else {
                addCardView(pos, ListaActivity.ProductosEroski);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosAlcampo);
                pos+=500;

            }
        } else {
            //MIRAR MEJOR ENTRE CARREFOUR Y EROSKI
            if(getPrecioDeCesta(ListaActivity.ProductosCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski)){
                addCardView(pos, ListaActivity.ProductosCarrefour);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosEroski);
                pos+=500;
            } else {
                addCardView(pos, ListaActivity.ProductosEroski);
                pos+=500;
                addCardView(pos, ListaActivity.ProductosCarrefour);
                pos+=500;
            }
        }

        //IMPRIME SEGUNDO MEJOR Y PEOR 2 SUPERS SI HAY AHORRO
        if(getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            //MIRAR MEJOR ENTRE EROSKIALCAMPO Y EROSKICARREFOUR
            if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour)){
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
                    addCardView(pos, ListaActivity.ProductosEroskiAlcampo);
                    pos+=500;
                }
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosEroskiCarrefour);
                    pos+=500;
                }
            } else {
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosEroskiCarrefour);
                    pos+=500;
                }
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
                    addCardView(pos, ListaActivity.ProductosEroskiAlcampo);
                    pos+=500;
                }
            }
        } else if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski)){
            //MIRAR MEJOR ENTRE CARREFOURALCAMPO Y EROSKICARREFOUR
            if(getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour)){
                if(getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                        getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosCarrefourAlcampo);
                    pos+=500;
                }
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosEroskiCarrefour);
                    pos+=500;
                }
            } else {
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosEroskiCarrefour);
                    pos+=500;
                }
                if(getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo) &&
                        getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
                    addCardView(pos, ListaActivity.ProductosCarrefourAlcampo);
                    pos+=500;
                }
            }
        } else if(getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                getPrecioDeCesta(ListaActivity.ProductosEroskiCarrefour) < getPrecioDeCesta(ListaActivity.ProductosCarrefour)){
            //MIRAR MEJOR ENTRE EROSKIALCAMPO Y CARREFOURALCAMPO
            if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosCarrefourAlcampo)){
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
                    addCardView(pos, ListaActivity.ProductosEroskiAlcampo);
                    pos+=500;
                }
                addCardView(pos, ListaActivity.ProductosCarrefourAlcampo);
                pos+=500;
            } else {
                addCardView(pos, ListaActivity.ProductosCarrefourAlcampo);
                pos+=500;
                if(getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosEroski) &&
                        getPrecioDeCesta(ListaActivity.ProductosEroskiAlcampo) < getPrecioDeCesta(ListaActivity.ProductosAlcampo)){
                    addCardView(pos, ListaActivity.ProductosEroskiAlcampo);
                    pos+=500;
                }
            }
        }

    }

    public double getPrecioDeCesta(ArrayList<Producto> cesta){
        double precio=0;
        for(Producto prod: cesta){
            precio+=Double.parseDouble(prod.getPrecioString().replace(",", "."));
        }
        return precio;
    }

    public boolean tiendaCheck(ArrayList<Producto> cesta, String tienda){
        for(Producto prod: cesta){
            if(prod.getTienda().equals(tienda)){
                return true;
            }
        }
        return false;
    }

    public void addCardView(int pos, ArrayList<Producto> productos){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layoutProductosLista);
        boolean carrefour = tiendaCheck(productos,"Carrefour");
        boolean eroski  = tiendaCheck(productos,"Eroski");
        boolean alcampo  = tiendaCheck(productos,"Alcampo");
        int nSupermercados = 0;
        final ArrayList<Producto> cesta = productos;

        if(carrefour){
            nSupermercados++;
        }
        if(eroski){
            nSupermercados++;
        }
        if(alcampo){
            nSupermercados++;
        }

        //CARDVIEW
        CardView cardview = new CardView(context);
        LinearLayout.LayoutParams layoutparams;
        layoutparams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(17,11+pos,17,15);
        layoutparams.height = 480;
        cardview.setLayoutParams(layoutparams);
        cardview.setUseCompatPadding(true);
        cardview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cestafinal = cesta;
                Intent i = new Intent(opcionesActivity.this, opcionCestaFinalActivity.class);
                opcionesActivity.this.startActivity(i);
            }
        });

        if(nSupermercados == 1) {
            //IMAGEN LOGO
            Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier(productos.get(0).getTienda().toLowerCase() + "logo", "drawable", context.getPackageName()));
            ImageView logoview = new ImageView(context);
            logoview.setImageDrawable(logo);
            logoview.setPadding(0, 0, 600, 50);
            LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams2.setMargins(30, 0, 0, 58);
            layoutparams2.height = 500;
            logoview.setLayoutParams(layoutparams2);
            cardview.addView(logoview);
        } else if(nSupermercados == 2){
            int desplazamiento = 0;
            if(carrefour){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("carrefourlogo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 160, 600, 0);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
                desplazamiento += 160;
            }
            if(eroski){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("eroskilogo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 160-desplazamiento, 600, desplazamiento);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
                desplazamiento += 200;
            }
            if(alcampo){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("alcampologo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 0, 600, 0+desplazamiento);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
            }

        } else {
            //IMAGEN LOGO
            if(carrefour){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("carrefourlogo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 260, 600, 0);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
            }
            if(eroski){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("eroskilogo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 0, 600, 40);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
            }
            if(alcampo){
                //IMAGEN LOGO
                Drawable logo = context.getResources().getDrawable(context.getResources().getIdentifier("alcampologo", "drawable", context.getPackageName()));
                ImageView logoview = new ImageView(context);
                logoview.setImageDrawable(logo);
                logoview.setPadding(0, 0, 600, 340);
                LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams2.setMargins(30, 0, 0, 58);
                layoutparams2.height = 500;
                logoview.setLayoutParams(layoutparams2);
                cardview.addView(logoview);
            }
        }

        //Precio
        double val = getPrecioDeCesta(productos);
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
        TextView textview = new TextView(context);
        textview.setText(precioString+"€");
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
        textview.setTypeface(null, Typeface.BOLD);
        textview.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutparams4 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(precioString.length()==4) {
            layoutparams4.setMargins(720, 195, 0, 0);
        } else {
            layoutparams4.setMargins(700, 195, 0, 0);
        }
        textview.setLayoutParams(layoutparams4);
        textview.setMaxLines(1);

        //Numero de productos
        TextView textview2 = new TextView(context);
        textview2.setText("Total, "+productos.size()+" productos");
        textview2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        LinearLayout.LayoutParams layoutparams5 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams5.setMargins(665,120,0,0);
        textview2.setLayoutParams(layoutparams5);
        textview2.setMaxLines(1);

        //ADD DE LOS VIEWS
        cardview.addView(textview);
        cardview.addView(textview2);
        layout.addView(cardview);

    }


}
