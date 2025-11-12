package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xaxc.teqin.model.entity.TaskArchivesData;
import com.xaxc.teqin.mapper.TaskArchivesDataMapper;
import com.xaxc.teqin.service.ITaskArchivesDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-01
 */
@Service
public class TaskArchivesDataServiceImpl extends ServiceImpl<TaskArchivesDataMapper, TaskArchivesData> implements ITaskArchivesDataService {


    static int[] levelCounters = new int[100];

    private static String toChineseNumber(int num) {
        if (num == 0) return "零";

        String[] units = {"", "十", "百", "千"};
        String[] digits = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        StringBuilder sb = new StringBuilder();
        int unitIndex = 0;

        while (num > 0) {
            int current = num % 10;
            num /= 10;

            // 处理单位
            if (current != 0) {
                sb.insert(0, units[unitIndex]);
                sb.insert(0, digits[current]);
            } else {
                // 避免重复零（如一千零百 -> 一千）
                if (sb.length() > 0 && !sb.substring(0, 1).equals("零")) {
                    sb.insert(0, "零");
                }
            }

            // 处理万级单位（扩展支持更大数字可继续添加）
            if (unitIndex == 3 && num > 0) {
                sb.insert(0, "万");
            }

            unitIndex++;
        }

        // 处理开头和结尾的零
        String result = sb.toString()
                .replaceAll("零+", "零")
                .replaceAll("^零", "")
                .replaceAll("零$", "");

        // 特殊处理10-19的情况（如一十 -> 十）
        if (result.startsWith("一十")) {
            result = result.substring(1);
        }
        return result;
    }

    public static String getXWPFParagraphText(XWPFParagraph p, boolean getTitleFlag) {
        if (p.getStyleID() != null) {
            int index = Integer.parseInt(p.getStyleID());
            levelCounters[index]++;
            for (int i = index + 1; i < levelCounters.length; i++) {
                levelCounters[i] = 0;
            }
            String number = "";
            if ("chineseCounting".equals(p.getNumFmt())) {
                if (p.getNumLevelText().startsWith("%1")) {
                    number = p.getNumLevelText().replace("%1", toChineseNumber(levelCounters[index]));
                }
            }
            return getTitleFlag ? number + p.getParagraphText() : number;
            //标题
            //String number = ""; //p.getNumLevelText().replace("%1", "chineseCounting".equals(p.getNumFmt()) ? toChineseNumber(levelCounters[index]) : levelCounters[index] + "");
            //
            // return getTitleFlag ? number + p.getParagraphText() : number;
        }
        return "";
//        else {
//            return getTitleFlag ? "" : p.getParagraphText();
//        }
    }

    public static Map<String, Object> getParagraphFormat(XWPFParagraph p, String id, String parentId) {
        if (p == null) {
            Map<String, Object> paraFormat = new LinkedHashMap<>();
            paraFormat.put("content", new ArrayList<>());
            paraFormat.put("id", id);
            paraFormat.put("parentId", parentId);
            return paraFormat;
        }
        String number = getXWPFParagraphText(p, false);
        // 获取带格式的文本内容
        List<Map<String, Object>> runs = new ArrayList<>();
        if (StringUtils.hasText(number)) {
            Map<String, Object> runStyle = new HashMap<>();
            runStyle.put("text", number);
            runs.add(runStyle);
        }
        for (XWPFRun run : p.getRuns()) {
            Map<String, Object> runStyle = new HashMap<>();
            // 文本内容处理换行符
            String text = run.text()
                    .replace("\r", "<softReturn/>")  // 软回车
                    .replace("\n", "<hardReturn/>"); // 硬回车
            if (runs.isEmpty()) {
                text = text.replaceAll(" ", "&nbsp;");
            }
            runStyle.put("text", text);
            runStyle.put("fontFamily", run.getFontFamily());
            runStyle.put("fontSize", run.getFontSize()); // 单位：半磅（如24表示12pt）
            runStyle.put("bold", run.isBold());
            runStyle.put("italic", run.isItalic());
            runStyle.put("color", run.getColor());
            runs.add(runStyle);
        }
        Map<String, Object> paraFormat = new LinkedHashMap<>();
        // 获取段落格式
        paraFormat.put("alignment", p.getAlignment() != null ? p.getAlignment().name() : "LEFT");
        paraFormat.put("leftIndent", p.getIndentFromLeft());  // 左缩进（单位：缇，1cm=567缇）
        paraFormat.put("firstLineIndent", p.getIndentationFirstLine()); // 首行缩进
        paraFormat.put("spacingAfter", p.getSpacingAfter());  // 段后间距
        paraFormat.put("spacingBefore", p.getSpacingBefore());// 段前间距
        paraFormat.put("spacingBetween", p.getSpacingBetween());// 行间距
        paraFormat.put("content", runs);
        paraFormat.put("id", id);
        paraFormat.put("parentId", parentId);
        return paraFormat;
    }

