package com.smeunier.plasmatic;

import java.util.Random;

import android.graphics.Bitmap;

public class PlasmaFractal {
	public float roughness;
	private int width;
	private int height;
	private String plasmaType;
	private float bigSize;
	public long seed;
	private Random random;
	private float[][] points;
	
    private float Displace(float smallSize)
    {
        
        float max = smallSize / bigSize * roughness;
        return (random.nextFloat() - 0.5f) * max;
    }

    public Bitmap Generate(int width, int height, String plasmaType, float roughness, int seed)
    {
		this.width = width;
		this.height = height;
		this.plasmaType = plasmaType;
		this.roughness = roughness;
		this.seed = seed;
				
		if (this.seed > 0)
			random = new Random(this.seed);
		else
			random = new Random();
		
        points = new float[width + 1][height + 1];
        float c1, c2, c3, c4;
        
        //Assign the four corners of the initial grid random color values
        c1 = random.nextFloat();
        c2 = random.nextFloat();
        c3 = random.nextFloat();
        c4 = random.nextFloat();
        bigSize = this.width + this.height;
        DivideGrid(0, 0, this.width, this.height, c1, c2, c3, c4);
        
        Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
            	bitmap.setPixel(x, y, ComputeColor(points[x][y], this.plasmaType));
            }
        }
        return bitmap;
    }

    public void DivideGrid(float x, float y, float gridWidth, float gridHeight, float c1, float c2, float c3, float c4)
    {
        float edge1, edge2, edge3, edge4, middle;

        float newWidth = (float) Math.floor(gridWidth / 2);
        float newHeight = (float) Math.floor(gridHeight / 2);

        if (gridWidth > 1 || gridHeight > 1)
        {
            middle = ((c1 + c2 + c3 + c4) / 4) + Displace(newWidth + newHeight);	//Randomly displace the midpoint!
            edge1 = ((c1 + c2) / 2);	//Calculate the edges by averaging the two corners of each edge.
            edge2 = ((c2 + c3) / 2);
            edge3 = ((c3 + c4) / 2);
            edge4 = ((c4 + c1) / 2);
            //Make sure that the midpoint doesn't accidentally "randomly displaced" past the boundaries!
            middle = Rectify(middle);
            edge1 = Rectify(edge1);
            edge2 = Rectify(edge2);
            edge3 = Rectify(edge3);
            edge4 = Rectify(edge4);
            //Do the operation over again for each of the four new grids.			
            DivideGrid(x, y, newWidth, newHeight, c1, edge1, middle, edge4);
            DivideGrid(x + newWidth, y, gridWidth - newWidth, newHeight, edge1, c2, edge2, middle);
            DivideGrid(x + newWidth, y + newHeight, gridWidth - newWidth, gridHeight - newHeight, middle, edge2, c3, edge3);
            DivideGrid(x, y + newHeight, newWidth, gridHeight - newHeight, edge4, middle, edge3, c4);
        }
        else	//This is the "base case," where each grid piece is less than the size of a pixel.
        {
            //The four corners of the grid piece will be averaged and drawn as a single pixel.
            float c = (c1 + c2 + c3 + c4) / 4;

            points[(int)(x)][(int)(y)] = c;
            if (gridWidth == 2)
            {
                points[(int)(x+1)][(int)(y)] = c;
            }
            if (gridHeight == 2)
            {
                points[(int)(x)][(int)(y+1)] = c;
            }
            if ((gridWidth == 2) && (gridHeight == 2)) 
            {
                points[(int)(x + 1)][(int)(y+1)] = c;
            }
        }
    }

    private float Rectify(float num)
    {
        if (num < 0)
        {
            num = 0;
        }
        else if (num > 1.0)
        {
            num = 1.0f;
        }
        return num;
    }

    //Returns a color based on a color value, c.
    public int ComputeColor(float c, String plasmaType)
    {
        float red = 0;
        float green = 0;
        float blue = 0;

        if (plasmaType.equals("Plasma"))
        {
            if (c < 0.5)
            {
                red = c * 2;
            }
            else
            {
                red = (1.0f - c) * 2;
            }

            if (c >= 0.3 && c < 0.8)
            {
                green = (c - 0.3f) * 2;
            }
            else if (c < 0.3)
            {
                green = (0.3f - c) * 2;
            }
            else
            {
                green = (1.3f - c) * 2;
            }

            if (c >= 0.5)
            {
                blue = (c - 0.5f) * 2;
            }
            else
            {
                blue = (0.5f - c) * 2;
            }
        }
        else if (plasmaType.equals("Cloud"))
        {

            if (c < 0.3)
            {
                red = c;
            }
            red = green = c;

            blue = 1;
        }
        else
        {
            red = green = blue = c;
        }
        
        int color = ((255 & 0xFF) << 24) | //alpha
        ((Math.round(red * 255) & 0xFF) << 16) | //red
        ((Math.round(green * 255) & 0xFF) << 8)  | //green
        ((Math.round(blue * 255) & 0xFF) << 0); //blue

        return color;
    }	
}
