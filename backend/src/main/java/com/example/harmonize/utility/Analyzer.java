package com.example.harmonize.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Analyzer {

    public Double GetPossibility(Music list, UserVoice userVoice){
        Double max_pos = (list.getMax() > userVoice.getMax())? userVoice.getMax() : list.getMax();
        Double min_pos = (list.getMin() > userVoice.getMin())? list.getMin() : userVoice.getMin();
        return (max_pos-min_pos)/(list.getMax()-list.getMin())*100;
    }

    // find max, min
    public List<Double> FindMusicRange() throws IOException {
        List<Double> list = new ArrayList<>();

        // 엑셀 파일 경로 지정
        String excelFilePath = "C:/Harmonize/backend/src/main/resources/excel/test.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        // XSSFWorkbook 객체 생성
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        int cnt = 0;
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;

        // 첫 번째 시트 가져오기
        int sheetIndex = 0;
        Row row;
        Cell cell;
        Double value;
        double[][] data = new double[2][workbook.getSheetAt(sheetIndex).getLastRowNum()];

        // 2행부터 마지막 행까지 반복
        for (int i = 1; i <= workbook.getSheetAt(sheetIndex).getLastRowNum(); i++) {

            row = workbook.getSheetAt(sheetIndex).getRow(i);
            // B열 데이터만 추출
            cell = row.getCell(1);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        //System.out.print(value + "\t");
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        data[0][cnt] = value;
                        //System.out.print(data[0][cnt] + "\t");
                        break;
                    default:
                        break;
                }
            }

            // C열 데이터만 추출
            cell = row.getCell(2);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        //System.out.print(value + "\t");
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        data[1][cnt] = value;
                        if (value >= max) {
                            max = value;
                        }
                        if (value <= min) {
                            min = value;
                        }
                        //System.out.print(data[1][cnt] + "\t");
                        cnt++;
                        break;
                    default:
                        break;
                }
            }
            //System.out.println(); // 개행
        }

        //System.out.println("최댓값 : " + max + ", 최솟값 : " + min);

        // Workbook, InputStream 객체 닫기
        workbook.close();
        inputStream.close();

        list.add(max);
        list.add(min);

        return list;
    }
}