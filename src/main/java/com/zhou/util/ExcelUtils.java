package com.zhou.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>类名: ExcelUtils</p>
 * <p>描述: excel公共类</p>
 */
public class ExcelUtils {

	/**
	 * <p>方法名: getCellStyle2007</p>
	 * <p>描述: 设置单元格样式</p>
	 * @param workBook
	 * @return
	 */
	public static CellStyle getCellStyle2007(Workbook workBook) {
		// 设置样式
		CellStyle cellStyle = workBook.createCellStyle();// 创建样式
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);// 居左
		// 边框实线
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);// 下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框

		//设置自动换行
		cellStyle.setWrapText(true);

		return cellStyle;
	}

	/**
	 * <p>方法名: getTitleCellStyle2007</p>
	 * <p>描述: 设置表头单元格样式</p>
	 * @return
	 */
	public static CellStyle getTitleCellStyle2007(Workbook workBook) {
		// 设置样式
		CellStyle cellStyle = workBook.createCellStyle();// 创建样式
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);// 居左
		// 边框实线
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);// 下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框

		//背景色
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		//字体
		Font font = workBook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		cellStyle.setFont(font);

		return cellStyle;
	}

	/**
	 * @描述: 追加excel
	 * @说明:
	 * @param list
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static boolean appendExcel(List<Object[]> list, String filePath) throws IOException{

		FileInputStream fs = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		//设置单元格样式
		CellStyle cellStyle = ExcelUtils.getCellStyle2007(wb);

		FileOutputStream out = new FileOutputStream(filePath);

		int count = sheet.getLastRowNum();
		for(Object[] obj : list){
			count++;
			row = sheet.createRow(count);
			row.setHeight((short)(25*20));
			for(int i = 0; i< obj.length ; i++ ){
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(obj[i] + "");
				cell.setCellStyle(cellStyle);
			}
		}
		out.flush();
		wb.write(out);
		out.close();
		fs.close();
		return true;
	}


	/**
	 * 创建一个标准的sheet  包含表头和内容
	 * @param list  String类型数据list
	 * @param workBook
	 * @param sheetName sheet名
	 * @param titleNames 字段名
	 */
	private static void createSheet(List<Object[]> list, Workbook workBook, String sheetName, String[] titleNames) {
		Sheet sheet = workBook.createSheet(sheetName);
		if(list !=null && list.size() > 0 && list.get(0).length == titleNames.length){
		}else{
			return;
		}
		//设置单元格样式
		CellStyle cellStyle = ExcelUtils.getCellStyle2007(workBook);
		CellStyle titleCellStyle = ExcelUtils.getTitleCellStyle2007(workBook);
		//粗体显示
		CellStyle style = workBook.createCellStyle();
		Font font = workBook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);


		Row row = sheet.createRow(0);
		Cell cell = null;

		sheet.setColumnWidth(0, 14 * 256);
		for(int i = 0; i < titleNames.length; i++){
			sheet.setColumnWidth(i+1, 25 * 256);

			cell = row.createCell(i);
			cell.setCellValue(titleNames[i]);
			cell.setCellStyle(titleCellStyle);
		}

		int count = 0;
		for(Object[] obj : list){
			count++;
			row = sheet.createRow(count);
			row.setHeight((short)(25*20));
			for(int i = 0; i< obj.length ; i++ ){
				cell = row.createCell(i);
				cell.setCellValue(obj[i] + "");
				cell.setCellStyle(cellStyle);
			}
		}
	}

	/**
	 * @描述: 生成excel文件
	 * @说明:
	 * @修改时间: 2016年3月24日 下午5:34:29
	 * @param task
	 * @param filePath
	 * @return
	 */
	public static boolean exportExcel(List<Object[]> list, String filePath,String sheetName,String[] titleNames) {

		Workbook workBook = new SXSSFWorkbook(5000);
		createSheet(list,workBook,sheetName,titleNames);

		// 将文件存到指定位置
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(filePath);
			workBook.write(fout);
			return true;
		} catch (IOException e) {
			System.out.println("导出失败,原因:" + e);
			return false;
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @描述: 生成excel文件
	 * @说明:
	 * @修改时间: 2016年3月24日 下午5:34:29
	 * @param task
	 * @param filePath
	 * @return
	 */
	public static boolean exportExcel2(List<Object[]> list, String filePath,String sheetName,String[] titleNames) {

		XSSFWorkbook workBook = new XSSFWorkbook();
		createSheet(list,workBook,sheetName,titleNames);

		// 将文件存到指定位置
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(filePath);
			workBook.write(fout);
			return true;
		} catch (IOException e) {
			System.out.println("任务详情导出失败,原因:" + e);
			return false;
		} finally {
			try {
				if (null != fout) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String path = "C:\\Users\\sks\\Desktop\\test.xlsx";
		String [] titleName = new String[]{"原始URL","爬取URL","爬取时间","爬取状态","网站标题","网站关键词","网站description","网页内容","外部链接"};
		Object[] arrayObjTest = new Object[]{"1","2","3","爬取状态","网站标题","网站关键词","网站description","网页内容","外部链接"};;
		Object[] arrayObj = new Object[titleName.length];
		List<Object[]> resultList = new ArrayList<Object[]>();
		arrayObj[0] = "append";
		arrayObj[1] = "test for something";
		resultList.add(arrayObjTest);
		resultList.add(arrayObjTest);
		exportExcel(resultList, path, "测试", titleName);

		resultList.add(arrayObj);
		try {
			appendExcel(resultList, path);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
