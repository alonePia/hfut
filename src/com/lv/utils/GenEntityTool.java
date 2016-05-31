package com.lv.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lv.controller.LoginController;

public class GenEntityTool {
	
	protected static final transient Log log = LogFactory
			.getLog(LoginController.class);

	private String tablename ="";  
	  
    private String[] colnames; // 列名数组  
  
    private String[] colTypes; // 列名类型数组  
  
    private int[] colSizes; // 列名大小数组  
  
    private boolean f_util = false; // 是否需要导入包java.util.*  
  
    private boolean f_sql = false; // 是否需要导入包java.sql.*  
    
    /**生成java类保存路径*/
    private static final String filePath="E:\\javaFile\\";
    
    /**类包名*/
    private static final String packName="package com.lv.entity;\n";
    
    /**需要导入的包名（针对springmvc注解模式）*/
    private static final Boolean isSpringMvc=true;
    
    /**需要导入的具体包名*/
    private static final String packForSpringMvcStr="import java.io.Serializable;import javax.persistence.Basic;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.GeneratedValue;import javax.persistence.GenerationType;import javax.persistence.Id;import javax.persistence.Table;";
  
    /**
     * @author lvliang
     * @param TableName 数据库表名
     */
    public GenEntityTool(String tablename) {  
    	this.tablename=tablename;
        Connection conn = SQLHelper.getConnection(); // 得到数据库连接  
        String strsql = "select * from " + tablename;  
        try {  
            PreparedStatement pstmt = conn.prepareStatement(strsql);  
            ResultSetMetaData rsmd = pstmt.getMetaData();  
            int size = rsmd.getColumnCount(); // 共有多少列  
            colnames = new String[size];  
            colTypes = new String[size];  
            colSizes = new int[size];  
            for (int i = 0; i < rsmd.getColumnCount(); i++) {  
                colnames[i] = rsmd.getColumnName(i + 1);  
                colTypes[i] = rsmd.getColumnTypeName(i + 1);  
                if (colTypes[i].equalsIgnoreCase("datetime")) {  
                    f_util = true;  
                }  
                if (colTypes[i].equalsIgnoreCase("image")  
                        || colTypes[i].equalsIgnoreCase("text")) {  
                    f_sql = true;  
                }  
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);  
            }  
            String content = parse(colnames, colTypes, colSizes);  
            try {  
                FileWriter fw = new FileWriter(initcap(tablename) + ".java");  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(content);  
                pw.flush();  
                pw.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
        	SQLHelper.closeConnection(conn);  
        }  
    }  
  
    /** 
    * 解析处理(生成实体类主体代码) 
    */  
    private String parse(String[] colNames, String[] colTypes, int[] colSizes) {  
        StringBuffer sb = new StringBuffer();
        
        sb.append(packName);  
        
        if (f_util) {  
            sb.append("import java.util.Date;\r\n");  
        }  
        if (f_sql) {  
            sb.append("import java.sql.*;\r\n");  
        }
        
        if(isSpringMvc){
        	String[] strs=packForSpringMvcStr.split(";");
        	for (int i = 0; i < strs.length; i++) {
				sb.append(strs[i]+";\r\n");
			}
        }
        
        sb.append("\r\n@Entity \r\n");  
        sb.append("@Table(name = "+"\""+tablename+"\""+") \r\n");  
        sb.append("public class " + initcap(tablename) + " implements Serializable {\r\n");  
        processAllAttrs(sb);  
        processAllMethod(sb);  
        sb.append("}\r\n");  
        //System.out.println(sb.toString());  
        saveFileToHardDisk(tablename+".java",sb.toString());
        return sb.toString();  
  
    }  
  
    /** 
    * 生成所有的方法 
    *  
    * @param sb 
    */  
    private void processAllMethod(StringBuffer sb) {  
        for (int i = 0; i < colnames.length; i++) {  
        	sb.append("\t/**"+colnames[i]+"*/\r\n");
            sb.append("\tpublic void set" + initcap(colnames[i]) + "("  
                    + sqlType2JavaType(colTypes[i]) + " " + colnames[i]  
                    + "){\r\n");  
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n\n");  

        	sb.append("\t/**"+colnames[i]+"*/\r\n");
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"  
                    + initcap(colnames[i]) + "(){\r\n");  
            sb.append("\t\treturn " + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n\n");  
        }  
    }  
  
