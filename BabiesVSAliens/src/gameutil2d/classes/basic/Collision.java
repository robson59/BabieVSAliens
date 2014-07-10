package gameutil2d.classes.basic;

/*
 *  [Collision.java]  - Classe que oferece funções para detecção de colisão entre objetos no jogo
 * 
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 *   
 */


import gameutil2d.classes.scene.Scene;

public class Collision {
	
	public static boolean Check(GameElement obj1, GameElement obj2) {
		
		if( ((obj1.GetX() + obj1.GetWidth()) >= obj2.GetX()) &&
    			
	    		((obj1.GetX() <= (obj2.GetX() + obj2.GetWidth() )) &&
	    		
	    		((obj1.GetY() + obj1.GetHeight()) >= obj2.GetY()) &&
	        			
	    	    ((obj1.GetY() <= (obj2.GetY() + obj2.GetHeight() )))))		
		
		  return true;
		else
		  return false;	
				
		
	}
	
	
	public static boolean Check(GameElement obj, Scene scene, String any_object_with_tag)
	{
		boolean anyCollision = false;
		
		for(GameElement e : scene.Elements())
		{
			if(e.GetTag() == any_object_with_tag)
			{
				if(Check(obj, e)){
					anyCollision = true;
					break;
				}
			}
		}
		
		return anyCollision;
	}
	
	public static GameElement CheckAndReturn(GameElement obj, Scene scene, String any_object_with_tag)
	{
		GameElement hitObject = null;
		
		for(GameElement e : scene.Elements())
		{
			if(e.GetTag() == any_object_with_tag)
			{
				if(Check(obj, e)){
					hitObject = e;
					break;
				}
			}
		}
		
		return hitObject;
	}


}
