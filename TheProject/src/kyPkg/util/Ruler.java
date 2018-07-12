package kyPkg.util;
import java.awt.Graphics;
import java.awt.Color;
/*
	égópó·ÅÑ
		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,null);
*/
public class Ruler {
	Ruler(){
	}
	public static void drawRuler(Graphics g,int xw,int xh,Color pBc){
		if (pBc==null) pBc = Color.WHITE;
		g.setColor(new Color(pBc.getRed(),pBc.getGreen() ,pBc.getBlue(),100));
//		g.setColor(pBc);
		g.fillRect(0,0,xw,xh);
		g.setColor(Color.white);
		g.drawRect(0,0,xw,xh);
		for(int j=0;j< xh;j+=10){
			if ((j % 100) == 0)	g.setColor(Color.red);
			else if ((j % 50) == 0) g.setColor(Color.yellow);
			else	g.setColor(Color.lightGray);
			g.drawLine(0,j,xw,j);
		}
		for(int i=0;i< xw;i+=10){
			if ((i % 100) == 0)	g.setColor(Color.red);
			else if ((i % 50) == 0) g.setColor(Color.yellow);
			else	g.setColor(Color.lightGray);
			g.drawLine(i,0,i,xh);
		}
	}
	public static void paintResct(Graphics g,int xw,int xh,Color pBc){
		if (pBc==null) pBc = Color.WHITE;
//		g.setColor(new Color(pBc.getRed(),pBc.getGreen() ,pBc.getBlue(),100));
		g.setColor(pBc);
//		g.fillRect(0,0,xw,xh);
		g.fillRoundRect(0, 0, xw, xh, 10, 10);
		g.setColor(Color.white);
//		g.drawRect(0,0,xw,xh);
		g.drawRoundRect(0, 0, xw, xh, 10, 10);
	}
	//	Color rndColor = new Color((int)Math.floor(Math.random() * 256),
	//							   (int)Math.floor(Math.random() * 256),
	//							   (int)Math.floor(Math.random() * 256));
	//	g.setColor(rndColor);
}
