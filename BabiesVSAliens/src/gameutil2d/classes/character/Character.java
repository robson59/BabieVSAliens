package gameutil2d.classes.character;

/*
 *  [Character.java]  - Classe destinada para a construção de personagens, voltado para jogos estilo plataforma
 *  Possui recursos de detecção de colisão, processamento de física (pulo e queda) e etc.
 * 
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 *  
 */

import java.util.ArrayList;



import android.content.Context;
import android.graphics.Canvas;

import gameutil2d.classes.basic.*;
import gameutil2d.classes.scene.*;

public class Character extends GameElement {
	
	
	boolean invert;
	
	AnimationSprites aCurrentAnimation;
	
	enum AnimationType { IDLE, MOVE, ATTACK, JUMPING, DAMAGED, DEAD }
	
	enum ActionState { IDLE, MOVE, ATTACK, DAMAGED, DEAD }
	
	enum DirectionState { RIGHT, LEFT }
	
	enum JumpState { JUMPING, FALLING, IS_GROUND }
	
	enum DamageState { NO_DAMAGE, DAMAGED }
	
	enum LiveState { LIVE, DYING, DEAD }
	
	ArrayList<String> aAnimationName;
	ArrayList <AnimationSprites> aAnimations;
	ArrayList <AnimationType> aAnimationType;
	
	ActionState action;
	
	DirectionState direction;
	
	JumpState jump;
	
	DamageState damageState;
	
	LiveState liveState;
	
	ArrayList<AnimationSprites> animationList;
	
	
	
	ArrayList<String> aCollisionElementBySide_Tag;

	ArrayList<String> aCollisionElementBySide_Type;
	
	ArrayList<String> aCollisionElementByFall_Tag;

	ArrayList<String> aCollisionElementByFall_Type;
	
	
	int INITIAL_VELOCITY_JUMP = -18;
	
	int MAX_VELOCITY_FALL = 12;
	
	int JumpShift;
	
	int MAX_FRAME_DAMAGE = 15;
	
	int countFrameDamage;
	
	boolean enableFall = true;
	
	Context context;
	
	
	int last_idle_animation_frame;
	int last_jumping_animation_frame;
	boolean last_loop_idle_animation;
	boolean last_loop_jumping_animation;
	
	String idle_animation_name_after_jumping,idle_animation_name_after_attack;
	
	public Character(Context context, int x, int y, int w, int h)
	{
		super.SetBounds(x, y, w, h);
		
		this.context = context;
		
		aAnimationName = new ArrayList<String>();		
		aAnimations = new ArrayList<AnimationSprites>();
		aAnimationType = new ArrayList<AnimationType>();
		
		aCollisionElementBySide_Tag =  new ArrayList<String>();
		aCollisionElementBySide_Type = new ArrayList<String>();
		
		aCollisionElementByFall_Tag =  new ArrayList<String>();
		aCollisionElementByFall_Type = new ArrayList<String>();
		
		direction = DirectionState.RIGHT;
		jump = JumpState.IS_GROUND;
		damageState = DamageState.NO_DAMAGE;
		
		invert = false;
		
		idle_animation_name_after_jumping = null;
		idle_animation_name_after_attack = null;
		last_loop_idle_animation = false;
		last_loop_jumping_animation = false;
	}
	
	private void AddNewSprite(String animation_name, int Id, AnimationType animationType)
	{
		//Primeiramente, será feito uma busca no array "aAnimationName" para checar
		//se a animação já existe
	    int index = aAnimationName.indexOf(animation_name);
		if(index != -1)
		{
			//Se existir, adiciona a imagem no array de imagem na posição referenciada por index
			aAnimations.get(index).Add(Id);
					
		}
		else {
			//Se a animação não existe, adiciona o nome da animação no vetor de nomes de animações
			aAnimationName.add(animation_name);
			//Cria no vetor de animação, uma nova animação, referenciada por  nome acima			
			AnimationSprites animation = new AnimationSprites(context,GetX(), GetY(), GetWidth(), GetHeight());
			animation.Add(Id);
			aAnimations.add(animation);
		    //Adiciona o tipo da animação
			aAnimationType.add(animationType);
		}
	}
	
	
	/*public void AddNewSpriteIdle(String animation_name, int Id)
	{
		AddNewSprite(animation_name, Id, AnimationType.IDLE);
	}*/
	