    /** 
    * 解析输出属性 
    *  
    * @return 
    */  
    private void processAllAttrs(StringBuffer sb) {  
        for (int i = 0; i < colnames.length; i++) {
        	sb.append("\t@Column(name = "+"\""+colnames[i]+"\""+", nullable = false)\r\n");
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " "  
                    + colnames[i] + ";\r\n\n");  
  
        }  
    }  
  
    /** 
    * 把输入字符串的首字母改成大写 
    *  
    * @param str 
    * @return 
    */  
    private static String initcap(String str) {  
        char[] ch = str.toCharArray();  
        if (ch[0] >= 'a' && ch[0] <= 'z') {  
            ch[0] = (char) (ch[0] - 32);  
        }  
        return new String(ch);  
    }  
  
    private String sqlType2JavaType(String sqlType) {  
        if (sqlType.equalsIgnoreCase("bit")) {  
            return "boolean";  
        } else if (sqlType.equalsIgnoreCase("tinyint")) {  
            return "byte";  
        } else if (sqlType.equalsIgnoreCase("smallint")) {  
            return "short";  
        } else if (sqlType.equalsIgnoreCase("int")) {  
            return "int";  
        } else if (sqlType.equalsIgnoreCase("bigint")) {  
            return "long";  
        } else if (sqlType.equalsIgnoreCase("float")) {  
            return "float";  
        } else if (sqlType.equalsIgnoreCase("decimal")  
                || sqlType.equalsIgnoreCase("numeric")  
                || sqlType.equalsIgnoreCase("real")
                || sqlType.equalsIgnoreCase("double")) {  
            return "double";  
        } else if (sqlType.equalsIgnoreCase("money")  
                || sqlType.equalsIgnoreCase("smallmoney")) {  
            return "double";  
        } else if (sqlType.equalsIgnoreCase("varchar")  
                || sqlType.equalsIgnoreCase("char")  
                || sqlType.equalsIgnoreCase("nvarchar")  
                || sqlType.equalsIgnoreCase("nchar")) {  
            return "String";  
        } else if (sqlType.equalsIgnoreCase("datetime")) {  
            return "Date";  
        }  
  
        else if (sqlType.equalsIgnoreCase("image")) {  
            return "Blob";  
        } else if (sqlType.equalsIgnoreCase("text")) {  
            return "Clob";  
        }  
        return null;  
    }  
  
    public static void main(String[] args) {  
    	List<String> list =new ArrayList<String>();
    	list.add("users");
    	list.add("cars");
    	list.add("orders");
    	list.add("repair");
    	list.add("broke");
    	for (int i = 0; i < list.size(); i++) {
            new GenEntityTool(list.get(i));  
		}
    }  
	
    
    /**
     * 读取本地文件
     * 以行为单位读取文件，常用于读面向行的格式化文件 
     */
    public  void readFileByLines(String fileName,String content) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line--------" + line + ": " + tempString);
                content=tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    
    
    /**
     * 生成本地java类文件
     * @author lvliang
     * @param filePath 文件保存路径
     * @param fileName 文件路径及名称
     * @param fileStr 文件内容
     */
    public static void saveFileToHardDisk(String fileName,String fileStr){
    	BufferedWriter writer = null;
    	File fileFloder =new File(filePath);
    	if(!fileFloder.exists() && !fileFloder.isDirectory()){
    		log.info(filePath+",该文件目录不存在，开始创建");
    		fileFloder.mkdirs();
    	}
    	//处理文件名首字母大写
    	if(fileName!=null && !"".equals(fileName)){
    		fileName=initcap(fileName);
    	}
    	File file = new File(filePath+fileName);
    	//如果文件不存在，则新建一个
    	if(!file.exists()){
	    	try {
	    		log.info(filePath+fileName+",该文件不存在，开始创建");
	    		file.createNewFile();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
    	}
    	//写入
    	try {
	    	writer = new BufferedWriter(new FileWriter(file));
	    	writer.write(fileStr);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}finally {
	    	try {
	    		if(writer != null){
	    			writer.close();
	    		}
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
    	}
    	log.info(filePath+fileName+"文件写入成功！");
	}
    
}
