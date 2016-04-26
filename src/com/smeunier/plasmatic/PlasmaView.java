package com.smeunier.plasmatic;

import java.io.File;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PlasmaView extends View{
	Context mContext;

	private Bitmap plasmaImage;
	
	public PlasmaView(Context context) {
		super(context);
	}
	
	public PlasmaView(Context context, int width, int height, String plasmaType, float roughness, int seed) {
		super(context);
		mContext = context;
		Log.i("a", String.valueOf(width) + "-" + String.valueOf(height) + "-" + plasmaType + "-" + String.valueOf(roughness) + "-" + String.valueOf(seed));
		new AsyncGenerate(context, this).execute(String.valueOf(width), String.valueOf(height), plasmaType, String.valueOf(roughness), String.valueOf(seed));
	}
	
	public void SaveImage(File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			plasmaImage.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			Toast.makeText(mContext, "Could not save image", Toast.LENGTH_LONG).show();
		}

		Toast.makeText(mContext, "File saved to sd card", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDraw(Canvas canvas){
		if (plasmaImage != null) {
			canvas.drawBitmap(plasmaImage, 0, 0, null);
	//		invalidate();
		}
	}
	
	
    private class AsyncGenerate extends AsyncTask<String, Void, Bitmap> {

    	private ProgressDialog progressDialog;
    	final private Context context;
    	private PlasmaView view;
    	
		public AsyncGenerate(Context context, PlasmaView view)
		{
			this.context = context;
			this.view = view;
		}
		
        @Override
        protected Bitmap doInBackground(String... params) {
    		PlasmaFractal plasmaFractal = new PlasmaFractal();
    		return plasmaFractal.Generate(Integer.parseInt(params[0]), Integer.parseInt(params[1]), params[2], Float.parseFloat(params[3]), Integer.parseInt(params[4]));
        //   return null;
        }      

        @Override
        protected void onPostExecute(Bitmap result) { 
           
            this.view.plasmaImage = result;
            this.view.invalidate();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        	 progressDialog = ProgressDialog.show(context, "", "Generating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}