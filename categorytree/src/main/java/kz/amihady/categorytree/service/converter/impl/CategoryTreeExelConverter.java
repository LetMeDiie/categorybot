package kz.amihady.categorytree.service.converter.impl;


import kz.amihady.categorytree.dto.CategoryDto;
import kz.amihady.categorytree.exception.CategoryTreeConversionFileException;
import kz.amihady.categorytree.service.converter.CategoryTreeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// преобразуем категорию в байт файл
@Slf4j
public class CategoryTreeExelConverter implements CategoryTreeConverter<byte[]> {
    public byte[] convert(List<CategoryDto> categoryDtoList) {
        Map<Long, List<CategoryDto>> categoryMap = categoryDtoList.stream()
                .collect(Collectors.groupingBy(CategoryDto::parentId));
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Categories");

            // Шрифт для выделения категорий
            XSSFFont font = workbook.createFont();
            font.setBold(true);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Категории");

            int rowNum = 1;
            rowNum = buildTree(categoryMap, -1L, 0, sheet, rowNum, font);

            // Создаем ByteArrayOutputStream для записи в память
            try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOut);
                log.info("Файл для категорий был успешно создан");
                return byteArrayOut.toByteArray(); // Возвращаем байтовый массив
            } catch (IOException e) {
                log.warn("Ошибка при записи Exel");
                throw new CategoryTreeConversionFileException("Ошибка при записи Excel в поток памяти.");
            }
        } catch (IOException e) {
            log.warn("Ошибка при созданий Exel");
            throw  new CategoryTreeConversionFileException("Ошибка при создании или записи Excel файла.");
        }
    }
    private int buildTree(Map<Long, List<CategoryDto>> categoryMap, Long parentId, int level,
                          Sheet sheet, int rowNum, XSSFFont font) {
        List<CategoryDto> children = categoryMap.getOrDefault(parentId, Collections.emptyList());

        for (CategoryDto category : children) {
            String icon = (parentId == -1) ? "📂" : (categoryMap.containsKey(category.categoryId()) ? "📁" : "📄");
            String indent = " ".repeat(level); // Отступы для каждого уровня
            String categoryName = indent + icon + " " + category.categoryName();

            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(categoryName);

            // Если категория имеет подкатегории, рекурсивно вызываем для нее
            rowNum = buildTree(categoryMap, category.categoryId(), level + 1, sheet, rowNum, font);
        }
        return rowNum;
    }
}

