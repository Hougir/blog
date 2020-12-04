package com.yellowhao.util;

public class QrCodeTest {
 
	public static void main(String[] args) throws Exception {
		// 存放在二维码中的内容
		//支付宝
		//String text = "https://qr.alipay.com/fkx09500ajfbz0eeoep5c89";
		//微信支付
		//String text = "wxp://f2f0hSOJJFp7llZNfUkIHjq3b3IAk0hdpmRs";
		//公众号
		String text = "http://weixin.qq.com/r/WCqVjb7Ey_O0rfru939K";
		// 嵌入二维码的图片路径
		String imgPath = "D:\\ideaWork\\blog\\yellowhao_blog\\src\\main\\resources\\static\\images\\avatar.png";
		// 生成的二维码的路径及名称
		String destPath = "D:/images/public_no_wechat.jpg";
		//生成二维码
		QRCodeUtil.encode(text, imgPath, destPath, true);
		// 解析二维码
		String str = QRCodeUtil.decode(destPath);
		// 打印出解析出的内容
		System.out.println(str);
 
	}
 
}