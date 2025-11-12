package com.xaxc.teqin.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.dto.DeployPoliceData;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.SceneInfo;
import com.xaxc.teqin.service.IFileInfoService;
import com.xaxc.teqin.service.ISceneInfoService;
import com.xaxc.teqin.service.impl.SqlScriptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 场景表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
@RestController
@RequestMapping("/scene-info")
public class SceneInfoController {

    @Autowired
    private ISceneInfoService sceneInfoService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody SceneInfo scene) {
        Assert.hasText(scene.getSceneName(), "sceneName不能为空");
        Assert.hasText(scene.getType(), "type不能为空");
        return sceneInfoService.addScene(scene);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody SceneInfo scene) {
        Assert.hasText(scene.getId(), "ID不能为空");
        return sceneInfoService.updateScene(scene);
    }

    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return sceneInfoService.deleteScene(id) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody SceneInfo scene) {
        LambdaQueryWrapper<SceneInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(scene.getSceneName())) {
            lambdaQueryWrapper.like(SceneInfo::getSceneName, scene.getSceneName());
        }
        if (StringUtils.hasText(scene.getType())) {
            lambdaQueryWrapper.eq(SceneInfo::getType, scene.getType());
        }
        if (StringUtils.hasText(scene.getTaskId())) {
            lambdaQueryWrapper.eq(SceneInfo::getTaskId, scene.getTaskId());
        }
        lambdaQueryWrapper.eq(SceneInfo::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(SceneInfo::getCreateTime);
        return ResponseResult.success(sceneInfoService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getListByTaskId")
    public ResponseResult getList(@RequestParam(value = "taskId") String taskId) {
        LambdaQueryWrapper<SceneInfo> lambdaQueryWrapper = new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getTaskId, taskId)
                .eq(SceneInfo::getDeleteFlag, 0)
                .orderByAsc(SceneInfo::getBeginTime).orderByAsc(SceneInfo::getEndTime);
        return ResponseResult.success(sceneInfoService.list(lambdaQueryWrapper));
    }

    @GetMapping("/getById")
    public ResponseResult getById(@RequestParam("id") String id) {
        return ResponseResult.success(sceneInfoService.geSceneById(id));
    }

    @GetMapping("/getSceneDetail")
    public ResponseResult getSceneDetail(@RequestParam("id") String id) {
        return ResponseResult.success(sceneInfoService.getDetail(id));
    }

    @GetMapping("/getDetailByTaskIdAndSceneName")
    public ResponseResult getDetailByName(@RequestParam("taskId") String taskId, @RequestParam("sceneName") String sceneName) {
        return ResponseResult.success(sceneInfoService.getDetailByTaskIdAndSceneName(taskId, sceneName));
    }

    @GetMapping("/getSceneListByTaskIdAndSceneName")
    public ResponseResult getSceneListByTaskIdAndSceneName(@RequestParam("taskId") String taskId, @RequestParam("sceneName") String sceneName) {
        return ResponseResult.success(sceneInfoService.getSceneListByTaskIdAndSceneName(taskId, sceneName));
    }


    @PostMapping("/getPage")
    public ResponseResult<Page<SceneInfo>> getPage(@RequestBody PageCondition<SceneInfo> pageCondition) {
        Page<SceneInfo> page = pageCondition.getPage();
        SceneInfo scene = pageCondition.getEntity();
        LambdaQueryWrapper<SceneInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(scene.getSceneName())) {
            queryWrapper.like(SceneInfo::getSceneName, scene.getSceneName());
        }
        if (StringUtils.hasText(scene.getTaskId())) {
            queryWrapper.eq(SceneInfo::getTaskId, scene.getTaskId());
        }
        queryWrapper.eq(SceneInfo::getDeleteFlag, 0);
        queryWrapper.orderByDesc(SceneInfo::getCreateTime);

        return ResponseResult.success(sceneInfoService.page(page, queryWrapper));
    }

    /**
     * 获取场景基本规划数据
     *
     * @param sceneId
     * @return
     */
    @GetMapping("/getBasicPlanOfScene")
    public ResponseResult getBasicPlanOfScene(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getBasicPlanOfScene(sceneId));
    }


    /**
     * 获取场景下标绘数据
     *
     * @param sceneId
     * @return
     */
    @GetMapping("/getDrawDataOfScene")
    public ResponseResult getDrawDataOfTask(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getDrawDataOfScene(sceneId));
    }

    /**
     * 获取场景下标绘数据
     *
     * @param sceneIds
     * @return
     */
    @GetMapping("/getBasicDrawDataOfScene")
    public ResponseResult getBasicDrawDataOfScene(@RequestParam("sceneIds") List<String> sceneIds) {
        return ResponseResult.success(sceneInfoService.getBasicDrawDataOfScene(sceneIds));
    }


    /**
     * 获取哨位警力（一张图）
     *
     * @param sceneId
     * @return
     */
    @GetMapping("/getPostPoliceDataOfScene")
    public ResponseResult getPostPoliceDataOfScene(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getPostPoliceDataOfScene(sceneId));
    }

    /**
     * 获取场景资源树
     *
     * @return
     */
    @GetMapping("/getSceneResources")
    public ResponseResult getSceneResources(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getMultiSceneResourceTree(Arrays.asList(sceneId)));
    }

    /**
     * 根据条件查询路线
     *
     * @return
     */
    @PostMapping("/querySceneResourcesByCondition")
    public ResponseResult querySceneResourcesByCondition(@RequestBody DrawData drawData) {
        return ResponseResult.success(sceneInfoService.querySceneResourcesByCondition(drawData));
    }


    @RequestMapping("/exportSceneResourcesToZip")
    public void exportSceneResourcesToZip(HttpServletResponse response,@RequestBody DrawData drawData)throws IOException {
        List<BasicDataDTO> list = sceneInfoService.querySceneResourcesByCondition(drawData);
        ByteArrayInputStream zipInputStream = zipFiles(list);
        exportZip(response,zipInputStream);
    }


    private ByteArrayInputStream zipFiles( List<BasicDataDTO> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (BasicDataDTO resource : list) {
                StringBuffer text = new StringBuffer();
                text.append(resource.getCoordinates());
                zipOutputStream.putNextEntry(new ZipEntry(resource.getName()+resource.getId()+".txt"));
                zipOutputStream.write(text.toString().getBytes("UTF-8"));
                zipOutputStream.closeEntry();
            }
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }



    /*
        导出单个文件的方法
        拼接字符串
     * @author
     * @param
     * @return
     */
