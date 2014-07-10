package gameutil2d.classes.text;

/*
 *  [TextDrawable.java]  - Classe utilizada  para  exibirmos frases , textos
 *  ou informações na tela. Ideal para a construção de HUD (Head-Up Display).
 *  
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 *  
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class TextDrawable {
	
	Paint p;
	
	int x;
	
	int y;
	
	public TextDrawable(Context context, FontName font_name)
	{
		
		p = new Paint();
		
		x = y = 0;
		
		switch (font_name) {
		case IMPACT:
			p.setTypeface(Typeface.createFromAsset(context.getAssets(), "impact.ttf"));
			break;
		case CHLORINAP:
			p.setTypeface(Typeface.createFromAsset(context.getAssets(), "chlorinap.ttf"));
			break;	
		case PC_SENIOR:
			p.setTypeface(Typeface.createFromAsset(context.getAssets(), "pcsenior.ttf"));
			break;			
		case TECHNO_OVERLOAD:
			p.setTypeface(Typeface.createFromAsset(context.getAssets(), "tecno_overload.ttf"));
			break;	
		case METAL_GEAR:
			p.setTypeface(Typeface.createFromAsset(context.getAssets(), "metal_gear.ttf"));
			break;	
		
		}
		
		p.setStyle(Style.FILL_AND_STROKE);
		p.setTextSize(20);
		p.setColor(Color.BLACK);
		p.setAntiAlias(true);
		
	}
	
	public void SetX(int value)
	{
		x = value;
	}
	
	public void SetY(int value)
	{
		y = value;
	}
	
	public void SetXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void MoveByX(int value)
	{
		x+=value;
	}
	
	public void MoveByY(int value)
	{
		y+=value;
	}
	
	public void SetColor(int color)
	{
		p.setColor(color);
	}
	
	public void SetSize(int size)
	{
		p.setTextSize(size);
	}
	
	public void DrawString(Canvas canvas, String text){
		
		canvas.drawText(text, x, y, p);
		
	}

}
