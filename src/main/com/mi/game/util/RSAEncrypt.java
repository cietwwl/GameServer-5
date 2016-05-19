package com.mi.game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncrypt {

	/**
	 * 私钥
	 */
	private RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	private RSAPublicKey publicKey;

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = {
			'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
	};

	/**
	 * 获取私钥
	 * 
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	public static String sign(String content, String privateKey) throws Exception {
		String charset = "UTF-8";
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);
		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
		signature.initSign(priKey);
		signature.update(content.getBytes(charset));
		byte[] signed = signature.sign();
		return new String(Base64Coder.encode(signed));
	}

	public static boolean doCheck(String content, String sign, String publicKey) throws Exception {
		String charset = "UTF-8";
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
		signature.initVerify(pubKey);
		signature.update(content.getBytes(charset));
		boolean bverify = signature.verify(Base64.decode(sign));
		return bverify;

	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void loadPublicKey(String publicKeyStr) throws Exception {
		// System.out.println("publicKeyStr:"+ publicKeyStr);
		try {
			byte[] buffer = Base64.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public void loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public void loadPrivateKey(String privateKeyStr) throws Exception {
		// System.out.println("privateKeyStr:"+ privateKeyStr);
		try {
			byte[] buffer = Base64.decode(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 公钥加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥加密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 公钥解密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 解密算法
	 * 
	 * @param cryptograph
	 *            :密文,带","
	 * @param d
	 *            私钥
	 * @param n
	 * @param numBit
	 *            位数
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String cryptograph, BigInteger d, BigInteger n, int numBit) throws Exception {
		String[] chs = cryptograph.split(" ");
		if (chs == null || chs.length <= 0) {
			throw new Exception("密文不符合要求!!!");
		}
		int numeroToken = chs.length;
		BigInteger[] StringToByte = new BigInteger[numeroToken];
		for (int i = 0; i < numeroToken; i++) {
			StringToByte[i] = new BigInteger(chs[i], 16);
		}
		byte[][] encodeM = dencodeRSA(StringToByte, d, n);
		byte[] sendMessage = StringToByte(encodeM);
		String message = new String(sendMessage, "UTF-8");
		String result = URLDecoder.decode(message, "UTF-8");
		return result;
	}

	/**
	 * <font color="red"> 解密算法(如果使用了产生密钥功能,则需要同步使用此方法解密)</font>
	 * 
	 * @param cryptograph
	 *            :密文,带","
	 * @param d
	 *            私钥
	 * @param n
	 *            modkey
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String cryptograph, BigInteger d, BigInteger n) throws Exception {
		return decrypt(cryptograph, d, n, 128);
	}

	/**
	 * 
	 * 将二维数组转化为一维数组
	 * 
	 * @param arraySenS
	 * @return
	 */
	private static byte[] StringToByte(byte[][] arraySenS) {
		int i, dab = 0;
		for (i = 0; i < arraySenS.length; i++) {
			if (arraySenS[i] == null) {
				return null;
			}
			dab = dab + arraySenS[i].length;
		}
		List<Byte> listByte = new ArrayList<Byte>();
		int j;
		for (i = 0; i < arraySenS.length; i++) {
			for (j = 0; j < arraySenS[i].length; j++) {
				if (arraySenS[i][j] != ' ') {
					listByte.add(arraySenS[i][j]);
				}
			}
		}
		Byte[] arrayByte = listByte.toArray(new Byte[0]);
		byte[] result = new byte[arrayByte.length];
		for (int k = 0; k < arrayByte.length; k++) {
			result[k] = (arrayByte[k]).byteValue();
		}
		listByte = null;
		arrayByte = null;
		return result;
	}

	/**
	 * 对密文进行解密，通过公式 明文 = （密文（d次幂）mod m）
	 * 
	 * @param 密文encodeM
	 *            不为空
	 * @param 密钥d
	 * @param 模数n
	 * @return 解密后的明文dencodeM
	 */
	private static byte[][] dencodeRSA(BigInteger[] encodeM, BigInteger d, BigInteger n) {
		byte[][] dencodeM = new byte[encodeM.length][];
		int i;
		int lung = encodeM.length;
		for (i = 0; i < lung; i++) {
			dencodeM[i] = encodeM[i].modPow(d, n).toByteArray();
		}
		return dencodeM;
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	public static String encrypt(String source, BigInteger e, BigInteger n) throws Exception {
		return encrypt(source, e, n, 128);
	}

	public static String encrypt(String source, BigInteger e, BigInteger n, int numBit) throws Exception {
		// 为了支持汉字、汉字和英文混排
		String text = URLEncoder.encode(source, "UTF-8");
		if (text == null || "".equals(text)) {
			throw new Exception("明文转换为UTF-8,导致转换异常!!!");
		}
		byte[] arraySendM = text.getBytes("UTF-8");
		if (arraySendM == null) {
			throw new Exception("明文转换为UTF-8,导致转换异常!!!");
		}
		if (numBit <= 1) {
			throw new Exception("随机数位数不能少于2!!!");
		}
		int numeroByte = (numBit - 1) / 8;
		byte[][] encodSendM = byteToEm(arraySendM, numeroByte);
		BigInteger[] encodingM = encodeRSA(encodSendM, e, n);
		StringBuilder encondSm = new StringBuilder();
		for (BigInteger em : encodingM) {
			encondSm.append(em.toString(16));
			encondSm.append(" ");
		}
		return encondSm.toString();
	}

	/**
	 * 将数组byte[]arrayByte,转化为二维数组,分段加密/解密
	 * 
	 * @param arrayByte
	 * @param numBytes
	 * @return arrayEm 不会为空
	 */
	private static byte[][] byteToEm(byte[] arrayByte, int numBytes) {
		/**
		 * 分段
		 */
		int total = arrayByte.length;
		int dab = (total - 1) / numBytes + 1, iab = 0;
		byte[][] arrayEm = new byte[dab][];
		int i, j;
		for (i = 0; i < dab; i++) {
			arrayEm[i] = new byte[numBytes];

			for (j = 0; j < numBytes && iab < total; j++, iab++) {
				arrayEm[i][j] = arrayByte[iab];
			}
			/**
			 * 补齐空格字符(ox20=32)
			 */
			for (; j < numBytes; j++) {
				arrayEm[i][j] = ' ';
			}
		}
		return arrayEm;
	}

	/**
	 * 对明文进行加密，通过公式 密文=(明文（e次幂） mod m)
	 * 
	 * @param 明文em
	 *            不为空
	 * @param 公钥e
	 * @param 模数n
	 * @return 加密后的密文encodeM
	 */
	private static BigInteger[] encodeRSA(byte[][] em, BigInteger e, BigInteger n) {
		BigInteger[] encodeM = new BigInteger[em.length];
		for (int i = 0; i < em.length; i++) {
			encodeM[i] = new BigInteger(em[i]);
			encodeM[i] = encodeM[i].modPow(e, n);
		}
		return encodeM;
	}

}