//    @PostMapping("/exportSceneResourcesByCondition")
    public void exportSceneResourcesByCondition(HttpServletResponse response, @RequestBody DrawData drawData){
        //获取数据
        List<BasicDataDTO> list = sceneInfoService.querySceneResourcesByCondition(drawData);
        StringBuffer text = new StringBuffer();
        //拼接字符串
        for(BasicDataDTO item:list){
            text.append(item.getCoordinates());
            text.append("\r\n");//换行字符
            text.append("\r\n");//换行字符
        }
        exportTxt(response,text.toString());
    }

    /* 导出txt文件
     * @author
     * @param    response
     * @param    text 导出的字符串
     * @return
     */
    public void exportTxt(HttpServletResponse response,String text){
        response.setCharacterEncoding("utf-8");
        //设置响应的内容类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition","attachment;filename="
                + genAttachmentFileName( "文件名称", "JSON_FOR_UCC_")//设置名称格式，没有这个中文名称无法显示
                + ".txt");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {try {
            buff.close();
            outStr.close();
        } catch (Exception e) {
            //LOGGER.error("关闭流对象出错 e:{}",e);
        }
        }
    }

    /* 导出txt文件
     * @author
     * @param    response
     * @param    text 导出的字符串
     * @return
     */
    public void exportZip(HttpServletResponse response,ByteArrayInputStream zipInputStream ){
        response.setCharacterEncoding("utf-8");
        //设置响应的内容类型
        response.setContentType("application/zip");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition","attachment;filename="
                + genAttachmentFileName( "files", "JSON_FOR_UCC_")//设置名称格式，没有这个中文名称无法显示
                + ".zip");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(zipInputStream.readAllBytes());
            buff.flush();
            buff.close();
        } catch (Exception e) {
            System.out.println("导出文件文件出错:{}"+e);
        } finally {try {
            buff.close();
            outStr.close();
        } catch (Exception e) {
            //LOGGER.error("关闭流对象出错 e:{}",e);
        }
        }
    }


    //防止中文文件名显示出错
    protected   String genAttachmentFileName(String cnName, String defaultName) {
        try {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }




    /**
     * 获取场景线路数据
     *
     * @param sceneId
     * @return
     */
    @GetMapping("/getSceneLineData")
    public ResponseResult getSceneLineData(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getSceneLineData(sceneId));
    }

    /**
     * 獲取隱患數據
     *
     * @param sceneId
     * @return
     */
    @GetMapping("/getDangerData")
    public ResponseResult getDangerData(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(sceneInfoService.getDangerData(sceneId));
    }

    /**
     * 获取多场景资源树
     *
     * @param sceneIds
     * @return
     */
    @GetMapping("/getMultiSceneResources")
    public ResponseResult getMultiSceneResources(@RequestParam("sceneIds") List<String> sceneIds) {
        return ResponseResult.success(sceneInfoService.getMultiSceneResourceTree(sceneIds));
    }


    /**
     * 获取应急力量（一张图）
     */
    @GetMapping("/getEmergencyForcesOfScene")
    public ResponseResult getEmergencyForcesOfScene(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(sceneInfoService.getEmergencyForcesOfScene(taskId));
    }

    /**
     * 获取路线安排（一张图）
     */
    @GetMapping("/getRoadPlanOfTask")
    public ResponseResult getRoadPlanOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(sceneInfoService.getRoadPlanOfScene(taskId));
    }

    /**
     * 方案列表
     *
     * @return
     */
    @PostMapping("/getSchemeList")
    public ResponseResult getSchemeList(@RequestBody SceneInfo sceneInfo) {
        return ResponseResult.success(sceneInfoService.getSchemeList(sceneInfo));
    }


    /**
     * 获取方案数
     *
     * @return
     */
    @GetMapping("/getSchemeNum")
    public ResponseResult getSchemeNum() {
        return ResponseResult.success(sceneInfoService.count(new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0).eq(SceneInfo::getSchemeFlag, 1)));
    }

    /**
     * 新增方案
     *
     * @param sceneInfo
     * @return
     */
    @PostMapping("/addScheme")
    public ResponseResult addScheme(@RequestBody SceneInfo sceneInfo) {
        return ResponseResult.success(sceneInfoService.addScheme(sceneInfo));
    }

    /**
     * 复制场景到指定任务下
     *
     * @param id
     * @param taskId
     * @return
     */
    @GetMapping("/copyScene")
    public ResponseResult copyScene(@RequestParam("id") String id, @RequestParam("taskId") String taskId) {
        return ResponseResult.success(sceneInfoService.copyScene(id, null, taskId));
    }


    /**
     * 复制方案
     *
     * @param id
     * @return
     */
    @GetMapping("/copyScheme")
    public ResponseResult copyScheme(@RequestParam("id") String id) {
        return ResponseResult.success(sceneInfoService.copyScheme(id));
    }

    /**
     * 更新方案
     *
     * @param sceneInfo
     * @return
     */
    @PostMapping("/updateScheme")
    public ResponseResult updateScheme(@RequestBody SceneInfo sceneInfo) {
        return ResponseResult.success(sceneInfoService.updateById(sceneInfo));
    }

    /**
     * 删除方案
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteScheme")
    public ResponseResult deleteScheme(@RequestParam("id") String id) {
        return ResponseResult.success(sceneInfoService.update(new LambdaUpdateWrapper<SceneInfo>()
                .set(SceneInfo::getDeleteFlag, 1).eq(SceneInfo::getId, id)));
    }

    @GetMapping("/getSchemeStatistics")
    public ResponseResult getSchemeStatistics() {
        return ResponseResult.success(sceneInfoService.getSchemeStatistics());
    }

    @GetMapping("/getSceneTree")
    public ResponseResult getSceneTree(@RequestParam("type") String type, @RequestParam(value = "name", required = false) String name) {
        return ResponseResult.success(sceneInfoService.getSceneTree(type, name));
    }

    @PostMapping("/deployPolice")
    public ResponseResult deployPolice(@RequestBody DeployPoliceData deployPoliceData) {
        return ResponseResult.success(sceneInfoService.deployPolice(deployPoliceData));
    }
}
