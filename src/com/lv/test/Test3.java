package com.lv.test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Test3 {

	/** 图片格式：JPG */
	private static final String PICTRUE_FORMATE_JPG = "jpg";
	
	public static void main(String[] args) {
		pressText("E:\36_69.jpg","淘播播","宋体",Font.BOLD,75,Color.black,77,78,79);
	}
	

	/**
	 * 69 * 添加文字水印 70 * 
	 * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg 71 * 
	 * @param pressText 水印文字， 如：中国证券网 72 * 
	 * @param fontName 字体名称， 如：宋体 73 * 
	 * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC) 74 * 
	 * @param fontSize 字体大小，单位为像素 75 * 
	 * @param color 字体颜色 76 * 
	 * @param x 水印文字距离目标图片左侧的偏移量，如果x<0,则在正中间 77 * 
	 * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间 78 * 
	 * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明) 79
	 */
	public static void pressText(String targetImg, String pressText,
			String fontName, int fontStyle, int fontSize, Color color, int x,
			int y, float alpha) {
		try {
			File file = new File(targetImg);

			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setColor(color);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));

			int width_1 = fontSize * getLength(pressText);
			int height_1 = fontSize;
			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}

			g.drawString(pressText, x, y + height_1);
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 118 * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符 119 * @param text 120 * @return
	 * 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4. 121
	 */
	public static int getLength(String text) {
		int textLength = text.length();
		int length = textLength;
		for (int i = 0; i < textLength; i++) {
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}
	
}
