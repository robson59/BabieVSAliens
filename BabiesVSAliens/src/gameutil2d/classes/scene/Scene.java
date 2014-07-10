package gameutil2d.classes.scene;

/*
 *  [Scene.java]  - Classe que serve para a construir  cenários de um jogo
 *  Ela funciona como um ArrayList que armazena todos os elementos que serão visualizados no jogo
 * 
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 *  
 */

import gameutil2d.classes.basic.*;


import java.util.ArrayList;

import android.graphics.Canvas;


public class Scene extends GameElement{
	
	ArrayList<GameElement> tela;
	
	public Scene() {
		tela = new ArrayList<GameElement>();
	}
	
	public void Add(GameElement element) {
		tela.add(element);
	}
	
	public GameElement Get(int index) {
		return tela.get(index);
	}
	
	public ArrayList<GameElement> Elements() {
		return tela;
	}
	
	public void Remove(int index) {
		tela.remove(index);
	}
	
	public void Remove(GameElement element) {
		tela.remove(element);
	}
	
	public int GetCount() {
		
		return tela.size();
		
	}
	
	
	public int GetCountByType(String type) {
		
		int c = 0;
		
		for(GameElement e : tela)
		{
			if(e.getClass().getSimpleName() == type)
				c++;
		}
		
		return c;
		
	}
	
     public int GetCountByTag(String tag) {
		
		int c = 0;
		
		for(GameElement e : tela)
		{
			if(e.GetTag() == tag)
				c++;
		}
		
		return c;
			
	}

	@Override
	public void Draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for(GameElement e : tela)
			e.Draw(canvas);
	}
	
	public void MoveByX(int value)
	{
		for(GameElement element : tela)
		{
			element.MoveByX(value);
		}
	}
	
	public void MoveByY(int value)
	{
		for(GameElement element : tela)
		{
			element.MoveByY(value);
		}
	}
	
	

}
