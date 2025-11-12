package com.xaxc.teqin.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.model.dto.DynamicDataDTO;
import com.xaxc.teqin.model.entity.SpecialServiceArchivesForm;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ExcelUtils {

    /**
     * 接收文件并返回文件名
     *
     * @param file
     * @return
     */
    public static String receiveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            FileUtils.writeToFile(new File(fileName), file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    public static List<JSONObject> getSheetData(String fileName, int sheetNo, Object classObj) {
        return getSheetData(fileName, sheetNo, classObj, 3, null);
    }

    /**
     * 首字母大写
     *
     * @param input
     * @return
     */
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String result = Character.toUpperCase(input.charAt(0)) + input.substring(1);
        return result.trim();
    }

    private static Class getClassByFieldType(String fieldType) {
        Class className;
        switch (fieldType) {
            case "String":
            case "input":
            case "textarea":
            case "point":
            case "video":
            case "photo":
            case "InputNumber":
            case "DatePicker":
                className = String.class;
                break;
            default:
                className = Object.class;
                break;
        }
        return className;
    }


    public static Class<? extends DynamicDataDTO> getDynamicClassByArchivesForm(List<SpecialServiceArchivesForm> specialServiceArchivesForms) {
        DynamicType.Builder<DynamicDataDTO> dynamicClass = new ByteBuddy().subclass(DynamicDataDTO.class);
        for (SpecialServiceArchivesForm specialServiceArchivesForm : specialServiceArchivesForms) {
            String fieldId = specialServiceArchivesForm.getFieldId();
            //去除（）的字段
            if (fieldId.contains("(")) {
                fieldId = fieldId.substring(0, fieldId.indexOf("("));
            }
            if (fieldId.contains("（")) {
                fieldId = fieldId.substring(0, fieldId.indexOf("（"));
            }
            String fieldName = specialServiceArchivesForm.getFieldName();
            String fieldType = specialServiceArchivesForm.getFieldType();
            Integer columnIndex = specialServiceArchivesForm.getColumnIndex();
            //是否必填
            String mandatory = specialServiceArchivesForm.getMandatory();
            String getMethod = "get" + capitalizeFirstLetter(fieldId);
            String setMethod = "set" + capitalizeFirstLetter(fieldId);
            Class typeName = getClassByFieldType(fieldType);
            DynamicType.Builder.FieldDefinition.Optional<DynamicDataDTO> optionalExcelRecordDynamicDTO;
            DynamicType.Builder.FieldDefinition.Optional.Valuable<DynamicDataDTO> valuable = dynamicClass.defineField(fieldId, String.class, Visibility.PUBLIC);
            if (null == columnIndex) {
                optionalExcelRecordDynamicDTO = valuable.annotateField(AnnotationDescription.Builder.ofType(ExcelProperty.class)
                        .defineArray("value", fieldName)
                        .build());
            } else {
                optionalExcelRecordDynamicDTO = valuable.annotateField(AnnotationDescription.Builder.ofType(ExcelProperty.class)
                        .defineArray("value", fieldName)
                        .define("index", columnIndex)
                        .build());
            }
            if ("0".equals(mandatory)) {
                //@ExcelValid(message = "名称不能为空")
                optionalExcelRecordDynamicDTO = optionalExcelRecordDynamicDTO.annotateField(AnnotationDescription.Builder.ofType(ExcelValid.class)
                        .define("message", "【" + fieldName + "】不能为空，请检查列数据是否存在。")
                        .build());
            }
            dynamicClass = optionalExcelRecordDynamicDTO.defineMethod(getMethod, typeName, Visibility.PUBLIC)
                    .intercept(FieldAccessor.ofBeanProperty())
                    .defineMethod(setMethod, void.class, Visibility.PUBLIC)
                    .withParameters(typeName)
                    .intercept(FieldAccessor.ofBeanProperty());
        }
        return dynamicClass.make().load(DynamicDataDTO.class.getClassLoader()).getLoaded();
    }

    public static Class<? extends DynamicDataDTO> getDynamicClass(String title, List<SpecialServiceArchivesForm> specialServiceArchivesForms) {
        DynamicType.Builder<DynamicDataDTO> dynamicClass = new ByteBuddy().subclass(DynamicDataDTO.class);
        for (SpecialServiceArchivesForm specialServiceArchivesForm : specialServiceArchivesForms) {
            String fieldId = specialServiceArchivesForm.getFieldId();
            //去除（）的字段
            if (fieldId.contains("(")) {
                fieldId = fieldId.substring(0, fieldId.indexOf("("));
            }
            if (fieldId.contains("（")) {
                fieldId = fieldId.substring(0, fieldId.indexOf("（"));
            }
            String fieldName = specialServiceArchivesForm.getFieldName();
            String fieldType = specialServiceArchivesForm.getFieldType();
            String getMethod = "get" + capitalizeFirstLetter(fieldId);
            String setMethod = "set" + capitalizeFirstLetter(fieldId);
            Class typeName = getClassByFieldType(fieldType);

            dynamicClass = dynamicClass.defineField(fieldId, String.class, Visibility.PUBLIC)
                    .annotateField(AnnotationDescription.Builder.ofType(ExcelProperty.class)
                            .defineArray("value", title, " ", fieldName)
                            .build())
                    .defineMethod(getMethod, typeName, Visibility.PUBLIC)
                    .intercept(FieldAccessor.ofBeanProperty())
                    .defineMethod(setMethod, void.class, Visibility.PUBLIC)
                    .withParameters(typeName)
                    .intercept(FieldAccessor.ofBeanProperty());
        }
        return dynamicClass.make().load(DynamicDataDTO.class.getClassLoader()).getLoaded();
    }

    /**
     * 获取档案各部分起始行位置
     *
     * @param fileName
     * @param sheetNo
     * @param partNames
     * @return
     */
    public static int[] getArchivesPartRowIndex(String fileName, int sheetNo, List<String> partNames) {
        int[] infoRowIndex = {3, 3, 3, 3, 3, 3};
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).file(fileName).sheet(sheetNo)
                .registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
                    @Override
                    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
                        Set<Integer> set = integerStringMap.keySet();
                        Iterator<Integer> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Integer key = iterator.next();
                            if (StringUtils.hasText(integerStringMap.get(key))) {
                                String part = integerStringMap.get(key).replaceAll(" ", "").replaceAll("\n", "");
                                if (partNames.get(0).equals(part)) {
                                    infoRowIndex[0] = analysisContext.readRowHolder().getRowIndex();
                                } else if (partNames.get(1).equals(part)) {
                                    infoRowIndex[1] = analysisContext.readRowHolder().getRowIndex();
                                } else if (partNames.get(2).equals(part)) {
                                    infoRowIndex[2] = analysisContext.readRowHolder().getRowIndex();
                                } else if (partNames.get(3).equals(part)) {
                                    infoRowIndex[3] = analysisContext.readRowHolder().getRowIndex();
                                } else if (partNames.get(4).equals(part)) {
                                    infoRowIndex[4] = analysisContext.readRowHolder().getRowIndex();
                                } else if (part.startsWith("审核人:")) {//结束行标记
                                    infoRowIndex[5] = analysisContext.readRowHolder().getRowIndex();
                                }
                            }
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }
                }).doRead();
        if (infoRowIndex[4] >= infoRowIndex[5]) {
            infoRowIndex[5] = infoRowIndex[4] + 10;
        }
        return infoRowIndex;
    }

    public static List<ReadSheet> getSheetList(String fileName) {
        return EasyExcel.read(fileName).build().excelExecutor().sheetList();
    }

    /**
     * 获取档案基本信息
     *
     * @param fileName
     * @param sheetNo
     * @param endRowIndex
     * @param specialServiceArchivesForms
     * @return
     */
    public static JSONObject getArchivesBasicInfo(String fileName, int sheetNo, int endRowIndex, List<SpecialServiceArchivesForm> specialServiceArchivesForms) {
        JSONObject jsonObject = new JSONObject();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).file(fileName).sheet(sheetNo)
                .registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
                    @Override
                    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
                        Set<Integer> set = integerStringMap.keySet();
                        Iterator<Integer> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Integer key = iterator.next();
                            if (StringUtils.hasText(integerStringMap.get(key))) {
                                String part = integerStringMap.get(key).replaceAll(" ", "").replaceAll("\n", "");
                                for (SpecialServiceArchivesForm specialServiceArchivesForm : specialServiceArchivesForms) {
                                    String fieldId = specialServiceArchivesForm.getFieldId();
                                    String fieldName = specialServiceArchivesForm.getFieldName();
                                    if (fieldName.equals(part) && analysisContext.readRowHolder().getRowIndex() < endRowIndex) {
                                        jsonObject.put(fieldId, integerStringMap.get(iterator.next()));
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }
                }).doRead();
        return jsonObject;
    }

    /**
     * 获取指定行之间的数据
     *
     * @param fileName
     * @param sheetNo
     * @param classObj
     * @param startRowNum
     * @param endRowNum
     * @return
     */

    public static List<JSONObject> getSheetData(String fileName, int sheetNo, Object classObj, Integer startRowNum, Integer endRowNum) {
        List<JSONObject> sheetDataList = new ArrayList<>();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(fileName).sheet(sheetNo).head(classObj.getClass()) //按指定类解析
                .headRowNumber(startRowNum) //从指定行开始解析
                .registerReadListener(new ReadListener() {
                    @Override
                    public void invoke(Object object, AnalysisContext analysisContext) {
                        if (endRowNum != null && analysisContext.readRowHolder().getRowIndex() >= endRowNum) {
                            return;
                        }
                        JSONObject jsonObject = new JSONObject();
                        //去除内容中多余的换行符
                        for (Field field : object.getClass().getDeclaredFields()) {
                            try {
                                field.setAccessible(true);
                                Object value = field.get(object);
                                log.info(field.getName() + "-------" + value);
                                if (!ObjectUtils.isEmpty(value)) {
                                    String newValue = value.toString().replaceAll(" ", "").replaceAll("\n", "");
                                    jsonObject.put(field.getName(), newValue);
                                } else {
                                    //是否包含必填校验注解
                                    boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
                                    if (isExcelValid) {
                                        throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                                + (analysisContext.readRowHolder().getRowIndex() + 1)
                                                + "行" + field.getAnnotation(ExcelValid.class).message()
                                        );
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                log.error("setAccessible {} ", e.getMessage());
                                throw new RuntimeException("导入参数检查失败");
                            }
                        }
                        sheetDataList.add(jsonObject);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }
                }).doRead();
        return sheetDataList;
    }


}
