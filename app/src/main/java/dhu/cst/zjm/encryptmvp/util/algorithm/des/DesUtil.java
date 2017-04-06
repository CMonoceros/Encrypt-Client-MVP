package dhu.cst.zjm.encryptmvp.util.algorithm.des;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密工具类
 * 
 * @author ZJM
 *
 */
public class DesUtil {
	 private static byte[] iv = {1,2,3,4,5,6,7,8};

	/**
	 * 根据DES密钥进行加密
	 * 
	 * @param data
	 *            所要加密数据的字节数组
	 * @param key
	 *            密钥字节数组
	 * @return 加密后数据字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		System.out.println("---------------DES加密文件------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("des/ECB/PKCS5Padding");
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey,sr);
		byte[] result=cipher.doFinal(data);
		System.out.println("---------------DES加密成功------------------");
		System.out.println(df.format(new Date()));
		return result;
	}

	/**
	 * 根据DES密钥进行解密
	 * 
	 * @param data
	 *            所要解密数据的字节数组
	 * @param key
	 *            密钥字节数组
	 * @return 解密后数据字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		System.out.println("---------------DES解密文件------------------");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println(df.format(new Date()));
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("des");
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		byte[] result=cipher.doFinal(data);
		System.out.println("---------------DES解密成功------------------");
		System.out.println(df.format(new Date()));
		return result;
	}

}
