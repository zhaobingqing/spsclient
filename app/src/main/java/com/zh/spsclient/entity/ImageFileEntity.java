package com.zh.spsclient.entity;
/*
 * 图片实体类
 * 重构 20140404 
 * @author Lee
 */
public class ImageFileEntity {
	public ImageFileEntity()
	{}
	
	private String imageName;
	private String imageContent;
	/*
	 * 设置上传图片文件名
	 */
	public void setImageName(String imageName) {
		this.imageName=imageName;
	}
	/*
	 * 获取图片文件名
	 */
	public String getImageName() {
		return imageName;
	}
	/*
	 * 设置图片流文件内容
	 */
	public void setImageContent(String imageContent) {
		this.imageContent=imageContent;
	}
	/*
	 * 获取图片流文件内容
	 */
	public String getImageContent() {
		return imageContent;
	}
}
