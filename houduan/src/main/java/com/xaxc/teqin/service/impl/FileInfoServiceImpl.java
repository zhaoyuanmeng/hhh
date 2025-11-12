package com.xaxc.teqin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.common.util.ExcelReaderPlus;
import com.xaxc.teqin.common.util.ExcelSheetData;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.mapper.FileInfoMapper;
import com.xaxc.teqin.model.dto.*;
import com.xaxc.teqin.model.entity.FileInfo;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.service.IFileInfoService;
import com.xaxc.teqin.service.IPointInfoService;
import com.xaxc.teqin.service.ISpecialServiceArchivesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;

/**
 * <p>
 * 文件信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-18
 */
@Slf4j
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Resource
    ISpecialServiceArchivesService specialServiceArchivesService;

    @Resource
    IPointInfoService pointInfoService;

    @Resource
    SqlScriptServiceImpl sqlScriptService;

    @Override
    public boolean deleteFile(String businessId) {
        //删除记录
        FileInfo fileInfo = getOne(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getBusinessId, businessId), false);
        if (fileInfo != null) {
            if (fileInfo.getFilePath() != null) {
                File file = new File(uploadPath + fileInfo.getFilePath().replaceAll("/", "\\\\"));
                System.out.println(file.getAbsolutePath());
                if (file.exists()) {
                    file.delete();
                }
            }
            return removeById(fileInfo.getId());
        } else {
            return false;
        }
    }

    @Override
    public boolean uploadFile(MultipartFile file, String businessId, String businessType) throws IOException {
        //获取文件原始名称
        String originalFileName = file.getOriginalFilename();
        //获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + "." + extension;
        //文件大小
        Long size = file.getSize();
        //文件类型
        String type = file.getContentType();
        //处理根据日期生成目录
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = uploadPath + "/" + (StringUtils.hasText(businessType) ? businessType : "files") + "/" + dateFormat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();
        //处理文件上传
        FileUtils.writeToFile(new File(dateDir, newFileName), file.getInputStream());

        String path = new File(dateDir, newFileName).getCanonicalPath();
        path = path.substring(uploadPath.length() - 1);
        path = path.replaceAll("\\\\", "/");

        FileInfo fileInfo = new FileInfo()
                .setBusinessId(businessId)
                .setOriginalFileName(originalFileName)
                .setNewFileName(newFileName)
                .setFileExt(extension)
                .setFileSize(size)
                .setFileType(type)
                .setFilePath(path);

        return save(fileInfo);
    }

    @Override
    public String uploadFile(MultipartFile file, String groupId) throws IOException {
        //获取文件原始名称
        String originalFileName = file.getOriginalFilename();
        //获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + "." + extension;
        //文件大小
        Long size = file.getSize();
        //文件类型
        String type = file.getContentType();
        //处理根据日期生成目录
        //String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/files";
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = uploadPath + "/files/" + dateFormat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();
        //处理文件上传
        //file.transferTo(new File(dateDir, newFileName));
        FileUtils.writeToFile(new File(dateDir, newFileName), file.getInputStream());

        FileInfo fileInfo = new FileInfo()
                .setGroupId(groupId)
                .setOriginalFileName(originalFileName)
                .setNewFileName(newFileName)
                .setFileExt(extension)
                .setFileSize(size)
                .setFileType(type)
                .setFilePath(dateDirPath);
        return save(fileInfo) ? fileInfo.getId() : "";
    }

    @Override
    public String uploadFiles(MultipartFile[] files, String groupId) {
        for (MultipartFile file : files) {
            try {
                uploadFile(file, groupId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return groupId;
    }

    @Override
    public void downloadFile(String fileId, HttpServletResponse response) throws IOException {
        getFile(fileId, null, response);
    }

    @Override
    public void previewFile(String fileId, HttpServletResponse response) throws IOException {
        getFile(fileId, "inline", response);
    }

    @Override
    public List<String> getFileIds(String groupId) {
        return list(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getGroupId, groupId)).stream().map(FileInfo::getId).collect(Collectors.toList());
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        //获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + "." + extension;
        //处理根据日期生成目录
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = uploadPath + "/" + dateFormat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();
        //处理文件上传
        FileUtils.writeToFile(new File(dateDir, newFileName), file.getInputStream());
        String path = new File(dateDir, newFileName).getCanonicalPath();
        path = path.substring(uploadPath.length() - 1);
        return path.replaceAll("\\\\", "/");
    }

    @Override
    public File transferToFile(MultipartFile multipartFile, String outFilePath) {
        //选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            file = new File(outFilePath, originalFilename);    //创建临时文件
            org.apache.commons.io.FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            //删除
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // 转换文件格式
    public final static File transferToFile(MultipartFile multipartFile) {
        //选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件后缀
            String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
            file = File.createTempFile(originalFilename, prefix);    //创建临时文件
            multipartFile.transferTo(file);
            //删除
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static Map<String, Object> process(String txtStr) {
        Map<String, Object> map = JSONObject.parseObject(txtStr, new TypeReference<Map<String, Object>>() {
        });
        return map;
    }

    @Override
    public void uploadPointFile(MultipartFile multipartFile) {
        File files = transferToFile(multipartFile);
        // 查询五个基础档案的id
        Map<String, Object> fileMap = specialServiceArchivesService.selectFileId();

        ExcelReaderPlus excelReaderPlus = new ExcelReaderPlus();
        ExcelReaderPlus.ExcelData excelData = excelReaderPlus.readExcel(files);
        String fileName = excelData.getFileName();
        String[] Filesplit = fileName.split("-");
        String name = Filesplit[1].toString();
        List<ExcelSheetData> sheetData = excelData.getSheetData();
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONObject.toJSON(sheetData);

        // config 读json
        Map<String, Object> map = null;
        try {
            ClassPathResource resource = new ClassPathResource("/config.json");
            InputStream inputStream = resource.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            String jsonStr = sb.toString();
            map = process(jsonStr);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 读档案信息json
        Map<String, Object> fileJson = null;
        try {
            ClassPathResource resource = new ClassPathResource("/file.json");
            InputStream inputStream = resource.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            String jsonStr = sb.toString();
            fileJson = process(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 读档案信息poinJson
        Map<String, Object> poinJson = null;
        try {
            ClassPathResource resource = new ClassPathResource("/poin.json");
            InputStream inputStream = resource.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            String jsonStr = sb.toString();
            poinJson = process(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 创建集合 用于存储 jcxxid
        Map<String, String> jcxxid = new HashMap<>();

        for (int i = 1; i < list.size(); i++) {
            // if语句的标识--只执行一次
            boolean visited = false;
            boolean aa = false;
            boolean basebs = false;

            Map<String, Object> objectMap = list.get(i);
            List lineData = (List) objectMap.get("lineData");
            Map map1 = (Map) lineData.get(0);
            List colData = (List) map1.get("colData");
            // 根据配置文件的名字获取对应的配置文件的值
            Map JsonMap = (Map) map.get(colData.get(0));
            Map poinMap = (Map) poinJson.get(colData.get(0).toString());


            if (ObjectUtils.isNotEmpty(JsonMap) || ObjectUtils.isNotEmpty(poinMap)) {
                List columes = null;
                if (ObjectUtils.isNotEmpty(JsonMap)) {
                    columes = (List) JsonMap.get("columes");
                }
                if (JsonMap != null) {
                    /*========================================处理基本情况登记表开始=======================================*/
                    //创建基本情况登记表信息
                    BasicInfo basicInfo = new BasicInfo();
                    if (colData.get(0).toString().equals("基本情况登记表") && !basebs) {
                        System.out.println("基本情况登记表");
                        //创建存储解析数据容器
                        HashMap<String, Object> hashMap = new LinkedHashMap<>();
                        //遍历每一行数据
                        for (int x = Integer.parseInt(poinMap.get("starthang").toString()); x < Integer.parseInt(poinMap.get("biaochang").toString()); x++) {
                            //获取当前序号的行数据
                            Map data = (Map) lineData.get(x);
                            //获取行数据集合
                            List colList1 = (List) data.get("colData");
                            //序号加一对应excel序号
                            Integer xx = x + 1;
                            //获取poin.json中的所有的数据点位信息
                            Map hlMap = (Map) poinMap.get("hl");
                            //根据xx获取数据点信息
                            Object o = hlMap.get(xx.toString());
                            if (o == null) {
                                continue;
                            }
                            //创建点位集合
                            List hlList = (List) o;
                            //获取poin.json中的字段信息
                            Map zMap = (Map) poinMap.get("ziduan");
                            Object o1 = zMap.get(xx.toString());
                            //创建字段集合
                            List zList = null;
                            if (ObjectUtils.isNotEmpty(o)) {
                                zList = (List) o1;
                            }
                            //根据点位信息和字段信息拼装数据
                            if (ObjectUtils.isNotEmpty(zList) && ObjectUtils.isNotEmpty(hlList)) {
                                for (int z = 0; z < zList.size(); z++) {
                                    String keys = zList.get(z).toString();
                                    Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                    hashMap.put(keys, value);
                                }
                            }
                        }
                        //名称
                        basicInfo.setMingcheng(hashMap.get("mingcheng").toString());
                        //管辖派出所
                        basicInfo.setGuanxiapaichusuo(hashMap.get("guanxiapaichusuo").toString());
                        //单位性质
                        basicInfo.setDanweixingzhi(hashMap.get("danweixingzhi").toString());
                        //隶属关系
                        basicInfo.setLishuguanxi(hashMap.get("lishuguanxi").toString());
                        //法人
                        basicInfo.setFaren(hashMap.get("faren").toString());
                        //联系电话
                        basicInfo.setLianxidianhua(hashMap.get("lianxidianhuafr").toString());
                        //总人数
                        basicInfo.setZongrenshu(hashMap.get("zongrenshu").toString());
                        //地址
                        basicInfo.setDizhi(hashMap.get("dizhi").toString());
                        //占地面积
                        basicInfo.setZhandimianji(hashMap.get("zhandimianji").toString());
                        //经度
                        basicInfo.setJingdu(hashMap.get("jingdu").toString());
                        //纬度
                        basicInfo.setWeidu(hashMap.get("weidu").toString());
                        //内设机构
                        //创建内设机构集合
                        List<InternalMechanism> internalMechanisms = new ArrayList<>();
                        for (int j = 1; j <= 4; j++) {
                            if (StrUtil.isEmpty(hashMap.get("mingchengnj" + j).toString())) {
                                continue;
                            }
                            //创建内设机构
                            InternalMechanism internalMechanism = new InternalMechanism();
                            //部门
                            internalMechanism.setMingcheng(hashMap.get("mingchengnj" + j).toString());
                            //人数
                            internalMechanism.setRenshu(hashMap.get("renshu" + j).toString());
                            //负责人
                            internalMechanism.setFuzeren(hashMap.get("fuzerennsjg" + j).toString());
                            //联系电话
                            internalMechanism.setLianxidianhua(hashMap.get("lianxidianhuansjg" + j).toString());
                            internalMechanisms.add(internalMechanism);
                        }
                        basicInfo.setNeishejigou(internalMechanisms);
                        //主要建筑
                        //创建主要建筑集合
                        List<MajorArchitecture> majorArchitectures = new ArrayList<>();
                        for (int j = 1; j <= 4; j++) {
                            if (StrUtil.isEmpty(hashMap.get("mingchengjz" + j).toString())) {
                                continue;
                            }
                            //创建主要建筑
                            MajorArchitecture majorArchitecture = new MajorArchitecture();
                            //名称
                            majorArchitecture.setMingcheng(hashMap.get("mingchengjz" + j).toString());
                            //建筑面积
                            majorArchitecture.setJianzhumianji(hashMap.get("jianzhumianji" + j).toString());
                            //层数
                            majorArchitecture.setCengshu(hashMap.get("cengshu" + j).toString());
                            //客房
                            majorArchitecture.setKefang(hashMap.get("kefang" + j).toString());
                            //容纳人数
                            majorArchitecture.setRongnarenshu(hashMap.get("rongnarenshu" + j).toString());
                            //贵宾休息室
                            MajorArchitectureRoom majorArchitectureRoom = new MajorArchitectureRoom();
                            majorArchitectureRoom.setMingcheng(hashMap.get("mingchenggbxxs" + j).toString());
                            majorArchitectureRoom.setZuowei(hashMap.get("zuoweigbxxs" + j).toString());
                            majorArchitecture.setGuibinxiuxishi(majorArchitectureRoom);
                            //会议室
                            MajorArchitectureRoom majorArchitectureRoomHys = new MajorArchitectureRoom();
                            majorArchitectureRoomHys.setMingcheng(hashMap.get("mingchenghys" + j).toString());
                            majorArchitectureRoomHys.setZuowei(hashMap.get("zuoweihys" + j).toString());
                            majorArchitecture.setHuiyishi(majorArchitectureRoomHys);
                            //餐厅
                            MajorArchitectureRoom majorArchitectureRoomCt = new MajorArchitectureRoom();
                            majorArchitectureRoomCt.setMingcheng(hashMap.get("mingchengct" + j).toString());
                            majorArchitectureRoomCt.setZuowei(hashMap.get("zuoweict" + j).toString());
                            majorArchitecture.setCanting(majorArchitectureRoomCt);
                            majorArchitectures.add(majorArchitecture);
                        }
                        basicInfo.setZhuyaojianzhu(majorArchitectures);
                        //重要部位
                        List<ImportantPart> importantParts = new ArrayList<>();
                        for (int j = 1; j <= 4; j++) {
                            if (StrUtil.isEmpty(hashMap.get("mingchengzybw" + j).toString())) {
                                continue;
                            }
                            //创建重要部位信息
                            ImportantPart importantPart = new ImportantPart();
                            //名称
                            importantPart.setMingcheng(hashMap.get("mingchengzybw" + j).toString());
                            //所处位置
                            importantPart.setSuochuweizhi(hashMap.get("suochuweizhi" + j).toString());
                            //安全设施
                            importantPart.setAnquansheshi(hashMap.get("anquansheshi" + j).toString());
                            //负责人
                            importantPart.setFuzeren(hashMap.get("fuzerenzybw" + j).toString());
                            //联系电话
                            importantPart.setLianxidianhua(hashMap.get("lianxidianhuazybw" + j).toString());
                            importantParts.add(importantPart);
                        }
                        basicInfo.setZhongyaobuwei(importantParts);
                        //四邻情况
                        NeighborhoodInfo neighborhoodInfo = new NeighborhoodInfo();
                        for (int j = 1; j <= 4; j++) {
                            if (StrUtil.isEmpty(hashMap.get("mingchengslqk" + j).toString())) {
                                continue;
                            }
                            Neighborhood neighborhood = new Neighborhood();
                            //名称
                            neighborhood.setMingcheng(hashMap.get("mingchengslqk" + j).toString());
                            //制高点
                            neighborhood.setZhigaodian(hashMap.get("zhigaodian" + j).toString());
                            //重点人
                            neighborhood.setZhongdianren(hashMap.get("zhongdianren" + j).toString());
                            switch (j) {
                                case 1:
                                    neighborhoodInfo.setDonglin(neighborhood);
                                    break;
                                case 2:
                                    neighborhoodInfo.setNanlin(neighborhood);
                                    break;
                                case 3:
                                    neighborhoodInfo.setXilin(neighborhood);
                                    break;
                                case 4:
                                    neighborhoodInfo.setBeilin(neighborhood);
                                    break;
                            }
                        }
                        basicInfo.setSilinqingkuang(neighborhoodInfo);
                        //停车场
                        List<CarPark> carParks = new ArrayList<>();
                        for (int j = 1; j <= 3; j++) {
                            if (StrUtil.isEmpty(hashMap.get("mingchengtcc" + j).toString())) {
                                continue;
                            }
                            //创建停车场
                            CarPark carPark = new CarPark();
                            //名称
                            carPark.setMingcheng(hashMap.get("mingchengtcc" + j).toString());
                            //车位
                            carPark.setChewei(hashMap.get("chewei" + j).toString());
                            carParks.add(carPark);
                        }
                        basicInfo.setTingchechang(carParks);
                    }
                    /*========================================处理基本情况登记表结束=======================================*/


                    boolean bb = false;
                    for (int a = Integer.parseInt(JsonMap.get("startForm").toString()); a < Integer.parseInt(JsonMap.get("endRow").toString()); a++) {
                        if (ObjectUtils.isNotEmpty(lineData.get(a))) {
                            Map LineMap = (Map) lineData.get(a);
                            List colList = (List) LineMap.get("colData");
                            HashMap<String, Object> hashMap = new LinkedHashMap<>();
                            String piont = "";
                            if (colData.get(0).toString().equals("人 员 政 审 表") && !aa) {
                                // 家庭主要成员数据
                                List<Map<String, Object>> familyList = new ArrayList<>();
                                // 单位意见数据
                                List<Map<String, Object>> unitList = new ArrayList<>();
                                // 二次核查
                                List<Map<String, Object>> checkList = new ArrayList<>();

                                for (int x = Integer.parseInt(poinMap.get("starthang").toString()); x < Integer.parseInt(poinMap.get("biaochang").toString()); x++) {

                                    if (x == 13 || x == 14 || x == 15 || x == 16) {
                                        // 获取指定行的数据
                                        Map data = (Map) lineData.get(x);
                                        List colList1 = (List) data.get("colData");
                                        Integer xx = x + 1;
                                        // 获取 hl 配置的 那行那列
                                        Map hlMap = (Map) poinMap.get("hl");
                                        Object o = hlMap.get(xx.toString());
                                        List hlList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            hlList = (List) o;
                                        }

                                        Map zMap = (Map) poinMap.get("ziduan");
                                        Object o1 = zMap.get(xx.toString());
                                        List zList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            zList = (List) o1;
                                        }
                                        Map<String, Object> zhMap = new LinkedHashMap<>();

                                        // 根据两个list的长度来组装
                                        if (ObjectUtils.isNotEmpty(zList) && ObjectUtils.isNotEmpty(hlList)) {
                                            for (int z = 0; z < zList.size(); z++) {
                                                String keys = zList.get(z).toString();
                                                Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                                zhMap.put(keys, value);
                                            }
                                        }
                                        familyList.add(zhMap);


                                    } else if (x == 21 || x == 22 || x == 23) {
                                        // 获取指定行的数据
                                        Map data = (Map) lineData.get(x);
                                        List colList1 = (List) data.get("colData");
                                        Integer xx = x + 1;
                                        // 获取 hl 配置的 那行那列
                                        Map hlMap = (Map) poinMap.get("hl");
                                        Object o = hlMap.get(xx.toString());
                                        List hlList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            hlList = (List) o;
                                        }

                                        Map zMap = (Map) poinMap.get("ziduan");
                                        Object o1 = zMap.get(xx.toString());
                                        List zList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            zList = (List) o1;
                                        }
                                        Map<String, Object> zhMap = new LinkedHashMap<>();
                                        Map<String, Object> erMap = new LinkedHashMap<>();

                                        // 根据两个list的长度来组装
                                        if (ObjectUtils.isNotEmpty(zList) && ObjectUtils.isNotEmpty(hlList)) {
                                            for (int z = 0; z < zList.size(); z++) {

                                                String keys = zList.get(z).toString();
                                                if (keys.equals("danweiyijian")) {
                                                    Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                                    zhMap.put(keys, value);
                                                } else if (keys.equals("ercihechaduibiqingkuang")) {
                                                    Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                                    erMap.put(keys, value);
                                                }
                                            }
                                        }
                                        if (ObjectUtils.isNotEmpty(zhMap)) {
                                            unitList.add(zhMap);
                                        }
                                        if (ObjectUtils.isNotEmpty(erMap)) {
                                            checkList.add(erMap);
                                        }
                                    } else if (!colData.get(0).toString().equals("基本情况登记表")) {
                                        // 获取指定行的数据
                                        Map data = (Map) lineData.get(x);
                                        List colList1 = (List) data.get("colData");
                                        Integer xx = x + 1;
                                        // 获取 hl 配置的 那行那列
                                        Map hlMap = (Map) poinMap.get("hl");
                                        Object o = hlMap.get(xx.toString());
                                        List hlList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            hlList = (List) o;
                                        }

                                        Map zMap = (Map) poinMap.get("ziduan");
                                        Object o1 = zMap.get(xx.toString());
                                        List zList = null;
                                        if (ObjectUtils.isNotEmpty(o)) {
                                            zList = (List) o1;
                                        }
                                        // 根据两个list的长度来组装
                                        if (ObjectUtils.isNotEmpty(zList) && ObjectUtils.isNotEmpty(hlList)) {
                                            for (int z = 0; z < zList.size(); z++) {
                                                String keys = zList.get(z).toString();
                                                Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                                hashMap.put(keys, value);
                                            }
                                        }
                                    }
                                }
                                if (ObjectUtils.isNotEmpty(familyList)) {
                                    hashMap.put("jiatingzhuyaochengyuan", familyList);

                                }
                                if (ObjectUtils.isNotEmpty(unitList)) {
                                    hashMap.put("danweiyijian", unitList);
                                }
                                if (ObjectUtils.isNotEmpty(checkList)) {
                                    hashMap.put("ercihechaduibiqingkuang", checkList);
                                }

                                aa = true;

                            } else if (!aa) {
                                for (int c = Integer.parseInt(JsonMap.get("colIndex").toString()); c < Integer.parseInt(JsonMap.get("beginRow").toString()); c++) {

                                    // 重点人员登记表--涉恐人员登记表
                                    if (ObjectUtils.isNotEmpty(colList.get(c)) && ObjectUtils.isNotEmpty(columes.get(c)) && !visited &&
                                            (colData.get(0).toString().equals("重点人员登记表") || colData.get(0).toString().equals("涉恐重点人员登记表")
                                                    || colData.get(0).toString().equals("沿线常住外籍人员基本情况登记表") || colData.get(0).toString().equals("周边常住外籍人员基本情况登记表")
                                                    || colData.get(0).toString().equals("重点部位登记表")
                                            )) {
                                        for (int x = Integer.parseInt(poinMap.get("starthang").toString()); x < Integer.parseInt(poinMap.get("biaochang").toString()); x++) {
                                            // 获取指定行的数据
                                            Map data = (Map) lineData.get(x);
                                            List colList1 = (List) data.get("colData");
                                            Integer xx = x + 1;
                                            // 获取 hl 配置的 那行那列
                                            Map hlMap = (Map) poinMap.get("hl");
                                            Object o = hlMap.get(xx.toString());
                                            List hlList = null;
                                            if (ObjectUtils.isNotEmpty(o)) {
                                                hlList = (List) o;
                                            }

                                            Map zMap = (Map) poinMap.get("ziduan");
                                            Object o1 = zMap.get(xx.toString());
                                            List zList = null;
                                            if (ObjectUtils.isNotEmpty(o)) {
                                                zList = (List) o1;
                                            }
                                            // 根据两个list的长度来组装
                                            if (ObjectUtils.isNotEmpty(zList) && ObjectUtils.isNotEmpty(hlList)) {
                                                for (int z = 0; z < zList.size(); z++) {
                                                    String keys = zList.get(z).toString();
                                                    Object value = colList1.get(Integer.parseInt(hlList.get(z).toString()));
                                                    hashMap.put(keys, value);
                                                }
                                            }
                                        }
                                    } else if (ObjectUtils.isNotEmpty(colList.get(c)) && ObjectUtils.isNotEmpty(columes.get(c)) && !visited) {
                                        if (!columes.get(c).toString().equals("jingdu") && !columes.get(c).toString().equals("weidu") && !columes.get(c).toString().equals("danwei1") && !columes.get(c).toString().equals("xieyishuangfangmingcheng")) {
                                            hashMap.put(columes.get(c).toString(), colList.get(c));
                                        }
                                        // 处理经纬度
                                        else if (columes.get(c).toString().equals("jingdu")) {
                                            piont += "{" + "\"" + "x" + "\"" + ":" + "" + colList.get(c).toString() + ",";
                                            hashMap.put(columes.get(c).toString(), colList.get(c));
                                        } else if (columes.get(c).toString().equals("weidu")) {
                                            piont += "\"" + "y" + "\"" + ":" + "" + colList.get(c).toString() + "" + "}";
                                            hashMap.put(columes.get(c).toString(), colList.get(c));
                                        }
                                        // 处理结合部协议的字段
                                        else if (columes.get(c).toString().equals("danwei1")) {
                                            hashMap.put("xieyishuangfangmingcheng1", colList.get(c));
                                        } else if (columes.get(c).toString().equals("xieyishuangfangmingcheng")) {
                                            hashMap.put("xieyishuangfangmingcheng2", colList.get(c));
                                        }
                                    }
                                }
                            }
                            if (ObjectUtils.isNotEmpty(piont)) {
                                hashMap.put("point", piont);
                            }
//                            if (ObjectUtils.isNotEmpty(hashMap)) {
//                                System.out.println(hashMap);
//                            }

                            // 获取 fileJson 所有的key，每个key就是每个档案的名字
                            Set<String> strings = fileJson.keySet();
                            for (String key : strings) {
                                // 从读取的文件获取基本信息表的名字
                                Map<String, Object> fileJsonMap = (Map<String, Object>) fileJson.get(key);
                                String base = fileJsonMap.get("base").toString();
                                // 存入到数据库的基本信息的id -jcxxid
//                                String msg = null;
                                // 判断是否为基本信息表
                                //key + ".xls"
                                if (((List<?>) map1.get("colData")).get(0).toString().equals(base) && name.equals(key)) {
                                    // 获取基本档案id
                                    String fileId = fileMap.get(key).toString();

                                    if (base.equals("基本情况登记表") && !bb) {
                                        Map<String, Object> baseMap = new LinkedHashMap<>();
                                        baseMap.put("title", basicInfo.getMingcheng());
                                        baseMap.put("guanxiapaichusuo", basicInfo.getGuanxiapaichusuo());
                                        baseMap.put("danweixingzhi", basicInfo.getDanweixingzhi());
                                        baseMap.put("lishuguanxi", basicInfo.getLishuguanxi());
                                        baseMap.put("faren", basicInfo.getFaren());
                                        baseMap.put("lianxidianhua", basicInfo.getLianxidianhua());
                                        baseMap.put("zongrenshu", basicInfo.getZongrenshu());
                                        baseMap.put("dizhi", basicInfo.getDizhi());
                                        baseMap.put("zhandimianji", basicInfo.getZhandimianji());

                                        // 处理经纬度
                                        piont = "{" + "\"" + "x" + "\"" + ":" + "" + basicInfo.getJingdu() + ","
                                                + "\"" + "y" + "\"" + ":" + "" + basicInfo.getWeidu() + "" + "}";


                                        baseMap.put("point", piont);
                                        baseMap.put("jingdu", basicInfo.getJingdu());
                                        baseMap.put("weidu", basicInfo.getWeidu());

                                        // 获取基本情况表的 点位id
                                        String baseName = fileJsonMap.get("baseName").toString();
                                        Map<String, Object> basemap = specialServiceArchivesService.getArchivesId(fileId, baseName);
                                        String baseId = basemap.get("id").toString();

                                        // 组装保存的数据
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.putAll(baseMap);
                                        PointInfo pointInfoModel = new PointInfo();
                                        pointInfoModel.setParentTypeId(fileId);
                                        pointInfoModel.setData(jsonObject);
                                        pointInfoModel.setChildTypeId(baseId);
                                        String result = pointInfoService.saveArchivesInfo(pointInfoModel);
                                        jcxxid.put("jdxxid", result);

                                        // 保存内设机构
                                        List<InternalMechanism> neishejigouList = basicInfo.getNeishejigou();
                                        // 获取 重点点位id
                                        List pointInfo = (List) fileJsonMap.get("basePoint");
                                        String pointName = pointInfo.get(0).toString();
                                        Map<String, Object> pointmap = specialServiceArchivesService.getArchivesId(fileId, pointName);
                                        String archivesId = pointmap.get("id").toString();

                                        for (InternalMechanism neishejigou : neishejigouList) {
                                            // 组装数据
                                            JSONObject json = new JSONObject();
                                            // 将对象转为map
                                            Map<String, Object> stringObjectMap = JSON.parseObject(JSON.toJSONString(neishejigou), new TypeReference<Map<String, Object>>() {
                                            });
                                            json.putAll(stringObjectMap);
                                            PointInfo pointInfoModel1 = new PointInfo();
                                            pointInfoModel1.setParentTypeId(fileId);
                                            pointInfoModel1.setJcxxId(jcxxid.get("jdxxid"));
                                            pointInfoModel1.setChildTypeId(archivesId);
                                            pointInfoModel1.setData(json);
                                            String pointResult = pointInfoService.saveArchivesInfo(pointInfoModel1);
                                        }

                                        // 保存重要部位
                                        List<ImportantPart> zhongyaobuweiList = basicInfo.getZhongyaobuwei();
                                        // 获取 重点点位id
                                        String pointName2 = pointInfo.get(2).toString();
                                        Map<String, Object> pointmap2 = specialServiceArchivesService.getArchivesId(fileId, pointName2);
                                        String archivesId2 = pointmap2.get("id").toString();

                                        for (ImportantPart zhongyaobuwei : zhongyaobuweiList) {
                                            // 组装数据
                                            JSONObject json = new JSONObject();
                                            // 将对象转为map
                                            Map<String, Object> stringObjectMap = JSON.parseObject(JSON.toJSONString(zhongyaobuwei), new TypeReference<Map<String, Object>>() {
                                            });
                                            json.putAll(stringObjectMap);
                                            PointInfo pointInfoModel1 = new PointInfo();
                                            pointInfoModel1.setParentTypeId(fileId);
                                            pointInfoModel1.setJcxxId(jcxxid.get("jdxxid"));
                                            pointInfoModel1.setChildTypeId(archivesId2);
                                            pointInfoModel1.setData(json);
                                            String pointResult = pointInfoService.saveArchivesInfo(pointInfoModel1);
                                        }

                                        // 保存主要建筑
                                        List<MajorArchitecture> zhuyaojianzhuList = basicInfo.getZhuyaojianzhu();
                                        // 获取 重点点位id
                                        String pointName1 = pointInfo.get(1).toString();
                                        Map<String, Object> pointmap1 = specialServiceArchivesService.getArchivesId(fileId, pointName1);
                                        String archivesId1 = pointmap1.get("id").toString();

                                        for (MajorArchitecture zhuyaojianzhu : zhuyaojianzhuList) {
                                            // 组装数据
                                            JSONObject json = new JSONObject();
                                            // 将对象转为map
                                            Map<String, Object> stringObjectMap = JSON.parseObject(JSON.toJSONString(zhuyaojianzhu), new TypeReference<Map<String, Object>>() {
                                            });
                                            json.putAll(stringObjectMap);
                                            PointInfo pointInfoModel1 = new PointInfo();
                                            pointInfoModel1.setParentTypeId(fileId);
                                            pointInfoModel1.setJcxxId(jcxxid.get("jdxxid"));
                                            pointInfoModel1.setChildTypeId(archivesId1);
                                            pointInfoModel1.setData(json);
                                            String pointResult = pointInfoService.saveArchivesInfo(pointInfoModel1);
                                        }

                                        // 保存四邻情况
                                        NeighborhoodInfo silinqingkuangList = basicInfo.getSilinqingkuang();

                                        if (!Objects.isNull(silinqingkuangList.getDonglin())) {
                                            silinqingkuangList.getDonglin().setFangwei("东邻");
                                        }
                                        if (!Objects.isNull(silinqingkuangList.getXilin())) {
                                            silinqingkuangList.getXilin().setFangwei("西邻");
                                        }
                                        if (!Objects.isNull(silinqingkuangList.getNanlin())) {
                                            silinqingkuangList.getNanlin().setFangwei("南邻");
                                        }
                                        if (!Objects.isNull(silinqingkuangList.getBeilin())) {
                                            silinqingkuangList.getBeilin().setFangwei("北邻");
                                        }

                                        Neighborhood donglin = silinqingkuangList.getDonglin();
                                        Neighborhood beilin = silinqingkuangList.getBeilin();
                                        Neighborhood nanlin = silinqingkuangList.getNanlin();
                                        Neighborhood xilin = silinqingkuangList.getXilin();

                                        // 获取 重点点位id
                                        String pointName3 = pointInfo.get(3).toString();
                                        Map<String, Object> pointmap3 = specialServiceArchivesService.getArchivesId(fileId, pointName3);
                                        String archivesId3 = pointmap3.get("id").toString();

                                        // 组装数据
                                        JSONObject donglinjson = new JSONObject();
                                        // 将对象转为map
                                        Map<String, Object> donglinMap = JSON.parseObject(JSON.toJSONString(donglin), new TypeReference<Map<String, Object>>() {
                                        });
                                        Map<String, Object> beilintMap = JSON.parseObject(JSON.toJSONString(beilin), new TypeReference<Map<String, Object>>() {
                                        });
                                        Map<String, Object> nanlinMap = JSON.parseObject(JSON.toJSONString(nanlin), new TypeReference<Map<String, Object>>() {
                                        });
                                        Map<String, Object> xilinMap = JSON.parseObject(JSON.toJSONString(xilin), new TypeReference<Map<String, Object>>() {
                                        });


                                        if (!CollectionUtils.isEmpty(donglinMap)) {
                                            PointInfo donglinpointInfoModel = new PointInfo();
                                            donglinjson.putAll(donglinMap);
                                            donglinpointInfoModel.setParentTypeId(fileId);
                                            donglinpointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                            donglinpointInfoModel.setChildTypeId(archivesId3);
                                            donglinpointInfoModel.setData(donglinjson);
                                            String donglinResult = pointInfoService.saveArchivesInfo(donglinpointInfoModel);
                                        }

                                        if (!CollectionUtils.isEmpty(beilintMap)) {
                                            JSONObject beilinjson = new JSONObject();
                                            beilinjson.putAll(beilintMap);
                                            PointInfo beilintpointInfoModel = new PointInfo();
                                            beilintpointInfoModel.setParentTypeId(fileId);
                                            beilintpointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                            beilintpointInfoModel.setChildTypeId(archivesId3);
                                            beilintpointInfoModel.setData(donglinjson);
                                            beilintpointInfoModel.setData(beilinjson);
                                            String beilinResult = pointInfoService.saveArchivesInfo(beilintpointInfoModel);
                                        }

                                        if (!CollectionUtils.isEmpty(nanlinMap)) {
                                            JSONObject nanlinjson = new JSONObject();
                                            nanlinjson.putAll(nanlinMap);
                                            PointInfo nanlinpointInfoModel = new PointInfo();
                                            nanlinpointInfoModel.setParentTypeId(fileId);
                                            nanlinpointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                            nanlinpointInfoModel.setChildTypeId(archivesId3);
                                            nanlinpointInfoModel.setData(donglinjson);
                                            nanlinpointInfoModel.setData(nanlinjson);
                                            String nanlinResult = pointInfoService.saveArchivesInfo(nanlinpointInfoModel);
                                        }

                                        if (!CollectionUtils.isEmpty(xilinMap)) {
                                            JSONObject xilinjson = new JSONObject();
                                            xilinjson.putAll(xilinMap);
                                            PointInfo xilinpointInfoModel = new PointInfo();
                                            xilinpointInfoModel.setParentTypeId(fileId);
                                            xilinpointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                            xilinpointInfoModel.setChildTypeId(archivesId3);
                                            xilinpointInfoModel.setData(donglinjson);
                                            xilinpointInfoModel.setData(xilinjson);
                                            String xilinResult = pointInfoService.saveArchivesInfo(xilinpointInfoModel);
                                        }

                                        // 保存停车场数据
                                        List<CarPark> tingchechangList = basicInfo.getTingchechang();
                                        // 获取 重点点位id
                                        String pointName4 = pointInfo.get(4).toString();
                                        Map<String, Object> pointmap4 = specialServiceArchivesService.getArchivesId(fileId, pointName4);
                                        String archivesId4 = pointmap4.get("id").toString();

                                        for (CarPark tingchechang : tingchechangList) {
                                            // 组装数据
                                            JSONObject json = new JSONObject();
                                            // 将对象转为map
                                            Map<String, Object> stringObjectMap = JSON.parseObject(JSON.toJSONString(tingchechang), new TypeReference<Map<String, Object>>() {
                                            });
                                            json.putAll(stringObjectMap);
                                            PointInfo pointInfoModel1 = new PointInfo();
                                            pointInfoModel1.setParentTypeId(fileId);
                                            pointInfoModel1.setJcxxId(jcxxid.get("jdxxid"));
                                            pointInfoModel1.setChildTypeId(archivesId4);
                                            pointInfoModel1.setData(json);
                                            String pointResult = pointInfoService.saveArchivesInfo(pointInfoModel1);
                                        }
                                        bb = true;
                                    } else if (!base.equals("基本情况登记表")) {
                                        // 获取基本情况表的 点位id
                                        String baseName = fileJsonMap.get("baseName").toString();
                                        Map<String, Object> pointmap = specialServiceArchivesService.getArchivesId(fileId, baseName);
                                        String archivesId = pointmap.get("id").toString();
                                        // 将获取的ex表的基础数据存入mongo
                                        if (ObjectUtils.isNotEmpty(hashMap)) {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.putAll(hashMap);
                                            PointInfo pointInfoModel = new PointInfo();
                                            pointInfoModel.setParentTypeId(fileId);
                                            pointInfoModel.setData(jsonObject);
                                            pointInfoModel.setChildTypeId(archivesId);
                                            String result = pointInfoService.saveArchivesInfo(pointInfoModel);
                                            if (ObjectUtils.isNotEmpty(result)) {
                                                jcxxid.put("jdxxid", result);
                                            }
                                        }
                                    }
                                }
                                // 判断基本表id部位null  存入重点点位数据
                                String[] split = name.split("\\.");
                                //System.out.println(split);
                                name = split[0];
                                if (ObjectUtils.isNotEmpty(jcxxid) && name.equals(key)) {
                                    Map m = (Map) lineData.get(a);
                                    List point = (List) fileJsonMap.get("point");
                                    if (ObjectUtils.isNotEmpty(point)) {
                                        for (int p = 0; p < point.size(); p++) {
                                            // 每个重点点位的名字
                                            String s = point.get(p).toString();
                                            // 判断点位的名字获取对应档案的数据
                                            if (colData.get(0).toString().equals(s) && ObjectUtils.isNotEmpty(hashMap) &&
                                                    (colData.get(0).toString().equals("重点人员登记表") || colData.get(0).toString().equals("涉恐重点人员登记表")
                                                            || colData.get(0).toString().equals("沿线常住外籍人员基本情况登记表") || colData.get(0).toString().equals("周边常住外籍人员基本情况登记表")
                                                            || colData.get(0).toString().equals("重点部位登记表") && !visited)) {
                                                // 获取基本档案id
                                                String fileId = fileMap.get(key).toString();
                                                // 获取 重点点位id
                                                List pointInfo = (List) fileJsonMap.get("info");
                                                String pointName = pointInfo.get(p).toString();
                                                Map<String, Object> pointmap = specialServiceArchivesService.getArchivesId(fileId, pointName);
                                                String archivesId = pointmap.get("id").toString();

                                                // 组装数据
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.putAll(hashMap);
                                                PointInfo pointInfoModel = new PointInfo();
                                                pointInfoModel.setParentTypeId(fileId);
                                                pointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                                pointInfoModel.setChildTypeId(archivesId);
                                                pointInfoModel.setData(jsonObject);
                                                String result = pointInfoService.saveArchivesInfo(pointInfoModel);
                                                visited = true;


                                            } else if (colData.get(0).toString().equals(s) && ObjectUtils.isNotEmpty(hashMap) && !colData.get(0).toString().equals("基本情况登记表")) {
                                                // 获取基本档案id
                                                String fileId = fileMap.get(key).toString();
                                                // 获取 重点点位id
                                                List pointInfo = (List) fileJsonMap.get("info");
                                                String pointName = pointInfo.get(p).toString();
                                                Map<String, Object> pointmap = specialServiceArchivesService.getArchivesId(fileId, pointName);
                                                String archivesId = pointmap.get("id").toString();
                                                // 将重点点位数据存入mongo +（基本档案id 重点点位id jcxxid）
                                                JSONObject jsonObject = new JSONObject();
                                                // 把weizhi转为double类型 --- 这是筛选的字段
                                                if (ObjectUtils.isNotEmpty(hashMap.get("weizhi"))) {
                                                    double weizhi = Double.parseDouble(hashMap.get("weizhi").toString());
                                                    hashMap.put("weizhi", weizhi);
                                                }
                                                jsonObject.putAll(hashMap);
                                                PointInfo pointInfoModel = new PointInfo();
                                                pointInfoModel.setParentTypeId(fileId);
                                                pointInfoModel.setJcxxId(jcxxid.get("jdxxid"));
                                                pointInfoModel.setChildTypeId(archivesId);
                                                pointInfoModel.setData(jsonObject);
                                                String result = pointInfoService.saveArchivesInfo(pointInfoModel);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public List<String> splitAndReplaceInFile(File inputFile, String outputPath, List<String> targets, String replacement, long splitLineNum) {
        List<String> sqlFiles = new ArrayList<>();
        String inputFilePath = inputFile.getPath();
        boolean splitFlag = inputFile.length() > 200 * 1024 * 1024;
        String ext = inputFilePath.substring(inputFilePath.lastIndexOf("."));
        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf("."));
        int index = 1;
        new File(outputPath).delete();
        new File(outputPath).mkdir();
        String outputFilePath = outputPath + fileName + index + ext;
        sqlFiles.add(outputFilePath);
        int i = 0;
        BufferedWriter writer = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            writer = new BufferedWriter(new FileWriter(outputFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                for (String target : targets) {
                    line = line.replaceAll(target, replacement);
                }
                i++;
                writer.write(line);
                writer.newLine();
                if (i == splitLineNum && splitFlag) {
                    writer.close();
                    i = 0;
                    index++;
                    outputFilePath = outputPath + fileName + index + ext;
                    sqlFiles.add(outputFilePath);
                    writer = new BufferedWriter(new FileWriter(outputFilePath));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlFiles;
    }

    @Override
    public String uploadFileAndGetFilePath(MultipartFile file) throws IOException {
        //获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + "." + extension;
//        //文件大小
//        Long size = file.getSize();
//        //文件类型
//        String type = file.getContentType();
        //处理根据日期生成目录
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = uploadPath + "/" + "files" + "/" + dateFormat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();
        //处理文件上传
        FileUtils.writeToFile(new File(dateDir, newFileName), file.getInputStream());
        String path = new File(dateDir, newFileName).getCanonicalPath();
        return path;
    }

    public void getFile(String fileId, String openType, HttpServletResponse response) throws IOException {
        FileInfo fileInfo = getById(fileId);
        FileInputStream is = new FileInputStream(fileInfo.getFilePath() + "/" + fileInfo.getNewFileName());
        response.setHeader("content-disposition", "inline".equals(openType) ? "inline" : "attachment" + ";fileName=" + URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8));
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}