    /**
     * 一级标题 文档设置样式标题1
     * 二级标题 文档设置样式标题2
     * 标题前不要使用自动编号
     * @param filePath
     * @param businessId
     * @throws IOException
     */
    @Override
    public void parseToDb(String filePath, String businessId,String fileName) throws IOException {
        List<TaskArchivesData> dataList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        // 创建XWPFDocument对象
        XWPFDocument document = new XWPFDocument(fis);
        // 存储解析结果
        String title = "";
        String titleId = "";
        Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<>();
        List<Map<String, Object>> formattedContent = new ArrayList<>();

        String secondTitle = "";
        String secondTitleId = "";
        List<Map<String, Object>> secondFormattedContent = new ArrayList<>();
        String subject = "";
        // 遍历所有段落
        for (XWPFParagraph p : document.getParagraphs()) {
            if (p.getStyleID() != null) {
                XWPFStyle xwpfStyle = document.getStyles().getStyle(p.getStyleID());
                String styleName = xwpfStyle.getName();

                if ("heading 1".equals(styleName) || "标题 1".equals(styleName)) {
                    formattedContent = new ArrayList<>();
                    secondFormattedContent = new ArrayList<>();
                    secondTitle = "";
                    //标题
                    title = getXWPFParagraphText(p, true);
                    titleId = IdWorker.getIdStr();
                } else if ("heading 2".equals(styleName) || "标题 2".equals(styleName)) {
                    secondFormattedContent = new ArrayList<>();
                    //标题
                    secondTitle = getXWPFParagraphText(p, true);
                    secondTitleId = IdWorker.getIdStr();
                    if (formattedContent.isEmpty()) {
                        formattedContent.add(getParagraphFormat(null, titleId, ""));
                        if(StringUtils.hasText(title)) {
                            dataMap.put(title, formattedContent);
                        }
                    }
                } else {
                    if (StringUtils.hasText(secondTitle)) {
                        secondFormattedContent.add(getParagraphFormat(p, secondTitleId, titleId));
                        dataMap.put(secondTitle, secondFormattedContent);
                    } else if (StringUtils.hasText(title)) {
                        formattedContent.add(getParagraphFormat(p, titleId, ""));
                        dataMap.put(title, formattedContent);
                    }
                }
            } else {
                //正文
                if (StringUtils.hasText(secondTitle)) {
                    secondFormattedContent.add(getParagraphFormat(p, secondTitleId, titleId));
//                    String htmlContent = toFormattedHtml(secondFormattedContent);
                    dataMap.put(secondTitle, secondFormattedContent);
                } else if (StringUtils.hasText(title)) {
                    formattedContent.add(getParagraphFormat(p, titleId, ""));
//                    String htmlContent = toFormattedHtml(formattedContent);
                    dataMap.put(title, formattedContent);
                } else if (StringUtils.hasText(subject) && !p.getText().equals(subject)) {
                    subject += p.getParagraphText();
//                    String htmlContent = toFormattedHtml(secondFormattedContent);
//                    String asdf = subject;
                    //简介
//                    String htmlContent = toFormattedHtml(List.of(getParagraphFormat(p)));
//                    try {
//                        Files.write(Paths.get("info.html"), htmlContent.getBytes());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                } else {
                    subject = p.getParagraphText();
                }
            }
        }

        dataMap.forEach((k, v) -> {
            String htmlContent = toFormattedHtml(v);
            TaskArchivesData taskArchivesData = new TaskArchivesData();
            taskArchivesData.setId(v.get(0).get("id").toString());
            taskArchivesData.setParentId(v.get(0).get("parentId").toString());
            taskArchivesData.setBusinessId(businessId);
            taskArchivesData.setTitle(k);
            taskArchivesData.setContent(htmlContent);
            dataList.add(taskArchivesData);
        });

        if (!CollectionUtils.isEmpty(dataList) || StringUtils.hasText(subject)) {
            // 先删除之前的businessId关联的数据
            remove(new LambdaQueryWrapper<TaskArchivesData>()
                    .eq(TaskArchivesData::getBusinessId, businessId));

            if(!CollectionUtils.isEmpty(dataList)){
                saveBatch(dataList);
            }

            if (!StringUtils.hasText(subject)) {
                subject= fileName;
            }
            TaskArchivesData taskArchivesData = new TaskArchivesData();
            taskArchivesData.setId(IdWorker.getIdStr());
            taskArchivesData.setBusinessId(businessId);
            taskArchivesData.setParentId("");
            taskArchivesData.setTitle("标题");
            taskArchivesData.setContent(subject);
            save(taskArchivesData);
        }
    }

