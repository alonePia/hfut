package com.lv.test;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


public class Pdf {

	 /** 
	    * 给pdf文件添加水印 
	    * @param InPdfFile 要加水印的原pdf文件路径 
	    * @param outPdfFile 加了水印后要输出的路径 
	    * @param markImagePath 水印图片路径 
	    * @param pageSize 原pdf文件的总页数（该方法是我当初将数据导入excel中然后再转换成pdf所以我这里的值是用excel的行数计算出来的，如果不是我这种可以 直接用reader.getNumberOfPages()获取pdf的总页数） 
	    * @throws Exception 
	    */  
	   public static void addPdfMark(String InPdfFile, String outPdfFile, String markImagePath) throws Exception {  
	      
	    PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());  
	       
	    PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outPdfFile));  
	     
	    Image img = Image.getInstance(markImagePath);// 插入水印     
	   
	    img.setAbsolutePosition(-10, -110);//水印图片的位置
	      
	    for(int i = 1; i <= reader.getNumberOfPages(); i++) {  
	       
	     PdfContentByte under = stamp.getUnderContent(i);  
	      
	     under.addImage(img);  
	         
	    }  
	      
	    stamp.close();// 关闭   
	      
	    File tempfile = new File(InPdfFile);  
	      
	    if(tempfile.exists()) {  
	       
	     tempfile.delete();  
	    }  
	      
	   }  
	
	   public static void main(String[] args) {
		   try {
			addPdfMark("E:\\1.pdf","E:\\2222.pdf","E:\\watermark.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	   
}
