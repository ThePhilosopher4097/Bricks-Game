import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class Brick extends Applet implements MouseListener
{
    boolean go=false,reset=false,Lnav=false,Tnav=false,SB=false,BS=false,start=false;
    int b1=620,b2=560;
    int left,top,move=600,key,cnt=0,newx,newy,move2;
    Button Play,Pause,Restart;
    AudioClip aud;
    Collision CC=new Collision();
    Collision CB[]=new Collision[20];
    public void init()
    {
        
        CC.Coll(CB);
        addMouseListener(this);   
    }
    
    class Collision
    {
        int T,L,B,R,C;
        Collision(int l,int t,int r,int b)
        {
            T=t;    L=l;    R=r;    B=b;    C=0;
        }
        Collision(){}
        public void Coll(Collision CB[])
        {
            int l=300,t=100,r=370,b=150;
            for(int i=0;i<19;i++)
            {   CB[i]=new Collision(l,t,r,b);
                if(i==7)
                {
                    l=300; r=370; t+=100; b+=100; 
                }
                if(i==13)
                {
                    l=440; r=510; t+=100; b+=100; 
                }
                else
                {
                    l+=90; r+=90;
                }    
            }
        }
        public void detect(Collision CB[])
        {   int i=0;
            for(i=0;i<19;i++)
            {  
                if((b1+30==CB[i].L) && b2<=CB[i].B && b2>=CB[i].T)  {Lnav=false; CB[i].C=1; CB[i].L=0; CB[i].T=0; CB[i].R=0; CB[i].B=0; break;}
                if(b1==CB[i].R && b2<=CB[i].B && b2>=CB[i].T) {Lnav=true; CB[i].C=1; CB[i].L=0; CB[i].T=0; CB[i].R=0; CB[i].B=0; break;}
                if(b2==CB[i].B && b1+30>=CB[i].L && b1<=CB[i].R) {Tnav=true; CB[i].C=1; CB[i].L=0; CB[i].T=0; CB[i].R=0; CB[i].B=0; break;}
                if(b2+31==CB[i].T && b1+30>=CB[i].L && b1<=CB[i].R) {Tnav=false; CB[i].C=1; CB[i].L=0; CB[i].T=0; CB[i].R=0; CB[i].B=0; break;}
            }
        }
    }

    public void update()
    {   
            if(b1<=50)  Lnav=true;
            if(b2<=50)  Tnav=true;
            if(b1>=1250)    Lnav=false;    
            //if(b2>=550)    Tnav=false;   
            CC.detect(CB);
            if(Lnav==true)  b1+=1;  else    b1-=1;          //set value x
            if(Tnav==true)  b2+=1;  else    b2-=1;          //set value y
            if(b1>=(move-20) && b1<=(move+70))
            {
                if (b2>549 && b2<590)
                {   Tnav=false;
                }
            }   
    }
    public void paint(Graphics g)   
    {   
        endgame :
        {
        top=100;left=300;   
        int j=40,k=255,th,tv;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(50,50,1250,550,20,20);
        g.setColor(Color.GREEN);
        g.fillRoundRect(600,40,40,40,20,20);
        //g.setFont(new Font("TimesNewRoman", Font.BOLD,20));
        //g.setColor(Color.BLACK);
        //g.drawString("P",618,65);
        g.setColor(Color.RED);
        g.fillRoundRect(650,40,40,40,20,20);
        //g.setColor(Color.BLACK);
        //g.drawString("S",668,65);
        g.setColor(Color.CYAN);
        g.fillRoundRect(700,40,40,40,20,20);
        //g.setColor(Color.BLACK);
        //g.drawString("X",718,65);
        for(int i=0;i<19;i++)
        {   /*if(CB[i].L==0 && CB[i].R==0 && CB[i].T==0 && CB[i].B==0)*/
            if(CB[i].C==1)    cnt=0;  else { cnt=1; break;}}  
        if(cnt==0)  
        {   
            setBackground(Color.BLUE);
            setForeground(Color.LIGHT_GRAY);
            g.setFont(new Font("Courier New", Font.BOLD,200));
            g.setColor(Color.GREEN);
            g.drawString("CONGRATS...YOU WIN !",150,400);
            go=false;   SB=false;
            break endgame;
        }
        for(int i=0;i<19;i++)
        {  
            Color c=new Color(255-i-5,j,k);
            j+=10;  k-=10;  
            if(CB[i].C==0)
            {g.setColor(c);  g.fillRect(left,top,70,50);}
            if(i==7)    {   top+=100; left=300;  }
            if(i==13)    {   top+=100; left=440; }
            else    left+=90; 
        }
        if(reset==true)
        {
            setBackground(Color.WHITE);
            setForeground(Color.LIGHT_GRAY);
        }
            if(b2>=590)                 //Dropped
            {
                setBackground(Color.BLUE);
                setForeground(Color.LIGHT_GRAY);
                g.setFont(new Font("Courier New", Font.BOLD,200));
                g.setColor(Color.RED);
                g.drawString("GAME OVER",150,400);
                go=false;   SB=false; 
            }
        
        if(move+80<=50)  {   move=1249;    g.setColor(Color.GREEN);    g.fillRoundRect(move,600,80,40,20,20);  }
        if(move>=1250)  {   move=51;    g.setColor(Color.GREEN);    g.fillRoundRect(move,600,80,40,20,20);  }  //rollback
        g.setColor(Color.RED);      g.fillOval(b1,b2,30,30);              //Ball
        if(SB==true)    {   g.setColor(Color.GREEN);    g.fillRoundRect(move,600,80,40,20,20); }     //User's Sliding Brick
        else if(BS==true)   {   g.setColor(Color.GREEN);    g.fillRoundRect(move2,600,80,40,20,20); }   //reset                                                           
        else    {   g.setColor(Color.GREEN);    g.fillRoundRect(600,600,80,40,20,20); }
        if(go==true)            //play
        {
           repaint();
            try
            {   
                g.setColor(Color.RED);          g.fillOval(b1,b2,30,30);
                Thread.sleep(5);
                update();
            }catch(Exception ie){}
        }
        repaint();
        }
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e)
    {
        newx=e.getX();
        newy=e.getY();
        if ((newx<600 && newy>80) || key==97)
            move-=50;
        else if(newx>740 && newy>80 || key==100)
            move+=50;   
        else    { move=move; } 
        repaint();
    }
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e)
    {   //go=true;
        if(newx>=600 && newx<=640 && newy>=40 && newy<=80)  {go=true; reset=false;  if(start==false) Tnav=false; SB=true; BS=false; start=true;}
        if(newx>=650 && newx<=690 && newy>=40 && newy<=80)  {go=false; SB=false;  move2=move; BS=true;} 
        if(newx>=700 && newx<=740 && newy>=40 && newy<=80)  {SB=false; BS=false; move=600;   Tnav=false; reset=true; go=false; b1=620; b2=549;  for(int i=0;i<19;i++)   CB[i].C=0; CC.Coll(CB);}
        repaint();
    }
} 
/*
Play=new Button("Play");
        Play.setBackground(Color.GREEN);
        Pause=new Button("Pause");
        Pause.setBackground(Color.ORANGE);
        Restart=new Button("Restart");
        Restart.setBackground(Color.BLUE);
        add(Play);
        add(Pause);
        add(Restart);
 //addKeyListener(this);
        //Play.addActionListener(this);
        //Pause.addActionListener(this);
        //Restart.addActionListener(this);

public void ActionJackson()
    {
        if(ae.getSource()==Play)
        {   Color c1=Play.getBackground()==Color.CYAN?Color.GREEN:Color.CYAN;
            Play.setBackground(c1); 
            go=true;
            repaint();
        }
        if(ae.getSource()==Pause)
        {
            Color c2=Pause.getBackground()==Color.ORANGE?Color.RED:Color.ORANGE;
            Pause.setBackground(c2); 
            go=false;
            repaint();
        }
        if(ae.getSource()==Restart)
        {
            Color c3=Restart.getBackground()==Color.PINK?Color.MAGENTA:Color.PINK;
            Restart.setBackground(c3); 
            reset=true;
            b1=620;    b2=549;
            go=false; 
            repaint();
        }
    }
*/