    @Override
    public List<TaskArchivesData> getArchivesData(String businessId) {
        List<TaskArchivesData> taskArchivesDataList = list(new LambdaQueryWrapper<TaskArchivesData>()
                .eq(TaskArchivesData::getBusinessId, businessId)
                .eq(TaskArchivesData::getParentId, "")
                .orderByAsc(TaskArchivesData::getCreateTime));
        taskArchivesDataList.forEach(taskArchivesData -> {
            taskArchivesData.setChildren(list(new LambdaQueryWrapper<TaskArchivesData>()
                    .eq(TaskArchivesData::getBusinessId, businessId)
                    .eq(TaskArchivesData::getParentId, taskArchivesData.getId())
                    .orderByAsc(TaskArchivesData::getCreateTime)));
        });
        return taskArchivesDataList;
    }

    // 单位转换方法
    private static double toCm(int twips) {
        return twips / 567.0; // 1cm = 567twip
    }

    private static double toPt(int halfPoints) {
        return halfPoints / 2.0; // 半磅转pt
    }

    private static String toFormattedHtml(List<Map<String, Object>> content) {
        StringBuilder html = new StringBuilder("<div>");
        for (Map<String, Object> para : content) {
            List<Map<String, Object>> runs = (List<Map<String, Object>>) para.get("content");
            if (!CollectionUtils.isEmpty(runs)) {
                // 段落样式
                html.append("<div style='")
                        .append("text-align:").append(para.get("alignment")).append(";")
                        .append("margin-left:").append(toCm((int) para.get("leftIndent"))).append("cm;")
                        .append("text-indent:").append(toCm((int) para.get("firstLineIndent"))).append("cm;")
                        .append("line-height:").append(21).append("pt;")
                        .append("margin-bottom:").append(toPt((int) para.get("spacingAfter"))).append("pt;")
                        .append("'>");
                // 处理文字片段
                for (Map<String, Object> run : runs) {
                    String text = ((String) run.get("text"))
                            .replace("<softReturn/>", "<br/>")
                            .replace("<hardReturn/>", "<br/>");
                    html.append("<span style='")
                            .append("font-size:").append(12).append("pt;")
                            .append("'>")
                            .append(text)
                            .append("</span>");
                }
                html.append("</div>");
            }
        }
        html.append("</div>");
        return html.toString().equals("<div></div>") ? "" : html.toString();
    }
}