	public void AddNewSpriteIdle(String animation_name,int...Ids) 
	{
		for (int id : Ids)
		  AddNewSprite(animation_name, id, AnimationType.IDLE);
	}
	
	/*public void AddNewSpriteMove(String animation_name, int Id)
	{
		AddNewSprite(animation_name, Id, AnimationType.MOVE);
	}*/
	
	public void AddNewSpriteMove(String animation_name, int...Ids)
	{
		for (int id : Ids)
		   AddNewSprite(animation_name, id, AnimationType.MOVE);
	}
	
	/*public void AddNewSpriteJumping(String animation_name, int Id)
	{
		AddNewSprite(animation_name, Id, AnimationType.JUMPING);
	}*/
	
	public void AddNewSpriteJumping(String animation_name, int...Ids)
	{
		for (int id : Ids)
		  AddNewSprite(animation_name, id, AnimationType.JUMPING);
	}
	
	/*public void AddNewSpriteAttack(String animation_name, int Id)
	{
		AddNewSprite(animation_name, Id, AnimationType.ATTACK);
	}*/
	
	public void AddNewSpriteAttack(String animation_name, int...Ids)
	{
		for (int id : Ids)
		   AddNewSprite(animation_name, id, AnimationType.DAMAGED);
	}
	
	
	/*public void AddNewSpriteDamage(String animation_name, int Id)
	{
		AddNewSprite(animation_name, Id, AnimationType.DAMAGED);
	}*/
	
	public void AddNewSpriteDamage(String animation_name, int...Ids)
	{
		for (int id : Ids)
		   AddNewSprite(animation_name, id, AnimationType.DAMAGED);
	}
	
	/*public void AddNewSpriteDie(String animation_name,int Id)
    {
		AddNewSprite(animation_name, Id, AnimationType.DEAD);
    }*/
	
	public void AddNewSpriteDie(String animation_name,int...Ids)
    {
		for (int id : Ids)
		    AddNewSprite(animation_name, id, AnimationType.DEAD);
    }
	
	public void Idle(String animation_name, int frames, boolean loop  )
	{
		if(action != ActionState.IDLE) {
	     action = ActionState.IDLE;	
		 //Busca pelo vetor de nomes de animação, se existe a animação informada
	     int index = aAnimationName.indexOf(animation_name);
	     
	     if(index != -1)
	     {
	    	 last_idle_animation_frame = frames;
	    	 last_loop_idle_animation = loop;
	    	 
	    	 //Verifica se a animação encontrada é uma animação do tipo Idle
	    	 if(aAnimationType.get(index) == AnimationType.IDLE)
	    	 {
	    		aCurrentAnimation = aAnimations.get(index);
	    		//Executa a animação
	    		aCurrentAnimation.Start(frames, loop);
	    	 }
	     }
		}
	}
	
	public void Idle(int frames, boolean loop  )
	{
		if(action != ActionState.IDLE)  {
	     action = ActionState.IDLE;	
		 //Como não foi informado o nome da animação do personagem parado,
	     //exibir a primeira animação que representa o personagem parado
	     int index = aAnimationType.indexOf(AnimationType.IDLE);
	     
	     if(index != -1)
	     {
	        last_idle_animation_frame = frames;
	    	last_loop_idle_animation = loop;
	    	
	        aCurrentAnimation = aAnimations.get(index);
	        aCurrentAnimation.Start(frames, loop);
	     }
	     
	     
		}
	}
	
	private void ForceIdle(int frames, boolean loop)
	{
		action = ActionState.IDLE;	
		 //Como não foi informado o nome da animação do personagem parado,
	     //exibir a primeira animação que representa o personagem parado
	     int index = aAnimationType.indexOf(AnimationType.IDLE);
	     
	     if(index != -1)
	     {
	        last_idle_animation_frame = frames;
	    	last_loop_idle_animation = loop;
	    	
	        aCurrentAnimation = aAnimations.get(index);
	        aCurrentAnimation.Start(frames, loop);
	     }
	}
	
