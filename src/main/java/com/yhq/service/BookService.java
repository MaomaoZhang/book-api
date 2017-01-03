package com.yhq.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.me.model.Message;
import com.yhq.dao.BookDao;
import com.yhq.model.Book;

@Service
public class BookService {
	@Autowired
	private BookDao bookDao;
	/**
	 * 上传书本
	 * @param cover
	 * @param picture
	 * @return
	 */
	public Message addBook(Book book , CommonsMultipartFile cover , CommonsMultipartFile picture) {
		Message message = null;
		String pictureName = uploadImage(picture);
		book.setPicture(pictureName);
		String coverName = uploadImage(cover);
		book.setCover(coverName);
		if(bookDao.addBook(book)) {
			message = Message.success("添加书本成功！");
		}
		else {
			message = Message.error("添加书本失败");
		}
		return message;
	}
	
	
	/**
	 * 查询所有的书本
	 * @return
	 */
	public Message queryAllBook() {
		Message message = null;
		List<?> list = bookDao.queryAllBook();
		if(list != null && list.size() > 0) {
			message = Message.success("查询所有书本成功！");
			message.dataPut("list", list);
		}
		else {
			message = Message.error("查询所有书本失败");
		}
		return message;
	}
	
	public Message queryOneBook(Long id) {
		Message message = null;
		Book book = new Book();
		book.setId(id);
		List<?> list = bookDao.queryOneBook(book);
		if(list != null && list.size() > 0) {
			message = Message.success("查询某本书成功！");
			message.dataPut("list", list);
		}
		else {
			message = Message.error("查询某本书失败！");
		}
		return message;
	}
	/**
	 * 删除书本
	 * @param id
	 * @return
	 */
	public Message deleteBook(Long id) {
		Message message = null;
		Book book = new Book();
		book.setId(id);
		if(bookDao.deleteBook(book)) {
			message = Message.success("删除成功！");
		}
		else {
			message = Message.error("删除失败！");
		}
		return message;
	}
	
	/**
	 * 上传图片
	 * @param image
	 * @return
	 */
	public String uploadImage(CommonsMultipartFile image) {
		if(image == null) {
			return "文件为空";
		}
		//获取原始文件名
		String fileName = image.getOriginalFilename();
		System.out.println("OriginalFilename is " + fileName);
		//新文件名
		String newFileName = UUID.randomUUID()+fileName;
		//上传到什么地方
		String path = "/Users/Wrappers/git/book-api/src/main/webapp/image";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		if (!image.isEmpty()) {
			try {
				FileOutputStream fos = new FileOutputStream(path + newFileName);
				InputStream in = image.getInputStream();
				int b = 0;
				while((b=in.read()) != -1){
					fos.write(b);
				}
				fos.close();
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("imgurl:"+ path+newFileName);
		}
		return newFileName;
	}
}
