package com.dsole.controldellamadas.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsole.controldellamadas.R;
import com.dsole.controldellamadas.classes.CallLog;

import java.util.List;


/**
 * Created by dsole on 12/02/2015.
 */
public class ResultadosAdapter extends RecyclerView.Adapter<ResultadosAdapter.ViewHolder> {

    private List<CallLog> items;
    private int itemLayout;
    private Context context;

    public ResultadosAdapter(Context context, List<CallLog> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CallLog item = items.get(position);

        holder.nombre.setText(item.getName());
        holder.numero.setText(item.getNumber());
        holder.fecha.setText(item.getDate());
        holder.tiempo.setText(item.getTime());
        holder.tipo.setText(item.getType());
        if(item.getImagen()!=null) holder.imagen.setImageBitmap(getRoundedShape(item.getImagen()));
    }

    public void add(CallLog item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(CallLog item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        //int targetWidth = (int) scaleBitmapImage.getWidth();
        //int targetHeight = (int) scaleBitmapImage.getHeight();

        int targetWidth = 190;
        int targetHeight = 189;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView numero;
        public TextView tiempo;
        public TextView fecha;
        public TextView tipo;
        public ImageView imagen;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre);
            numero = (TextView) itemView.findViewById(R.id.numero);
            tiempo = (TextView) itemView.findViewById(R.id.tiempo);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            tipo = (TextView) itemView.findViewById(R.id.tipo);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
        }
    }
}