	public void MoveToRight(String animation_name, int frames, boolean loop ) 
	{
		if((direction != DirectionState.RIGHT) || (action != ActionState.MOVE))
		{
		  direction = DirectionState.RIGHT;
 		  action = ActionState.MOVE;
 		  
 		  int index = aAnimationName.indexOf(animation_name);
 		  
 		  if(index != -1)
 		  {
 			  aCurrentAnimation = aAnimations.get(index);
 			  aCurrentAnimation.Start(frames, loop);
 		  }
 		  
		}
		
	}
	
	
	public void MoveToRight(int frames, boolean loop )
	{
		if((direction != DirectionState.RIGHT) || (action != ActionState.MOVE))
		{
		  direction = DirectionState.RIGHT;
 		  action = ActionState.MOVE;
 		  
 		  int index = aAnimationType.indexOf(AnimationType.MOVE);
 		  
 		  if(index != -1)
 		  {
 			  aCurrentAnimation = aAnimations.get(index);
 			  aCurrentAnimation.Start(frames, loop);
 		  }
 		  
		}
		
	}
	

	
	public void MoveToLeft(String animation_name, int frames, boolean loop )
	{
		if((direction != DirectionState.LEFT) || (action != ActionState.MOVE))
		{
		  direction = DirectionState.LEFT;
 		  action = ActionState.MOVE;
 		  
 		  int index = aAnimationName.indexOf(animation_name);
 		  
 		  if(index != -1)
 		  {
 			  aCurrentAnimation = aAnimations.get(index);
 			  aCurrentAnimation.Start(frames, loop);
 		  }		  		  
		}
		
	}
	
	
	public void MoveToLeft(int frames, boolean loop )
	{
		if((direction != DirectionState.LEFT) || (action != ActionState.MOVE))
		{
		  direction = DirectionState.LEFT;
 		  action = ActionState.MOVE;
 		  
 		  int index = aAnimationType.indexOf(AnimationType.MOVE);
 		  
 		  if(index != -1)
 		  {
 			  aCurrentAnimation = aAnimations.get(index);
 			  aCurrentAnimation.Start(frames, loop);
 		  }		  		  
		}
		
	}

	
	public void Attack(String animation_name, int frames) {
		
		action = ActionState.ATTACK;
		
		 int index = aAnimationName.indexOf(animation_name);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  idle_animation_name_after_attack = null;
		  }		  
		
	}
	
    public void Attack(int frames) {
		
		action = ActionState.ATTACK;
		
		 int index = aAnimationType.indexOf(AnimationType.ATTACK);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  idle_animation_name_after_attack = null;
		  }		  
		
	}
    
   public void Attack(String animation_name, int frames,String idle_animation_name_after_attack ) {
		
		action = ActionState.ATTACK;
		
		 int index = aAnimations.indexOf(animation_name);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  this.idle_animation_name_after_attack = idle_animation_name_after_attack;
		  }		  
		
	}
    
   public void Attack(int frames,String idle_animation_name_after_attack ) {
		
		action = ActionState.ATTACK;
		
		 int index = aAnimationType.indexOf(AnimationType.ATTACK);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  this.idle_animation_name_after_attack = idle_animation_name_after_attack;
		  }		  
		
	}
	
	
    
    
    public void Jump(String animation_name, int frames, boolean loop)
    {
    	if(jump == JumpState.IS_GROUND)
    	{
    		jump = JumpState.JUMPING;
    		JumpShift = INITIAL_VELOCITY_JUMP;    		
    		
    		int index = aAnimationName.indexOf(animation_name);
  		  
  		    if(index != -1)
  		    {
  			  aCurrentAnimation = aAnimations.get(index);
  			  aCurrentAnimation.Start(frames, false);
  		    }		
    	}
    }
    
    public void Jump(int frames, boolean loop)
    {
    	if(jump == JumpState.IS_GROUND)
    	{
    		jump = JumpState.JUMPING;
    		JumpShift = INITIAL_VELOCITY_JUMP;    		
    		
    		int index = aAnimationType.indexOf(AnimationType.JUMPING);
  		  
  		    if(index != -1)
  		    {
  			  aCurrentAnimation = aAnimations.get(index);
  			  aCurrentAnimation.Start(frames, false);
  		    }		
    	}
    }
    
    public void Jump(String animation_name, int frames, int jump_shift, boolean loop)
    {
    	if(jump == JumpState.IS_GROUND)
    	{
    		jump = JumpState.JUMPING;
    		JumpShift = jump_shift;    		
    		
    		int index = aAnimationName.indexOf(animation_name);
  		  
  		    if(index != -1)
  		    { 
  			  aCurrentAnimation = aAnimations.get(index);
  			  aCurrentAnimation.Start(frames, false);
  		    }		
    	}
    }
    
    public void Jump(int frames, int jump_shift, boolean loop)
    {
    	if(jump == JumpState.IS_GROUND)
    	{
    		jump = JumpState.JUMPING;
    		JumpShift = jump_shift;    		
    		
    		int index = aAnimationType.indexOf(AnimationType.JUMPING);
  		  
  		    if(index != -1)
  		    {
  			  aCurrentAnimation = aAnimations.get(index);
  			  aCurrentAnimation.Start(frames, false);
  		    }		
    	}
    }
    
  
    
	

	@Override
	public void Draw(Canvas canvas) {
		
		if(damageState == DamageState.DAMAGED) {
			  
			
			countFrameDamage++;
			
			if(countFrameDamage != MAX_FRAME_DAMAGE)
			{
			   if((countFrameDamage % 2) == 0)
				  return;
			}
			else 
		    {
			  countFrameDamage = 0;
			  damageState = DamageState.NO_DAMAGE;
		    }
		}
		
		if (!invert) {
			if (direction == DirectionState.RIGHT)
				aCurrentAnimation.Draw(canvas);
			else
				aCurrentAnimation.Draw(canvas, FlipEffect.HORIZONTAL);

		}
		else {
			
			if (direction == DirectionState.RIGHT)
				aCurrentAnimation.Draw(canvas,FlipEffect.HORIZONTAL);
			else
				aCurrentAnimation.Draw(canvas);
			
		}
	}
	
	
	public void Update(Scene scene) {
		
		
		boolean isGround = false;
		
		
		
		if(action == ActionState.ATTACK)
		{
			if(!aCurrentAnimation.IsPlaying())
			{
			   action = ActionState.IDLE;
			   
			   if(jump == JumpState.IS_GROUND)
			   {
				   int index;
				   
				   if(idle_animation_name_after_attack != null)
				   {
				     index = aAnimationName.indexOf(idle_animation_name_after_attack);
				     aCurrentAnimation = aAnimations.get(index);
					 aCurrentAnimation.Start(last_idle_animation_frame, last_loop_idle_animation);
				     
				   }
				   else {
					 index = aAnimationType.indexOf(AnimationType.IDLE);
					 aCurrentAnimation = aAnimations.get(index);
					 aCurrentAnimation.Start(last_idle_animation_frame, last_loop_idle_animation);
				   }
				   
				  
			   }
			   else { //Está caindo ???
				   
				   int index = aAnimationType.indexOf(AnimationType.JUMPING);
				   aCurrentAnimation = aAnimations.get(index);
				   aCurrentAnimation.Start(last_jumping_animation_frame, last_loop_jumping_animation);
				   
			   }
				   
			}
			  
			
			
		} 
		
		else if(action == ActionState.DAMAGED)
		{
			if(!aCurrentAnimation.IsPlaying())
			{
				action = ActionState.IDLE;
				
				if(jump == JumpState.IS_GROUND)
				   {
					   int index;
					   
					   if(idle_animation_name_after_attack != null)
					     index = aAnimationName.indexOf(idle_animation_name_after_attack);
					   else
						 index = aAnimationType.indexOf(AnimationType.IDLE);
					   
					   aCurrentAnimation = aAnimations.get(index);
					   aCurrentAnimation.Start(last_idle_animation_frame, last_loop_idle_animation);
				   }
				   else { //Está caindo ???
					   
					   int index = aAnimationType.indexOf(AnimationType.JUMPING);
					   aCurrentAnimation = aAnimations.get(index);
					   aCurrentAnimation.Start(last_jumping_animation_frame, last_loop_jumping_animation);
					   
				   }
			}
		}
		else if(action == ActionState.DEAD) {
			
			if(aCurrentAnimation.IsPlaying())
			{
				liveState = LiveState.DYING;
			} else {
				liveState = LiveState.DEAD;
			}
			
		}
		   
		if(!enableFall)
			return; //Sai, não será processado nenhuma queda
		   
		   if(jump == JumpState.JUMPING) {
			   
			   
			   this.MoveByY(JumpShift);
			   
			 
			   JumpShift++;
			   
			   if(JumpShift == 0) {
				  jump = JumpState.FALLING;
				   
			   }
		   } else if(jump == JumpState.FALLING) {
			   
	           this.MoveByY(JumpShift);
	           
	           JumpShift++;
	           
	           if(JumpShift > MAX_VELOCITY_FALL)
	        	   JumpShift--;
			   
			  
			   
			   //Processa todos os elementos da tela para ver se colidiu com o alguma coisa
			   for(GameElement element : scene.Elements()) {
				   
				   boolean colidiu = false;
				   
				   if( (IsCollisionElementByFall(element)) || (IsCollisionElementBySide(element)))  {
				      
					   
					   //Checa a colisao entre os objetos
					   if(Collision.Check(this, element)) {
						   
						  
						   
						   if ( (this.GetY() + this.GetHeight()) <= (element.GetY() + 15)) {
							   
							   
							   
							   jump = JumpState.IS_GROUND;
							   
							   
							   
							   this.SetY(element.GetY() - (this.GetHeight()));
							   
							   colidiu = true;
							   
						   }
						   
					   }
				   
				   }
				   
				   if(colidiu)
				   {					
					 ForceIdle(last_idle_animation_frame, last_loop_idle_animation);  
				     break;
				   }
				   
			   }
			   
			   
		   } else if(jump == JumpState.IS_GROUND) {
			   
			   for(GameElement element : scene.Elements()) {
				   
				   if( (IsCollisionElementByFall(element)) || (IsCollisionElementBySide(element)))  {
					   
					  
					   if(   
							   
							   
							   ((this.GetY() + (this.GetHeight())) == (element.GetY())) &&
								 
							    ((this.GetX() + (this.GetWidth())) >= element.GetX() ) && 
								
								((this.GetX()) <= (element.GetX() + element.GetWidth() ) ) ) {
						   
						   isGround = true;
						   
						   break;
					   }
					   
					   
				   }
				   
				   
			   }
			   
			   if(!isGround)
			   {
				   Jump(last_jumping_animation_frame,last_loop_jumping_animation);
				   jump = JumpState.FALLING;		   
				   JumpShift = 0;
				  
			   }
			   
		   }

		
		
	}
	
	public boolean IsAttacking() {
		return (action == ActionState.ATTACK);
	}
	
	public boolean IsDamaged()
	{
		return (damageState == DamageState.DAMAGED);
	}
	
	
	public boolean IsDying()
	{
		return (liveState == LiveState.DYING);
	}
	
	public boolean IsDead()
	{
		return (liveState == LiveState.DEAD);
	}
	
	public boolean IsGround()
	{
		return (jump == JumpState.IS_GROUND);
	}
	
	
	public void SufferDamage(String animation_name, int frames) {
		countFrameDamage = 0;
		damageState = DamageState.DAMAGED;
		action = ActionState.DAMAGED;
		
		int index = aAnimationName.indexOf(animation_name);
		
		if(index != -1)
		{
			AnimationType atype = aAnimationType.get(index);
			if(atype == AnimationType.DAMAGED)
			{
			    aCurrentAnimation = aAnimations.get(index);
			    aCurrentAnimation.Start(frames, false);
			}
		}
	}
	
	public void SufferDamage(int frames) {
		countFrameDamage = 0;
		damageState = DamageState.DAMAGED;
		action = ActionState.DAMAGED;
		
		int index = aAnimationType.indexOf(AnimationType.DAMAGED);
		
		if(index != -1)
		{
			if(aAnimationType.get(index) == AnimationType.DAMAGED)
			{
			    aCurrentAnimation = aAnimations.get(index);
			    aCurrentAnimation.Start(frames, false);
			}
		}
	}
	
	
   public void Die(String animation_name, int frames) {
		
		action = ActionState.DEAD;
		
		 int index = aAnimationName.indexOf(animation_name);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  idle_animation_name_after_attack = null;
		  }		  
		
	}
	
    public void Die(int frames) {
		
		action = ActionState.DEAD;
		
		 int index = aAnimationType.indexOf(AnimationType.DEAD);
		  
		  if(index != -1)
		  {
			  aCurrentAnimation = aAnimations.get(index);
			  aCurrentAnimation.Start(frames, false);
			  idle_animation_name_after_attack = null;
		  }		  
		
	}
	
	public void SetMaxFramesDamageDuration(int max_frames)
	{
		MAX_FRAME_DAMAGE = max_frames;
	}
	
	
	public void AddCollisionElementOfFallByTag(String tag) {
		aCollisionElementByFall_Tag.add(tag);
	}
	
	public void AddCollisionElementOfSideByTag(String tag) {
		aCollisionElementBySide_Tag.add(tag);
	}
	
	

	private boolean IsCollisionElementByFall(GameElement element) {
		
		boolean isElement = false;
		
		for(String type : aCollisionElementByFall_Type)
		{
			
			if(element.getClass().getSimpleName().trim().equals(type.trim())) {
				isElement = true;
			    break;
			}
		}
			
		if(isElement)
			return true;
		else
		
		{
			
			for(String tag : aCollisionElementByFall_Tag)
			{
				if(element.GetTag() == tag) {
					isElement = true;
					
				    break;	
				}
			}
			
			if(isElement)
				return true;
							
		}
		
		return isElement;
		
	}
	
	
  private boolean IsCollisionElementBySide(GameElement element) {
		
		boolean isElement = false;
		
		/*for(String type : aCollisionElementBySide_Type)
		{
			
			if(element.getClass().getSimpleName().trim().equals(type.trim()))
				isElement = true;
		}
			
		if(isElement)
			return true;
		else*/
		
		{
			
			for(String tag : aCollisionElementBySide_Tag)
			{
				if(element.GetTag() == tag)
					isElement = true;
			}
			
			if(isElement)
				return true;
			
				
		}
		
		return isElement;
		
	}
	
    
  public void MoveByX(int value) {
	 
	 
	 super.MoveByX(value);
	 
	 for(AnimationSprites a : aAnimations)
		 a.MoveByX(value);
	 
  }
 
  public void MoveByY(int value) {
	 
	 
	 super.MoveByY(value);
	 
	 for(AnimationSprites a : aAnimations)
		 a.MoveByY(value);
	 
  }
  
   public void SetX(int value) {
		 
		 
		 super.SetX(value);
		 
		 for(AnimationSprites a : aAnimations)
			 a.SetX(value);
		 
   }
   
   public void SetY(int value) {
		 
		 
		 super.SetY(value);
		 
		 for(AnimationSprites a : aAnimations)
			 a.SetY(value);
		 
   }
   
   public void SetWidth(int value) {
		 
		 
		 super.SetWidth(value);
		 
		 for(AnimationSprites a : aAnimations)
			 a.SetWidth(value);
		 
  }
   
   public void SetHeight(int value) {
		 
		 
		 super.SetHeight(value);
		 
		 for(AnimationSprites a : aAnimations)
			 a.SetHeight(value);
		 
  }
   
   
   public boolean CollisionBySide(Scene scene)
   {
	   boolean anyCollision = false;
	   
	   for(GameElement e : scene.Elements())
	   {
		   if(IsCollisionElementBySide(e))
		   {
			 if((Collision.Check(this, e)) && ((this.y + this.height) >= e.GetY() + 10))
			 {
			   anyCollision = true;
			   break;
			 }
		   }
	   }
	   
	   return anyCollision;
   }
   
   public void SetBounds(int x, int y, int w, int h) {
		 
		 SetY(x);
		 SetY(y);
		 SetWidth(w);
		 SetHeight(h);
		 
  }

   public void SetEnableJumpAndFall(boolean jump_fall)
   {
	   enableFall = jump_fall;
   }
   
   public void TurnToLeft()
   {
	   direction = DirectionState.LEFT;
   }
   
   public void TurnToRight()
   {
	   direction = DirectionState.RIGHT;
   }
   
   public void InvertSprites()
   {
	   invert = !invert;   
   }
   

}
