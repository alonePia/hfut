package com.lv.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FilesUpload extends HttpServlet {
	protected final transient Log log = LogFactory
			.getLog(FilesUpload.class);
	

	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	    request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8");
		PrintWriter out=response.getWriter();
		/**/
		log.info("--------------------------开始上传文件-------------------------------");
	
		request.setCharacterEncoding( "UTF-8" );	// 从request中取时, 以UTF-8编码解析

		// 工厂, 
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

		// 获取上传文件存放的 目录 , 无则创建
		String path = request.getRealPath( "/upload" );
		log.info("文件保存路径--->"+path);

		new java.io.File( path ).mkdir();
        /** 
         * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上，  
         * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的  
         * 然后再将其真正写到 对应目录的硬盘上 
         */  
		diskFileItemFactory.setRepository( new File( path ) );
        //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室  
        diskFileItemFactory.setSizeThreshold( 1024*1024 );

        ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory); 
        try
        {
            // 可上传多个文件
            @SuppressWarnings("unchecked")
			List<FileItem> list = (List<FileItem>) upload.parseRequest( request );
            for (FileItem item : list )
            {
                // 获取 提交表单的 属性名
                String name = item.getFieldName();

                // 字符串类 属性
                if ( item.isFormField() )
                {
                    String value = item.getString();
                    log.info("文件属性--->name="+value);
                } 
                // 二进制类
                else 
                {
                    // 获取上传文件的名字                   
                    String value = item.getName(); // 1,获取路径                    
                    int start = value.lastIndexOf( "\\" );// 2,索引到最后一个反斜杠
                    String filename = value.substring( start+1 );//3, 截取(+1是去掉反斜杠) 

                    File file = null;
                    do {  
                        // 生成文件名	
                        start = filename.lastIndexOf( "." );    // 索引到最后一个点
                        filename = /*filename.substring( 0, start )    // 不含扩展名的文件
                                    + */
                                    StringUtil.getStrUUID()  // 随机数 UUID.randomUUID()带下划线随机数
                                    + filename.substring( start );  // 扩展名
                        file = new File(path, filename);  
                    } while (file.exists());  

                    log.info("文件名称--->" + filename );

                    // 写到磁盘上去
                    item.write( file );
                    //返回图片路径
					out.write("/upload/"+filename);
					log.info("--------------------------返回文件路径-------------------------------");
                }
            }
        }
        catch (Exception e)
        {
    		log.error("FilesUpload--->文件上传失败",e);
        }

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}
	
	public static void main(String[] args) {
		System.out.println( "I am a serlvet to process upload!" );
	}

}
