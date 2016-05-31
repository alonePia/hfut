package com.lv.test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Test {
	/** 
	    * 给图片添加水印 
	  * @param filePath 需要添加水印的图片的路径 
	  * @param markContent 水印的文字 
	  * @param markContentColor 水印文字的颜色 
	  * @param qualNum 图片质量 
	  * @param wartmark 水印图片滴路径
	  * @return 
	  */ 
	  public boolean createMark(String filePath,String markContent,Color markContentColor,float qualNum,String watermark) 
	  { 
	    ImageIcon imgIcon=new ImageIcon(filePath); 
	    Image theImg =imgIcon.getImage(); 
	    int width=theImg.getWidth(null); 
	    int height= theImg.getHeight(null); 
	    //ImageIcon waterIcon=new ImageIcon(watermark);  
	    // Image waterImg =waterIcon.getImage();  
	    BufferedImage bimage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); 
	    Graphics2D g=bimage.createGraphics(); 
	    g.setColor(markContentColor); 
	    g.setBackground(Color.white); 
	    g.drawImage(theImg, 0, 0, null ); 
	    // g.drawImage(waterImg, width*2, height, null );  
	    g.drawString(markContent,width-300,height-130); //添加水印的文字和设置水印文字出现的内容 
	    g.dispose(); 
	    try{ 
	    FileOutputStream out=new FileOutputStream(filePath); 
	    JPEGImageEncoder encoder =JPEGCodec.createJPEGEncoder(out); 
	    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage); 
	    param.setQuality(qualNum, true); 
	    encoder.encode(bimage, param); 
	    out.close(); 
	    }catch(Exception e) 
	    { return false; } 
	    return true; 
	  } 
	  public static void main(String arg[]){ 
		  Test wk=new Test(); 
	    if(wk.createMark("E:\\36_69.jpg","淘播播",Color.RED,70f,"")){ 
	      System.out.println("制作成功"); 
	    }else{ 
	       System.out.println("我失败了！"); 
	    } 
	  } 
	
}
