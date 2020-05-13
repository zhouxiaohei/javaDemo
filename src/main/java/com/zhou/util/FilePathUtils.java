package com.zhou.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilePathUtils {

	private static final Logger logger = LoggerFactory.getLogger(FilePathUtils.class);

	/** 域名正则表达式 **/
	private static String DOMAIN_RULES = "ac.cn|com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";

	private static Set<String> imageSet = new HashSet<String>();

	private static Set<String> htmlSet =  new HashSet<String>();

	static{
		//图片类型
		imageSet.add("jpg");imageSet.add("jpeg");imageSet.add("bmp");imageSet.add("gif");imageSet.add("tif");imageSet.add("png");

		//html类型
		htmlSet.add("action");htmlSet.add("do");
		htmlSet.add("jsp");htmlSet.add("asp");htmlSet.add("aspx");htmlSet.add("php");htmlSet.add("html");htmlSet.add("shtml");htmlSet.add("cshtml");htmlSet.add("htm");
	}

	/**
	 * @描述: 得到合法文件名
	 * @说明:
	 * @修改时间: 2017年3月13日 下午8:26:11
	 * @return
	 */
	public static String getValidName(String fileName) {
		if (StringUtils.isBlank(fileName)){
			return "uf-" + System.currentTimeMillis();
		}else if(fileName.length() > 128){
			int lastIndexOf = fileName.lastIndexOf(".");
			if(lastIndexOf > 120){
				String postfix = fileName.substring(lastIndexOf, fileName.length());
				if(postfix.length() < 16){
					fileName = fileName.substring(0,120) + postfix;
				}else{
					fileName = fileName.substring(0,120);
				}
			}else{
				fileName = fileName.substring(0,127);
			}
		}
		fileName = replaceSpecStr(fileName);
		return fileName;
	}

	/**
	 * @描述:分割最后/得到url中的文件名
	 * @说明: 验证文件名是否合法
	 * @修改时间: 2017年3月13日 下午8:32:46
	 * @return
	 */
	public static String getNameByUrl(String url) {
		String fileName = null;
		if(url.endsWith("/")){
			url = url.substring(0,url.length() -1);
			fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		}else{
			fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		}
		fileName = getValidName(fileName);
		return fileName;
	}

	/**
	 * <p>方法名: isUrl</p>
	 * <p>描述: 该方法能判断出ip形式的url但是不能判断ip的正确性，可与isIp方法共同使用</p>
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str) {
		String regex = "^((https|http|ftp|rtsp|mms)?://)"
				+ "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" // ftp的user@
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "(([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]+\\.)?" // 域名www.
				+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // 二级域名
				+ "(" + DOMAIN_RULES + "))" // first level domain- .com or .museum
				+ "(:[0-9]{1,4})?" // 端口- :80
				+ "((/?)|" // a slash isn't required if there is no file name
				+ "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(str);
		return matcher.matches();
	}

	/**
	 * @描述: http://www.baidu.com 返回 www.baidu.com
	 * @说明: 不符合标准返回""或者null
	 * @修改时间: 2017年3月14日 下午2:23:22
	 * @param urlStr
	 * @return
	 */
	public static String getDomain(String urlStr){
		if(!urlStr.startsWith("http") && !urlStr.startsWith("https")){
			urlStr = "http://" + urlStr;
		}
		try {
			URL url = new URL(urlStr);
			return url.getHost();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * @描述: 提取http://www.baidu.com/aa/bb/cc中的目录
	 * @说明: 去除最后一个斜杠
	 * "http://www.baidu.com//dd/dd/dd/cc.html"; 获取/www.baidu.com/dd/dd/dd/
	 * // http 多个//  已/结尾  区分
	 * @修改时间: 2017年3月14日 下午3:03:54
	 */
	public static String getDir(String url){
		if(url.endsWith("/")){
			url = url.substring(0, url.length() -1);
		}
		String[] split = url.split("/");
		int count = 0;
		//String dir = File.separator;
		String dir = "";
		for (String string : split) {
			count++;
			if(count == split.length){
				break;
			}
			if(!StringUtils.isBlank(string) && !string.startsWith("http")){
				string = getValidName(string);
				dir = dir + string + File.separator;
			}
		}
		return dir;
	}

	/**
	 * @描述: 通过url得到路径   并创建成功
	 * @说明: prefix 包含储存路径 scan.thread.dir加上任务路径 【linux下/结尾】
	 */
	public static String getFilePathByUrl(String url, String prefix, int fileType, String host){
		String dir = null;
		if(fileType == 2){
			dir = host +  File.separator +  "allImage" +  File.separator;
		}else if(fileType == 3){
			dir =  host +  File.separator + "js-css" +  File.separator;
		}else if(fileType == 1){
			dir = getDir(url);
		}else{
			return null;
		}
		String fileName = getNameByUrl(url);
		if(!StringUtils.isBlank(dir)){
			File file = new File(prefix + dir);
			boolean mkdir = true;
			if(file.exists() && file.isDirectory()){
			}else{
				mkdir = file.mkdirs();
			}
			if(mkdir){
				File realfile = new File(prefix + dir + fileName);
				// 文件是一个目录  或者文件已经存在
				if(realfile.isDirectory() || realfile.exists()){
					return prefix + dir + System.currentTimeMillis() + "-" + fileName;
				}
				return prefix + dir + fileName;
			}
		}
		/**
		 * 目录为空 或者目录创建失败 创建uf目录
		 */
		String udDir = host + File.separator + "uf" + File.separator;
		File file = new File(prefix + udDir);
		if(file.exists() && file.isDirectory()){
		}else{
			file.mkdirs();
		}
		File realfile = new File(prefix + udDir + fileName);
		// 文件是一个目录  或者文件已经存在
		if(realfile.isDirectory() || realfile.exists()){
			return prefix + udDir + System.currentTimeMillis() + "-" + fileName;
		}
		return prefix + udDir + fileName;
	}

	/**
	 * @描述: 根据内容和路径写文件
	 * @说明:
	 */
	public static boolean createFile(String content, String filePath){
		FileOutputStream  out = null;
		FileChannel fcOut = null;
		try {
			 out = new FileOutputStream(filePath);
			 fcOut = out.getChannel();
			 ByteBuffer buf = ByteBuffer.wrap(content.getBytes());
			 buf.put(content.getBytes());
			 buf.flip();
			 fcOut.write(buf);
			 return true;
		} catch (FileNotFoundException e) {
			logger.error("写文件失败FileNotFoundException：" + filePath, e);
		} catch (IOException e) {
			logger.error("写文件失败IOException：" + filePath, e);
		}finally{
			if(fcOut != null){
				try {
					fcOut.close();
				} catch (IOException e) {
					logger.error("关闭FileChannel失败：", e);
				}
			}

			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("关闭FileOutputStream失败：", e);
				}
			}
		}
		 return false;
	}

	/**
	 * @描述: 得到文件类型
	 * @说明:
	 * 1类文件   html 下载或者解析
	 * 2类文件  图片
	 * 3类文件   js、css
	 * 0类文件  不识别的文件 过滤掉
	 */
	public static int getFileType(String url){
		if(url.endsWith("/")){
			url = url.substring(0, url.length() -1);
		}
		String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		//  到处都是点2jizdy_2.jsp?urltype=news.NewsContentUrl&wbtreeid=1193&wbnewsid=3049
		if(fileName.contains(".")){
			int end =  fileName.length();
			if(fileName.contains("?")){
				end = fileName.lastIndexOf("?");
				fileName = fileName.substring(0,end);
			}
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, end);

			fileType = fileType.toLowerCase();
			if(imageSet.contains(fileType)){
				return 2;
			}else if(htmlSet.contains(fileType)){
				return 1;
			}else if(fileType.equals("css") || fileType.equals("js")){
				return 3;
			}
			return 0;
		}
		return 1;
	}

	/**
	 * 替换掉特殊字符
	 * .不要替换掉
	 */
	public static String replaceSpecStr(String orgStr){
	    if (null != orgStr && !"".equals(orgStr.trim())) {
	    	//String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";
	    	//不替换掉.
	    	String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。》>、/？?]";
	    	Pattern p = Pattern.compile(regEx);
	        Matcher m = p.matcher(orgStr);
	        return m.replaceAll("");
	    }
	    return null;
	}

	public static void main(String[] args) {
		//int num = 0xFFFFFFF;
//		System.out.println(Integer.toBinaryString(num));
//		System.out.println(Integer.toBinaryString(num).length());
//		for(int i=0; i<5; i ++) {
//		       num = num >> 2;
//		     System.out.println(num);
//		   }
//		System.out.println(Integer.toBinaryString(num));
//		System.out.println(Integer.toBinaryString(num).length());
		//System.out.println(Integer.valueOf(Integer.toBinaryString(num),2));
		//System.out.println(getNameByUrl("https://www.huxiu.com/"));
//		System.out.println(isValidFileName("list.asp?id=723"));
//		System.out.println(isSimpleDomain("www.baidu.com/ab/dd"));
//		String url = "http://www.baidu.com//dd/dd/dd/cc.html";
//		System.out.println(getDir(url));
//
//		String dir = System.getProperty("user.dir") + File.separator;
//		System.out.println(System.getProperty("user.dir") + File.separator);
//		File file = new File(dir + "/125175*&$%^/4q9%^**/js/aa");
//		System.out.println( file.mkdirs());
	}
}
