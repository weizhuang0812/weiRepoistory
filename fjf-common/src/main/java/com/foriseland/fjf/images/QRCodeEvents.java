package com.foriseland.fjf.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成器
 * @author wangHiayang
 *
 */
public class QRCodeEvents {
	
	private int qrcodeWidth = 200;
    private int qrcodeHeight = 200;
    private String outFilePath = "D:\\"; // 文件輸出地址
    private String qrcodeType = "png";
    private static final int BLACK = 0xff000000;  
    private static final int WHITE = 0xFFFFFFFF;
    
    public QRCodeEvents(int qrcodeWidth,int qrcodeHeight){
    	this.qrcodeWidth = qrcodeWidth;
    	this.qrcodeHeight = qrcodeHeight;
    }
    
    /**
     * 
     * @param qrcodeWidth  二维码图片 - 宽度
     * @param qrcodeHeight 二维码图片 - 高度
     * @param outFilePath  二维码图片 - 输出目录
     * @param qrcodeType   二维码图片 - 图片的文件类型(默认png)
     */
    public QRCodeEvents(int qrcodeWidth,int qrcodeHeight,String outFilePath,String qrcodeType){
    	this.qrcodeWidth = qrcodeWidth;
    	this.qrcodeHeight = qrcodeHeight;
    	this.outFilePath = outFilePath;
    	this.qrcodeType = qrcodeType;
    }
	
    /**
     * createQrcode 生成二维码
     * @param qrText 二维码文件内容
     * @param fileName 生成二维码文件名
     * @return
     */
	@SuppressWarnings("deprecation")
	public String createQrcode(String qrText,String fileName){
        String qrcodeFilePath = "";
        try {
            HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");//"http://www.cnblogs.com/java-class/"
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
            BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < qrcodeWidth; x++) {  
                for (int y = 0; y < qrcodeHeight; y++) {  
                    image.setRGB(x, y, bitMatrix.get(x, y) == true ? BLACK : WHITE);  
                }  
            } 
            //            Random random = new Random();
            File QrcodeFile = new File(outFilePath+fileName+"."+qrcodeType);
            ImageIO.write(image, qrcodeType, QrcodeFile);
            MatrixToImageWriter.writeToFile(bitMatrix, qrcodeType, QrcodeFile);
            qrcodeFilePath = QrcodeFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrcodeFilePath;
    }
//	
//	public void s2(String qrText ) throws Exception{
////		String text = String.valueOf(System.currentTimeMillis())+"測試二維碼";
//		String format = "png";
//		Hashtable hints = new Hashtable();
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
//		File outputFile = new File("D:\\new.png");
//		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
//	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		String text = String.valueOf(System.currentTimeMillis())+"測試二維碼";
//		createQrcode(text);
		QRCodeEvents event = new QRCodeEvents(200,200);
		event.createQrcode("make java", "newqr");
	}
}
