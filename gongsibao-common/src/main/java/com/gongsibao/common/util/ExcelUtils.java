package com.gongsibao.common.util;

import com.csvreader.CsvWriter;
import com.gongsibao.common.util.oss.OSSFileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    /**
     * 获得表格指定sheet指定行的列数，默认为第一sheet的第一行的列数
     *
     * @param workbook
     * @param idx
     * @return
     */
    public static int getColCount(Workbook workbook, Integer... idx) {
        Sheet sheet = null;
        int colcount = 0;
        if (ArrayUtils.isEmpty(idx)) {
            sheet = workbook.getSheetAt(0);
            colcount = sheet.getRow(0).getLastCellNum();
        } else if (idx.length == 1) {
            sheet = workbook.getSheetAt(idx[0]);
            colcount = sheet.getRow(0).getLastCellNum();
        } else {
            sheet = workbook.getSheetAt(idx[0]);
            colcount = sheet.getRow(idx[1]).getLastCellNum();
        }

        return colcount;
    }

    private static Workbook getHSSFWorkbook(InputStream stream) throws IOException {
        return new HSSFWorkbook(stream);
    }

    private static Workbook getXSSFWorkbook(InputStream stream) throws IOException {
        return new XSSFWorkbook(stream);
    }

    public static Workbook getWorkbook(InputStream stream) {
        Workbook workbook = null;
        try {
            workbook = getHSSFWorkbook(stream);
        } catch (IOException e) {
            try {
                workbook = getXSSFWorkbook(stream);
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        return workbook;
    }

    public static Workbook getWorkbook(File file) {
        if (null == file) {
            return null;
        }

        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            String fileExt = OSSFileUtils.getExtName(file.getName());
            if (".xls".equalsIgnoreCase(fileExt)) {
                return getHSSFWorkbook(stream);
            } else if (".xlsx".equalsIgnoreCase(fileExt)) {
                return getXSSFWorkbook(stream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * excel转变成list(指定sheet指定行开始封装，默认为第0个sheet的第0行)
     *
     * @return
     */
    public static List<List<String>> getExcelToList(Workbook workbook, Integer... idx) {
        List<List<String>> result = new ArrayList<List<String>>();
        Sheet sheet = null;
        if (ArrayUtils.isEmpty(idx)) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheetAt(idx[0]);
        }
        int i = 0;
        if (ArrayUtils.isNotEmpty(idx) && idx.length > 1) {
            i = idx[1];
        }

        for (; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String value = null;
            List<String> values = new ArrayList<String>();
            if (null != row) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);

                    if (null != cell) {
                        value = "";
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                value = NumberUtils.numberFormat(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                            //							case Cell.CELL_TYPE_BLANK :
                            //							case Cell.CELL_TYPE_ERROR :
                            //							case Cell.CELL_TYPE_FORMULA :
                            //								value = "";
                            //								break;
                            default:
                                value = "";
                                break;
                        }
                    }
                    values.add(StringUtils.trimToEmpty(value));
                }

                while (values.remove("")) ;
                if (CollectionUtils.isNotEmpty(values)) {
                    result.add(values);
                }
            }
        }

        return result;
    }

    /**
     * list转变成excel(带样式)
     *
     * @return
     * @throws IOException
     */
    public static void getListToExcel(List<List<String>> list, String outpath, List<List<CellStyle>> cellstyle) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        Cell cell = null;
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> values = list.get(i);
            for (int j = 0; j < values.size(); j++) {
                cell = row.createCell(j);
                cell.setCellValue(values.get(j));
                if (CollectionUtils.isNotEmpty(cellstyle)) {
                    cell.setCellStyle(cellstyle.get(i).get(j));
                }
            }
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(outpath);
            workbook.write(fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fOut) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * list转变成excel(不带样式)
     *
     * @return
     * @throws IOException
     */
    public static void getListToExcel(List<List<String>> list, String outpath) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        Cell cell = null;
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> values = list.get(i);
            for (int j = 0; j < values.size(); j++) {
                cell = row.createCell(j);
                cell.setCellValue(values.get(j));
            }
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(outpath);
            workbook.write(fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fOut) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * csv格式导出，数据量不限
     *
     * @param list
     * @param outpath
     */
    public static void getListToCsv(List<List<String>> list, String outpath) {
        CsvWriter writer = null;
        try {
            writer = new CsvWriter(outpath, ',', Charset.forName("GBK"));
            for (List<String> rows : list) {
                writer.writeRecord(rows.toArray(new String[]{}));
            }
        } catch (Exception e) {
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}
